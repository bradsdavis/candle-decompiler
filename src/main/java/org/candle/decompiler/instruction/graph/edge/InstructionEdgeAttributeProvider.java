package org.candle.decompiler.instruction.graph.edge;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.ext.ComponentAttributeProvider;

public class InstructionEdgeAttributeProvider implements ComponentAttributeProvider<InstructionEdge> {

	@Override
	public Map<String, String> getComponentAttributes(InstructionEdge edge) {
		Map<String, String> attributes = new HashMap<String, String>();

		if(edge.getType() == EdgeType.BACK) {
			attributes.put("style", "dashed");
			attributes.put("color", "red");
		}
		else if(edge.getType() == EdgeType.EXCEPTION) {
			attributes.put("style", "dashed");
			attributes.put("color", "blue");
		}
		
		return attributes;
	}
}
