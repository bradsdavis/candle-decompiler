package org.candle.decompiler.intermediate.graph;

import java.util.HashSet;
import java.util.Set;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.graph.context.GraphUpdatedListener;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;

public abstract class GraphIntermediateVisitor extends EmptyIntermediateVisitor {

	protected final IntermediateComparator comparator = new IntermediateComparator();
	protected final IntermediateGraphContext igc;
	protected final boolean listenForUpdates;
	
	public GraphIntermediateVisitor(IntermediateGraphContext igc) {
		this(igc, false);
	}
	
	public GraphIntermediateVisitor(IntermediateGraphContext igc, boolean listenForUpdates) {
		this.igc = igc;
		this.listenForUpdates = listenForUpdates;
	}
	
	public void process() {

		GraphUpdatedListener gul = new GraphUpdatedListener();
		if(listenForUpdates) {
			igc.getGraph().addGraphListener(gul);
		}
		while(true) {
			Set<AbstractIntermediate> snapshot = new HashSet<AbstractIntermediate>();
			snapshot.addAll(igc.getGraph().vertexSet());
			
			for(AbstractIntermediate vertex : snapshot) {
				if(igc.getGraph().containsVertex(vertex)) {
					vertex.accept(this);
				}
			}
			//reset the update listener.
			if(!gul.isUpdated()) {
				break;
			}
			else {
				gul.reset();
			}
		}
		igc.getGraph().removeGraphListener(gul);
	}
}
