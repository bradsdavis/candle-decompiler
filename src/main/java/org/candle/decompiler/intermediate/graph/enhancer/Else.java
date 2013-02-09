package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class Else extends GraphIntermediateVisitor {

	public Else(IntermediateGraphContext igc) {
		super(igc, false);
	}


	@Override
	public void visitAbstractLine(AbstractIntermediate line) {
		//for all lines...
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		if(predecessors.size() == 0) {
			return;
		}
		
		TreeSet<GoToIntermediate> gotoIntermediates = new TreeSet<GoToIntermediate>(new IntermediateComparator());
		
		for(AbstractIntermediate predecessor : predecessors) {
			if(predecessor instanceof GoToIntermediate) {
				gotoIntermediates.add((GoToIntermediate)predecessor);
			}
		}
		
		if(gotoIntermediates.size() == 0) {
			return;
		}
		
		//now, the largest should be...
		GoToIntermediate maxGotoForBranch = gotoIntermediates.pollLast();
		
		if(maxGotoForBranch.getInstruction().getPosition() > line.getInstruction().getPosition()) 
		{
			return;
		}
		
		//find the element directly after this one...
		SortedSet<AbstractIntermediate> elseBranchElements = igc.getOrderedIntermediate().subSet(maxGotoForBranch, false, line, false);
		
		//get the first element...
		if(elseBranchElements.size() > 0) {
			AbstractIntermediate firstElseBlockElement = elseBranchElements.first();
			if(firstElseBlockElement instanceof StatementIntermediate) {
				//we should add the ELSE right away...
				
				addElseBlock(firstElseBlockElement);
				return;
			}
			
			if(firstElseBlockElement instanceof BooleanBranchIntermediate) {
				//only add ELSE if the child of conditional doesn't go to the target.
				BooleanBranchIntermediate ci = (BooleanBranchIntermediate)firstElseBlockElement;
				if(igc.getFalseTarget(ci) == line || igc.getTrueTarget(ci) == line) {
					//do nothing.
					return;
				}
				
				//else if this is an ElseIf, probably should be an IF.
				if(firstElseBlockElement instanceof ElseIfIntermediate) {
					IfIntermediate ifIntermediate = new IfIntermediate(firstElseBlockElement.getInstruction(), ((BooleanBranchIntermediate) firstElseBlockElement).getExpression());
					igc.getIntermediateGraph().addVertex(ifIntermediate);
					igc.redirectPredecessors(firstElseBlockElement, ifIntermediate);
					igc.redirectSuccessors(firstElseBlockElement, ifIntermediate);
					igc.getIntermediateGraph().removeVertex(firstElseBlockElement);
					
					//add the else between this conditional.
					addElseBlock(ifIntermediate);
				}
			}
		}
	}
	
	
	protected void addElseBlock(AbstractIntermediate ai) {

		ElseIntermediate elseIntermediate = new ElseIntermediate(ai.getInstruction().getPrev());
		igc.getIntermediateGraph().addVertex(elseIntermediate);
		igc.redirectPredecessors(ai, elseIntermediate);
		//add a link to the statement.
		
		igc.getIntermediateGraph().addEdge(elseIntermediate, ai);
	}
}
