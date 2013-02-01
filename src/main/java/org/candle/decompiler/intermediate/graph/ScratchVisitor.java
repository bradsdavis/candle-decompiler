package org.candle.decompiler.intermediate.graph;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.StatementBlock;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class ScratchVisitor extends GraphIntermediateVisitor {

	
	public ScratchVisitor(DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph) {
		super(intermediateGraph);
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
				
				retract(line);
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
		
		retract(line);
	}
}
