package org.candle.decompiler.intermediate.graph;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.ext.EdgeNameProvider;

public class IntermediateEdgeProvider implements EdgeNameProvider<IntermediateEdge> {

	private final IntermediateGraphContext igc;
	
	public IntermediateEdgeProvider(IntermediateGraphContext igc) {
		this.igc = igc;
	}
	
	@Override
	public String getEdgeName(IntermediateEdge edge) {
		return "";
	}

}
