package org.candle.decompiler.intermediate.graph.context;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

public class PositionalIntermediateListener implements GraphListener<AbstractIntermediate, IntermediateEdge> {
	private static final Log LOG = LogFactory.getLog(PositionalIntermediateListener.class);
	
	private final IntermediateGraphContext igc;
	private Set<AbstractIntermediate> queuePosition = new HashSet<AbstractIntermediate>();
	
	public PositionalIntermediateListener(IntermediateGraphContext igc) {
		this.igc = igc;
	}
	
	@Override
	public void vertexAdded(GraphVertexChangeEvent<AbstractIntermediate> e) {
		if(!this.igc.getOrderedIntermediate().add(e.getVertex())) {
			//queue it up.
			queuePosition.add(e.getVertex());
			LOG.info("Queueing up vertex: "+e.getVertex());
		}
	}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<AbstractIntermediate> e) {
		this.igc.getOrderedIntermediate().remove(e.getVertex());
		
		//remove the queued object if it is there too.
		queuePosition.remove(e.getVertex());
		
		for(AbstractIntermediate ai : queuePosition) {
			if(ai.getInstruction() == e.getVertex().getInstruction()) {
				LOG.info("Removed vertex: "+e.getVertex());
				LOG.info("\tLocated queued vertex: "+ai);
				
				this.igc.getOrderedIntermediate().add(ai);
				break;
			}
		}
	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<AbstractIntermediate, IntermediateEdge> e) {

	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<AbstractIntermediate, IntermediateEdge> e) {
		
	}

	
}
