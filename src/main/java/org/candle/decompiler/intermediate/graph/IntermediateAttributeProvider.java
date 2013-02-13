package org.candle.decompiler.intermediate.graph;

import java.util.HashMap;
import java.util.Map;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.jgrapht.ext.ComponentAttributeProvider;

public class IntermediateAttributeProvider implements ComponentAttributeProvider<AbstractIntermediate> {

	@Override
	public Map<String, String> getComponentAttributes(AbstractIntermediate component) {
		Map<String, String> attributes = new HashMap<String, String>();
		if(component instanceof BooleanBranchIntermediate) {
			attributes.put("shape", "diamond");
			//attributes.put("fillcolor", "#FFC285");
		}
		else if(component instanceof MultiBranchIntermediate) {
			attributes.put("shape", "hexagon");
			//attributes.put("fillcolor", "#FFC285");
		}
		else if(component instanceof CaseIntermediate) 
		{
			//attributes.put("shape", "box");
			attributes.put("fillcolor", "#EEE");
		}
		else {
			//attributes.put("shape", "box");
		}
		return attributes;
	}

}
