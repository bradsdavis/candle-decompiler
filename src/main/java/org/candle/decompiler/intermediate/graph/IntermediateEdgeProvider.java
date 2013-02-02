package org.candle.decompiler.intermediate.graph;

import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.jgrapht.ext.EdgeNameProvider;

public class IntermediateEdgeProvider implements EdgeNameProvider<IntermediateEdge> {

	@Override
	public String getEdgeName(IntermediateEdge edge) {
		//check to see if the edge is between a conditional...
		if(edge.getSource() instanceof ConditionalIntermediate) {
			ConditionalIntermediate ci = (ConditionalIntermediate)edge.getSource();
			if(ci.getTrueTarget() == edge.getTarget()) {
				return "TRUE";
			}
			else {
				return "FALSE";
			}
		}
		
		return "";
	}

}
