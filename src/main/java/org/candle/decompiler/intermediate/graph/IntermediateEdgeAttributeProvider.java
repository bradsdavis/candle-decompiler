package org.candle.decompiler.intermediate.graph;

import java.util.HashMap;
import java.util.Map;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.jgrapht.ext.ComponentAttributeProvider;

public class IntermediateEdgeAttributeProvider implements ComponentAttributeProvider<IntermediateEdge> {

	@Override
	public Map<String, String> getComponentAttributes(IntermediateEdge edge) {
		Map<String, String> attributes = new HashMap<String, String>();
		
		AbstractIntermediate source = (AbstractIntermediate)edge.getSource();
		AbstractIntermediate target = (AbstractIntermediate)edge.getTarget();
		
		if(source instanceof TryIntermediate && target instanceof CatchIntermediate) {
			attributes.put("style", "dashed");
		}
		
		
		
		return attributes;
	}

}
