package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.expression.ArrayPositionReference;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.expression.GeneratedVariable;
import org.candle.decompiler.intermediate.expression.Variable;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

/**
 * 
 * 
 * @author bradsdavis
 *
 */
public class ArrayForToEnhancedFor extends GraphIntermediateVisitor {
	private static final Log LOG = LogFactory.getLog(ArrayForToEnhancedFor.class);
	
	public ArrayForToEnhancedFor(IntermediateGraphContext igc) {
		super(igc, false);
	}

	@Override
	public void visitForLoopLine(ForIntermediate line) {
		//we need to look at the previous 2 statements and the first statement child.
		AbstractIntermediate arrayLenthCandidate = getForExteriorPredecessor(line);
		
		//check that the array length candidate's declared length variable is used in the for's condition.
		
		AbstractIntermediate tempArrayCandidate = null;
		AbstractIntermediate firstChild = igc.getTrueTarget(line);
		
		if(arrayLenthCandidate != null) {
			tempArrayCandidate = getSinglePredecessor(arrayLenthCandidate);
		}

		//if either of these are null, then this doesn't match.
		if(arrayLenthCandidate == null || tempArrayCandidate == null) {
			return;
		}
		
		GeneratedVariable generatedArrayLength = extractGeneratedVariableDeclaration(arrayLenthCandidate);
		GeneratedVariable generatedArrayReference = extractGeneratedVariableDeclaration(tempArrayCandidate);
		GeneratedVariable arrayIteratorValue = extractGeneratedVariableDeclaration(line.getInit());
		
		if(generatedArrayLength == null || generatedArrayReference == null || arrayIteratorValue == null) {
			return;
		}
		
		//now validate the types.  array length should be an integer.  the array reference, well, should be an array ;)
		//the iterator should be an integer.

		if(generatedArrayLength.getType() != Type.INT) {
			
			if(!(generatedArrayReference.getType() instanceof ArrayType)) {
				return;
			}
			
			if(arrayIteratorValue.getType() != Type.INT) {
				return;
			}
		}
		
		//great; at this point we know the pattern matches.  check the next statement to see if the transformation is possible.
		//format should be: 40 : GENERATED_ARRAY_REFERENCE_TYPE i = GENERATED_ARRAY_REFERENCE[ARRAY_ITERATOR_VALUE] | 
		StatementIntermediate childDeclarationStatement = ((StatementIntermediate)firstChild);
		Declaration childDeclaration = (Declaration)childDeclarationStatement.getExpression();
		
		if(firstMatchesGeneratedVariables(childDeclarationStatement, generatedArrayReference, arrayIteratorValue)) {
			
			LOG.debug("Likely a enhanced for loop for array: "+generatedArrayLength + " , "+ generatedArrayReference);
			
			//we are good to go here.  Now we just need to reorganize the graph.  Start by creating the new enhanced for loop.
			EnhancedForIntermediate efl = new EnhancedForIntermediate(line.getInstruction(), line.getConditionalIntermediate(), childDeclaration.getVariable(), extractExpressionFromGeneratedArrayAssignment(tempArrayCandidate));
			efl.setTrueBranch(line.getTrueBranch());
			efl.setFalseBranch(line.getFalseBranch());
			//add the new node...
			this.igc.getIntermediateGraph().addVertex(efl);
			
			//now, we just need to redirect.
			igc.redirectPredecessors(tempArrayCandidate, efl);
			igc.redirectPredecessors(line, efl);
			igc.redirectSuccessors(line, efl);
			
			AbstractIntermediate nextChild = getSingleSuccessor(firstChild);
			igc.redirectPredecessors(firstChild, nextChild);
			
			//remove line.
			igc.getIntermediateGraph().removeVertex(line);
			igc.getIntermediateGraph().removeVertex(tempArrayCandidate);
			igc.getIntermediateGraph().removeVertex(firstChild);
			igc.getIntermediateGraph().removeVertex(arrayLenthCandidate);
		}
	}
	
	private Expression extractExpressionFromGeneratedArrayAssignment(AbstractIntermediate declaration) {
		if(declaration instanceof StatementIntermediate) {
			StatementIntermediate si = (StatementIntermediate)declaration;
			
			Declaration dec = (Declaration)si.getExpression();
			return dec.getAssignment().getRight();
		}
		
		return null;
	}
	
	private boolean firstMatchesGeneratedVariables(StatementIntermediate first, GeneratedVariable generatedArrayRef, GeneratedVariable generatedArrayIterator) {
		Declaration childDeclaration = (Declaration)first.getExpression();
		Expression right = childDeclaration.getAssignment().getRight();
		
		if(right instanceof ArrayPositionReference) {
			ArrayPositionReference apr = (ArrayPositionReference)right;
			
			if(!(apr.getArrayPosition() instanceof Variable)) {
				return false;
			}
			if(!(apr.getArrayReference() instanceof Variable)) {
				return false;
			}
			
			//cast both to variable. check the variables match the name and type of the ones found above.
			Variable arrayPosition = (Variable)apr.getArrayPosition();
			Variable arrayRef = (Variable)apr.getArrayReference();
			
			if(!StringUtils.equals(arrayPosition.getName(), generatedArrayIterator.getName())) {
				return false;
			}
			
			if(!StringUtils.equals(arrayRef.getName(), generatedArrayRef.getName())) {
				return false;
			}
			
			return true;
		}
		
		return true;
	}
	
	
	
	private AbstractIntermediate getForExteriorPredecessor(ForIntermediate line) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		
		//loop max min
		int min = line.getInstruction().getPosition();
		int max = igc.getFalseTarget(line).getInstruction().getPosition();
		
		Set<AbstractIntermediate> nested = new HashSet<AbstractIntermediate>();
		//eliminate nested predecessors.
		for(AbstractIntermediate pred : predecessors) {
 			int curPred = pred.getInstruction().getPosition();
 			if(curPred > min && curPred < max) {
 				nested.add(pred);
 			}
		}
		
		predecessors.removeAll(nested);

		if(predecessors.size() == 1) {
			return predecessors.get(0);
		}
		return null;
	}
	
	private AbstractIntermediate getSingleSuccessor(AbstractIntermediate line) {
		List<AbstractIntermediate> successor = Graphs.successorListOf(igc.getIntermediateGraph(), line);
		
		if(successor.size() == 1) {
			return successor.get(0);
		}
		return null;
	}
	
	
	private AbstractIntermediate getSinglePredecessor(AbstractIntermediate line) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		
		if(predecessors.size() == 1) {
			return predecessors.get(0);
		}
		return null;
	}
	
	private GeneratedVariable extractGeneratedVariableDeclaration(AbstractIntermediate declaration) {
		if(declaration instanceof StatementIntermediate) {
			StatementIntermediate si = (StatementIntermediate)declaration; 
			return extractGeneratedVariableDeclaration(si.getExpression());
		}
		
		return null;
	}
	
	private GeneratedVariable extractGeneratedVariableDeclaration(Expression expression) {
		if(expression instanceof Declaration) {
			Declaration dec = (Declaration)expression;
			
			Variable var = dec.getVariable();
			
			if(var instanceof GeneratedVariable) {
				return (GeneratedVariable)var;
			}
		}
		return null;
	}

	
	
}
