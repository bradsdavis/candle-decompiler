package org.candle.decompiler.intermediate.graph.edge;

import org.jgrapht.ext.EdgeNameProvider;

public class IntermediateEdgeProvider implements EdgeNameProvider<IntermediateEdge> {
	
	@Override
	public String getEdgeName(IntermediateEdge edge) {
		if(edge instanceof ConditionEdge) {
			if(((ConditionEdge) edge).isCondition()) {
				return "TRUE";
			}
			return "FALSE";
		}
		
		return "";
		
		
	}

}
