package org.candle.decompiler.instruction.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.context.PositionalInstructionListener;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.candle.decompiler.util.GraphUtil;
import org.jgrapht.Graphs;
import org.jgrapht.graph.ListenableDirectedGraph;

public class InstructionGraphContext extends GraphUtil<InstructionHandle, InstructionEdge> {

	private final Map<Integer, InstructionHandle> positionMap = new HashMap<Integer, InstructionHandle>();
	
	public InstructionGraphContext(ListenableDirectedGraph<InstructionHandle, InstructionEdge> graph) {
		super(graph);
		this.graph.addGraphListener(new PositionalInstructionListener(positionMap));
	}

	public Map<Integer, InstructionHandle> getPositionMap() {
		return positionMap;
	}
	
	public List<InstructionHandle> getSuccessors(InstructionHandle iv) {
		return Graphs.successorListOf(this.graph, iv);
	}
	
	public List<InstructionHandle> getPredecessors(InstructionHandle iv) {
		return Graphs.predecessorListOf(this.graph, iv);
	}
	

	public InstructionHandle getSingleSuccessor(InstructionHandle ai) {
		List<InstructionHandle> successors = Graphs.successorListOf(this.graph, ai);
		
		if(successors.size() == 1) {
			return successors.get(0);
		}
		
		throw new IllegalStateException("Should have exactly 1 outgoing edge; actually: "+successors.size());
	}
	
	public InstructionHandle getSinglePredecessor(InstructionHandle ai) {
		List<InstructionHandle> predecessors = Graphs.predecessorListOf(this.graph, ai);
		
		if(predecessors.size() == 1) {
			return predecessors.get(0);
		}
		
		throw new IllegalStateException("Should only have 1 incoming edge.");
	}
	
}
