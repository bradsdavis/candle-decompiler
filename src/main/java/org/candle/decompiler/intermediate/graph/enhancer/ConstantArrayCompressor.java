package org.candle.decompiler.intermediate.graph.enhancer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.ArrayPositionReference;
import org.candle.decompiler.intermediate.expression.Assignment;
import org.candle.decompiler.intermediate.expression.ConstantArray;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.expression.NewConstantArrayInstance;
import org.candle.decompiler.intermediate.expression.Resolved;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class ConstantArrayCompressor extends GraphIntermediateVisitor {
	
	private static final Log LOG = LogFactory.getLog(ConstantArrayCompressor.class);
	
	public ConstantArrayCompressor(IntermediateGraphContext igc) {
		super(igc, false);
	}

	@Override
	public void visitCompleteLine(StatementIntermediate line) {
		//first, look for the 
		Assignment assignment = extractConstantArrayAssignment(line.getExpression());
		if(assignment == null) {
			return;
		}
		
		//at this point, we know both the statement is an assignment, and that the left assignment is to a constant array value.
		//find the one that is right before the array assignment.
		
		Declaration declaration = extractNextDeclaration(line);

		//if we didn't find the declaration, this must not be the constant array assignment proceeding the declaration.
		if(declaration == null) {
			return;
		}
		
		//check the right hand of the declaration...
		if(!(declaration.getAssignment().getRight() instanceof NewConstantArrayInstance)) {
			return;
		}
		NewConstantArrayInstance ncai = (NewConstantArrayInstance)declaration.getAssignment().getRight();
		Expression countExpression = ncai.getCount();
		
		
		AbstractIntermediate current = line;
		Map<Integer, Expression> values = new HashMap<Integer, Expression>();
		collectConstantAssignments(current, values);
		
		//create a new array...
		Integer count = toInteger(countExpression);
		List<Expression> expressions = new ArrayList<Expression>(count);
		for(int i=0, j=count; i<j; i++) {
			Expression exp = null;
			if(values.containsKey(i)) {
				exp = values.get(i);
			}
			else {
				exp = new Resolved(ncai.getInstructionHandle(), Type.NULL, "null");
			}
			expressions.add(i, exp);
		}
		
		
		//ok, we have the stack... now we need to just create a new expression.

		//create the contant...
		ConstantArray constantArray = new ConstantArray(declaration.getAssignment().getRight().getInstructionHandle(), expressions);
		declaration.getAssignment().setRight(constantArray);
		
		
		//excellent.  we have reordered the statements into the appropriate ContantArray assignment.  Now, we need to remove the dead nodes and heal the graph.
		AbstractIntermediate next = Graphs.successorListOf(igc.getIntermediateGraph(), line).get(0);
		
		//loop through the dead elements...
		//we know the number of dead items is equal to the number of values we found.
		healGraph(line, next, values.size());
	}
	
	public void healGraph(AbstractIntermediate current, AbstractIntermediate next, int deadElements) {
		for(int i=0, j=deadElements; i<j; i++) {
			//if this is the last element, redirect it's predecessors to the next node. 
			if(i+1 == j) {
				igc.redirectPredecessors(current, next);
			}
			
			
			AbstractIntermediate temp = null;
			//move back..
			List<AbstractIntermediate> predecessor = Graphs.predecessorListOf(igc.getIntermediateGraph(), current);
			if(predecessor.size() > 0) {
				temp = predecessor.get(0);
			}
			//retract current..
			igc.getIntermediateGraph().removeVertex(current);
			
			//set next... 
			current = temp;
			
		}
	}
	
	public void collectConstantAssignments(AbstractIntermediate current, Map<Integer, Expression> assignments) {
		StatementIntermediate si = (StatementIntermediate)current;
		
		//get the assignment...
		Assignment assignment = extractConstantArrayAssignment(si.getExpression());
		if(assignment == null) {
			return;
		}
		
		Expression right = assignment.getRight();
		ArrayPositionReference apr = (ArrayPositionReference)assignment.getLeft();
		assignments.put(toInteger(apr.getArrayPosition()), right);
		
		List<AbstractIntermediate> predecessor = Graphs.predecessorListOf(igc.getIntermediateGraph(), current);
		
		if(predecessor.size() != 1) {
			return;
		}
		
		if(!(predecessor.get(0) instanceof StatementIntermediate)) {
			return;
		}
		
		for(AbstractIntermediate a : predecessor) {
			collectConstantAssignments(a, assignments);
		}
	}
	
	public Integer toInteger(Expression apr) {
		StringWriter pos = new StringWriter();
		try {
			apr.write(pos);
		} catch (IOException e) {
			throw new IllegalStateException("Position is not an integer.", e);
		}
		
		return Integer.parseInt(pos.toString());
	}
	
	public Declaration extractNextDeclaration(StatementIntermediate statement) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(igc.getIntermediateGraph(), statement);
		if(successors.size() != 1) {
			return null;
		}
		
		if(!(successors.get(0) instanceof StatementIntermediate)) {
			return null;
		}
		
		StatementIntermediate si = (StatementIntermediate)successors.get(0);
		
		if(si.getExpression() instanceof Declaration)  {
			return (Declaration)si.getExpression();
		}
		
		return null;
	}
	
	public Assignment extractConstantArrayAssignment(Expression line) {
		if(!(line instanceof Assignment)) {
			return null;
		}
		
		Assignment assignment = (Assignment)line;
		
		if(!(assignment.getLeft() instanceof ArrayPositionReference)) {
			return null;
		}
		
		ArrayPositionReference apr = (ArrayPositionReference)assignment.getLeft();
		if(!(apr.getArrayReference() instanceof NewConstantArrayInstance)) {
			return null;
		}
		
		return assignment;
	}
}
