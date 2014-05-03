package org.candle.decompiler.instruction.graph.edge;

import java.util.HashMap;
import java.util.Map;

import org.candle.decompiler.intermediate.graph.edge.BooleanConditionEdge;
import org.candle.decompiler.intermediate.graph.edge.EdgeType;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.edge.SwitchEdge;
import org.jgrapht.ext.ComponentAttributeProvider;

public class InstructionEdgeAttributeProvider implements ComponentAttributeProvider<IntermediateEdge> {

	@Override
	public Map<String, String> getComponentAttributes(IntermediateEdge edge) {
		Map<String, String> attributes = new HashMap<String, String>();

		if(edge.getType() == EdgeType.BACK) {
			attributes.put("style", "dashed");
			attributes.put("color", "red");
		}
		else if(edge.getType() == EdgeType.EXCEPTION) {
			attributes.put("style", "dashed");
			attributes.put("color", "blue");
		}
		
		if(edge instanceof BooleanConditionEdge) {
			attributes.put("color", "orange");
		}
		
		if(edge instanceof SwitchEdge) {
			attributes.put("color", "yellow");
		}
		
		return attributes;
	}
}
