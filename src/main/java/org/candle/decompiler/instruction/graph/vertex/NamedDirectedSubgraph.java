package org.candle.decompiler.instruction.graph.vertex;

import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;

public class NamedDirectedSubgraph extends DirectedSubgraph<InstructionHandle, InstructionEdge> {

	private final String name;
	public NamedDirectedSubgraph(String name, DirectedGraph<InstructionHandle, InstructionEdge> base, Set<InstructionHandle> vertexSubset, Set<InstructionEdge> edgeSubset) {
		super(base, vertexSubset, edgeSubset);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
