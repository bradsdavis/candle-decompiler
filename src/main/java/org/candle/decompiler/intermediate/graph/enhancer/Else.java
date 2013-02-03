package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;

public class Else extends GraphIntermediateVisitor {

	private TreeSet<AbstractIntermediate> orderedVertexes;
	public Else(DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		super(intermediateGraph);
		
	}


	@Override
	public void visitAbstractLine(AbstractIntermediate line) {

		orderedVertexes = new TreeSet<AbstractIntermediate>(new IntermediateComparator());
		orderedVertexes.addAll(intermediateGraph.vertexSet());
		
		//for all lines...
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
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
		
		//find the element directly after this one...
		SortedSet<AbstractIntermediate> elseBranchElements = orderedVertexes.subSet(maxGotoForBranch, false, line, false);
		
		//get the first element...
		if(elseBranchElements.size() > 0) {
			AbstractIntermediate firstElseBlockElement = elseBranchElements.first();
			if(firstElseBlockElement instanceof StatementIntermediate) {
				//we should add the ELSE right away...
			}
			
			if(firstElseBlockElement instanceof ConditionalIntermediate) {
				//only add ELSE if the child of conditional doesn't go to the target.
				ConditionalIntermediate ci = (ConditionalIntermediate)firstElseBlockElement;
				if(ci.getFalseTarget() == line || ci.getTrueTarget() == line) {
					//do nothing.
					return;
				}
				
				//else if thie is an ElseIf, probably should be an IF.
				
				if(firstElseBlockElement instanceof ElseIfIntermediate) {
					IfIntermediate ifIntermediate = new IfIntermediate(firstElseBlockElement.getInstruction(), ((ConditionalIntermediate) firstElseBlockElement).getExpression());
					ifIntermediate.setTrueTarget(((ConditionalIntermediate) firstElseBlockElement).getTrueTarget());
					ifIntermediate.setFalseTarget(((ConditionalIntermediate) firstElseBlockElement).getFalseTarget());
					
					this.intermediateGraph.addVertex(ifIntermediate);
					
					redirectPredecessors(firstElseBlockElement, ifIntermediate);
					redirectSuccessors(firstElseBlockElement, ifIntermediate);
					this.intermediateGraph.removeVertex(firstElseBlockElement);
					
				}
			}
		}
		
		for(AbstractIntermediate elements : elseBranchElements) {
			System.out.println(elements);
		}
	}
	
	
}
