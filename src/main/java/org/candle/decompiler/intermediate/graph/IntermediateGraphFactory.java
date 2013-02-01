package org.candle.decompiler.intermediate.graph;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Return;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class IntermediateGraphFactory extends EmptyIntermediateVisitor {

	private final IntermediateLineContext ilc;
	private final DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph;
	
	public IntermediateGraphFactory(IntermediateLineContext ilc) {
		//first, populate the map.
		this.intermediateGraph = new DefaultDirectedGraph<AbstractIntermediate, DefaultEdge>(DefaultEdge.class);
		this.ilc = ilc;
		
		Set<AbstractIntermediate> lines = new HashSet<AbstractIntermediate>(ilc.getIntermediate().values());
		for(AbstractIntermediate line : lines) {
			line.accept(this);
		}
	}
	
	public DirectedGraph<AbstractIntermediate, DefaultEdge> getIntermediateGraph() {
		return intermediateGraph;
	}
	
	
	@Override
	public void visitCompleteLine(StatementIntermediate line) {
		AbstractIntermediate next = ilc.getNext(line);
		
		//check to see if it is a return statement.
		if(line.getExpression() instanceof Return) {
			//don't add a line to next.
			return;
		}
		
		if(next != null) {
			//find how that actually maps to the abstract line..
			AbstractIntermediate intermediate = next;
			
			//now, we just add this into the graph.
			intermediateGraph.addVertex(intermediate);
			intermediateGraph.addEdge(line, intermediate);
		}
	}

	@Override
	public void visitGoToLine(GoToIntermediate line) {
		
		//find how that actually maps to the abstract line..
		AbstractIntermediate intermediate = ilc.getNext(line);
		intermediateGraph.addVertex(intermediate);
		line.setTarget(intermediate);
		
		//now, we just add this into the graph.
		intermediateGraph.addEdge(line, intermediate);
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		InstructionHandle next = line.getInstruction().getNext();
		
		//find how that actually maps to the abstract line..
		AbstractIntermediate nextIntermediate = ilc.getNext(next.getPosition());
		//now, we just add this into the graph.
		
		line.setFalseTarget(nextIntermediate);
		intermediateGraph.addVertex(nextIntermediate);
		intermediateGraph.addEdge(line, nextIntermediate);

		//also add the target.
		BranchHandle bi = ((BranchHandle)line.getInstruction());
		AbstractIntermediate targetIntermediate = ilc.getNext(bi.getTarget().getPosition());

		if(targetIntermediate == null) {
			System.out.println(line);
		}
		
		line.setTrueTarget(targetIntermediate);
		intermediateGraph.addVertex(targetIntermediate);
		intermediateGraph.addEdge(line, targetIntermediate);
	}

	@Override
	public void visitAbstractLine(AbstractIntermediate line) {
		//add the vertex.
		intermediateGraph.addVertex(line);
	}
}
