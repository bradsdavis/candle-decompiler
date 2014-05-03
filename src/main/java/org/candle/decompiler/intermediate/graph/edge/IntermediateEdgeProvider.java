package org.candle.decompiler.intermediate.graph.edge;

import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.expression.DefaultCase;
import org.jgrapht.ext.EdgeNameProvider;

public class IntermediateEdgeProvider implements EdgeNameProvider<IntermediateEdge> {
	
	@Override
	public String getEdgeName(IntermediateEdge edge) {
		if(edge instanceof BooleanConditionEdge) {
			if(((BooleanConditionEdge) edge).isCondition()) {
				return "TRUE";
			}
			return "FALSE";
		}
		
		if(edge instanceof SwitchEdge) {
			Case sc = ((SwitchEdge) edge).getSwitchCase();
			
			if(sc instanceof DefaultCase) {
				return "DEFAULT";
			}
			return sc.getExpression().toString();
		}
		
		return "";
		
		
	}

}
