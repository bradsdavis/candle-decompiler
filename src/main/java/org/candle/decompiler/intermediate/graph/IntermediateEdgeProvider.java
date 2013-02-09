package org.candle.decompiler.intermediate.graph;

import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.ext.EdgeNameProvider;

public class IntermediateEdgeProvider implements EdgeNameProvider<IntermediateEdge> {

	private final IntermediateGraphContext igc;
	
	public IntermediateEdgeProvider(IntermediateGraphContext igc) {
		this.igc = igc;
	}
	
	@Override
	public String getEdgeName(IntermediateEdge edge) {
		//check to see if the edge is between a conditional...
		if(edge.getSource() instanceof BooleanBranchIntermediate) {
			BooleanBranchIntermediate ci = (BooleanBranchIntermediate)edge.getSource();
			if(igc.getTrueTarget(ci) == edge.getTarget()) {
				return "TRUE";
			}
			else {
				return "FALSE";
			}
		}
		
		return "";
	}

}
