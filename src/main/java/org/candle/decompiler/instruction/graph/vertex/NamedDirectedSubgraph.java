package org.candle.decompiler.instruction.graph.vertex;

import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;

public class NamedDirectedSubgraph extends DirectedSubgraph<InstructionHandle, IntermediateEdge> {

	private final String name;
	public NamedDirectedSubgraph(String name, DirectedGraph<InstructionHandle, IntermediateEdge> base, Set<InstructionHandle> vertexSubset, Set<IntermediateEdge> edgeSubset) {
		super(base, vertexSubset, edgeSubset);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
