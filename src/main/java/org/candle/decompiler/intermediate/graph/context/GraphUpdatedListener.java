package org.candle.decompiler.intermediate.graph.context;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

public class GraphUpdatedListener implements GraphListener<AbstractIntermediate, IntermediateEdge> {

	private boolean updated = false;
	
	public boolean isUpdated() {
		return updated;
	}
	
	public void reset() {
		this.updated = false;
	}
	
	@Override
	public void vertexAdded(GraphVertexChangeEvent<AbstractIntermediate> e) {
		updated = true;
	}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<AbstractIntermediate> e) {
		updated = true;
	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<AbstractIntermediate, IntermediateEdge> e) {
		updated = true;
		
	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<AbstractIntermediate, IntermediateEdge> e) {
		updated = true;
	}

}
