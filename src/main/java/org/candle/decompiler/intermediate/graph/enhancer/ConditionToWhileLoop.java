package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;
import java.util.TreeSet;

import org.apache.bcel.generic.BranchHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.expression.Continue;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;
import org.jgrapht.alg.CycleDetector;

public class ConditionToWhileLoop extends GraphIntermediateVisitor {
	
	public ConditionToWhileLoop(IntermediateGraphContext igc) {
		super(igc, true);
	}

	@Override
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		
		CycleDetector<AbstractIntermediate, IntermediateEdge> cycleDetector = new CycleDetector<AbstractIntermediate, IntermediateEdge>(igc.getIntermediateGraph());
		if(!cycleDetector.detectCyclesContainingVertex(line)) {
			return;
		}
		
		//first, determine if the condition has two incoming lines.
		if(predecessors.size() >= 2) {
			//check to see that 1 predecessor is a GOTO.
			
			TreeSet<GoToIntermediate> incomingGotoNonNested = new TreeSet<GoToIntermediate>(new IntermediateComparator());
			TreeSet<GoToIntermediate> incomingGotoNested = new TreeSet<GoToIntermediate>(new IntermediateComparator());
			GoToIntermediate nonNestedLine = null;
			AbstractIntermediate otherLine = null;
			
			//classify.
			for(AbstractIntermediate predecessor : predecessors) {
				//check to see if 1 is GOTO.
				if(predecessor instanceof GoToIntermediate) {
					if(isNested(line, predecessor)) {
						incomingGotoNested.add((GoToIntermediate)predecessor);
					}
					else {
						incomingGotoNonNested.add((GoToIntermediate)predecessor);
					}
					continue;
				}
				else {
					otherLine = predecessor;
				}
			}
			
			//if there are more than one GOTO statements that are not-nested, return.
			if(incomingGotoNonNested.size() > 1) {
				return;
			}
			
			nonNestedLine = incomingGotoNonNested.pollLast();
			
			//stop if both conditions aren't met.
			if(nonNestedLine == null && otherLine == null) {
				return;
			}
			
			//check to validate that the GOTO instruction is less than the other incoming...
			if(comparator.before(otherLine, line)) {
				//take the lower condition...
				BranchHandle refHandle = null;
				if(comparator.before(nonNestedLine, line)) {
					refHandle = (BranchHandle)nonNestedLine.getInstruction();
				}
				else {
					refHandle = (BranchHandle)line.getInstruction();
				}
				
				
				WhileIntermediate whileIntermediate = new WhileIntermediate(refHandle, line);
				whileIntermediate.setTrueBranch(line.getTrueBranch());
				whileIntermediate.setFalseBranch(line.getFalseBranch());
				
				//add this to the graph.
				this.igc.getIntermediateGraph().addVertex(whileIntermediate);
				
				//get the incoming from the goto...
				igc.redirectPredecessors(nonNestedLine, whileIntermediate);
				igc.redirectSuccessors(line, whileIntermediate);
								
				//now, create line from other to while.
				this.igc.getIntermediateGraph().addEdge(otherLine, whileIntermediate);
				
				//now, remove the GOTO and Conditional Vertext from graph.
				igc.getIntermediateGraph().removeVertex(nonNestedLine);
				igc.getIntermediateGraph().removeVertex(line);
				
				//now, the other GOTO lines coming in should all be CONTINUE statements...
				for(GoToIntermediate gotoIntermediate : incomingGotoNested) {
					Continue continueExpression = new Continue(gotoIntermediate.getInstruction()); 
					StatementIntermediate continueIntermediate = new StatementIntermediate(gotoIntermediate.getInstruction(), continueExpression);

					//add the node...
					igc.getIntermediateGraph().addVertex(continueIntermediate);
					igc.redirectPredecessors(gotoIntermediate, continueIntermediate);
					//remove vertex.
					igc.getIntermediateGraph().removeVertex(gotoIntermediate);
					
					//now, add line to the loop.
					igc.getIntermediateGraph().addEdge(continueIntermediate, whileIntermediate);
				}
			}
		}
	}

	protected boolean isNested(BooleanBranchIntermediate ci, AbstractIntermediate ai) 
	{
		int max = igc.getFalseTarget(ci).getInstruction().getPosition();
		int min = igc.getTrueTarget(ci).getInstruction().getPosition();
		
		return (ai.getInstruction().getPosition() <= max && ai.getInstruction().getPosition() >= min);
		
		
	}
	
}
