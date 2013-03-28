package org.candle.decompiler.instruction.graph.context;

import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

public class PositionalInstructionListener implements GraphListener<InstructionHandle, IntermediateEdge> {

	private final Map<Integer, InstructionHandle> positionMap;
	
	public PositionalInstructionListener(Map<Integer, InstructionHandle> positionMap) {
		this.positionMap = positionMap;
	}
	
	@Override
	public void vertexAdded(GraphVertexChangeEvent<InstructionHandle> e) {
		positionMap.put(e.getVertex().getPosition(), e.getVertex());
	}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<InstructionHandle> e) {
		positionMap.remove(e.getVertex().getPosition());
	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<InstructionHandle, IntermediateEdge> e) {
		
	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<InstructionHandle, IntermediateEdge> e) {
		
	}


}
