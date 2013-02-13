package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.LinkedList;
import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.ArrayPositionReference;
import org.candle.decompiler.intermediate.expression.Assignment;
import org.candle.decompiler.intermediate.expression.ConstantArray;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.expression.NewConstantArrayInstance;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class ConstantArrayCompressor extends GraphIntermediateVisitor {

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
		
		
		AbstractIntermediate current = line;
		LinkedList<Expression> values = new LinkedList<Expression>();
		collectConstantAssignments(current, values);
		
		//ok, we have the stack... now we need to just create a new expression.

		//create the contant...
		ConstantArray constantArray = new ConstantArray(declaration.getAssignment().getRight().getInstructionHandle(), values);
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
	
	public void collectConstantAssignments(AbstractIntermediate current, LinkedList<Expression> assignments) {
		StatementIntermediate si = (StatementIntermediate)current;
		
		//get the assignment...
		Assignment assignment = extractConstantArrayAssignment(si.getExpression());
		if(assignment == null) {
			return;
		}
		
		Expression right = assignment.getRight();
		assignments.addFirst(right);
		
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
