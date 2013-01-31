package org.candle.decompiler.intermediate.graph;

import java.util.LinkedList;
import java.util.List;

import org.candle.decompiler.blockinterpreter.Visitor;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.LogicalGateConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateType;
import org.candle.decompiler.intermediate.expression.StatementBlock;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class IntermediateGraphCompressor implements Visitor {

	private final DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph;
	private List<AbstractIntermediate> retractVertex = new LinkedList<AbstractIntermediate>();
	
	
	public IntermediateGraphCompressor(DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph) {
		this.intermediateGraph = intermediateGraph;
		
		boolean conditionalUpdates = true;
		
		while(conditionalUpdates) {
			for(AbstractIntermediate vertex : intermediateGraph.vertexSet()) {
				vertex.accept(this);
			}
			
			if(retractVertex.size() == 0) {
				conditionalUpdates = false;
			}
			for(AbstractIntermediate kv : retractVertex) {
				intermediateGraph.removeVertex(kv);
			}
			retractVertex.clear();
		}
	}
	
	@Override
	public void visitCompleteLine(StatementIntermediate line) {
		if(true)return;

		//basically, we just need to take any statements who have 1 path in, and then merge with the path in.
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
		if(predecessors.size() == 1) {
			
			//find the lesser instruction.
			AbstractIntermediate ail = predecessors.get(0);
			if(ail instanceof StatementIntermediate) {
				System.out.println("Candidate to merge with existing line: "+line + " into: "+ail);
				//now we know we can merge.
				StatementIntermediate sil = (StatementIntermediate)ail;
				StatementBlock statementBlock = new StatementBlock(sil.getInstruction(), sil.getExpression(), line.getExpression());
				sil.setExpression(statementBlock);
				
				//remove all edges from p to statement.
				intermediateGraph.removeEdge(ail, line);
				
				List<AbstractIntermediate> successors = Graphs.successorListOf(intermediateGraph, line);
				for(AbstractIntermediate s : successors) {
					intermediateGraph.removeEdge(line, s);
					intermediateGraph.addEdge(sil, s);
				}
				
				retractVertex.add(line);
			}
			 
		}
	}

	@Override
	public void visitGoToLine(GoToIntermediate line) {
		if(true)return;

		//remove goto and heal graph.
		//basically, find everything going into the goto, and replace with the goto target.
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
		List<AbstractIntermediate> successors = Graphs.predecessorListOf(intermediateGraph, line);
		
		if(successors.size() > 1) {
			System.out.println("This is an error I think.");
		}
		
		for(AbstractIntermediate p : predecessors) {
			//remove all edges from p to goto.
			intermediateGraph.removeEdge(p, line);
			intermediateGraph.addEdge(p, line.getTarget());
		}
		
		for(AbstractIntermediate s : successors) {
			intermediateGraph.removeEdge(line, s);
		}
		
		retractVertex.add(line);
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(intermediateGraph, line);
		List<AbstractIntermediate> predecessor = Graphs.predecessorListOf(intermediateGraph, line);
		
		for(AbstractIntermediate i : predecessor) {
			//check to see whether the incoming is a conditional..
			if(i instanceof ConditionalIntermediate) {
				ConditionalIntermediate ci = (ConditionalIntermediate)i;
				
				//potential to merge.
				
				if(ci == line) {
					continue;
				}
				
				//merging or statements are when conditionals have 2 legs.  in that case, if both legs target only the same target
				//and one leg of the other conditional targets this conditional, then we can compress.
				
				//we already know here that the conditional i enters this node.  check whether the outcome of this node matches the other node.
				List<AbstractIntermediate> cSuccess = Graphs.successorListOf(intermediateGraph, ci);
				
				//first, remove self from list.
				cSuccess.remove(line);
				
				//next, for each successor of this, remove from other.
				for(AbstractIntermediate ai : successors) {
					cSuccess.remove(ai);
				}
				
				//if this is empty now, we can merge.
				if(cSuccess.size() == 0) {
					System.out.println("Merge: "+ line + " AND "+i);
					
					//check to see if we need to negate first...
					if(ci.getFalseTarget() == line.getTrueTarget()) {
						ci.negate();
						System.out.println("Need CI: "+ci);
					}
					else if(ci.getTrueTarget() == line.getFalseTarget()) {
						line.negate();
						System.out.println("Negated Line: "+line);
					}
					
					if(ci.getTrueTarget() == line.getTrueTarget()) {
						System.out.println("Don't even need to negate!");
						LogicalGateConditionalExpression expression = new LogicalGateConditionalExpression(line.getExpression(), ci.getExpression(), LogicalGateType.OR);
						line.setExpression(expression);
						
						List<AbstractIntermediate> cPrede = Graphs.predecessorListOf(intermediateGraph, ci);
						
						//find references to ci, redirect to line.
						for(AbstractIntermediate p : cPrede) {
							//remove the edge to ci, add one to line.
							if(!intermediateGraph.containsEdge(p, line)) {
								intermediateGraph.addEdge(p, line);
							}
							intermediateGraph.removeEdge(p, ci);
						}
						//now remove vertex.
						retractVertex.add(ci);
					}
				}
			}
		}
	}

	@Override
	public void visitAbstractLine(AbstractIntermediate line) {
		
	}

}
