package org.candle.decompiler.intermediate.graph;

import java.util.HashMap;
import java.util.Map;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BlockSerializable;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.jgrapht.ext.ComponentAttributeProvider;

public class IntermediateAttributeProvider implements ComponentAttributeProvider<AbstractIntermediate> {

	@Override
	public Map<String, String> getComponentAttributes(AbstractIntermediate component) {
		Map<String, String> attributes = new HashMap<String, String>();
		if(component instanceof BooleanBranchIntermediate) {
			attributes.put("shape", "diamond");
		}
		else if(component instanceof MultiBranchIntermediate) {
			attributes.put("shape", "hexagon");
		}
		else if(component instanceof CatchIntermediate)
		{
			attributes.put("shape", "rectangle");
		}
		else if(component instanceof CaseIntermediate) 
		{

		}

		if(component instanceof BlockSerializable) {
			BlockSerializable block = (BlockSerializable)component;
			
			if(block.getBlockRange().isRangeDetermined()) {
				attributes.put("fillcolor", "green");
			}
			else {
				attributes.put("fillcolor", "red");
			}
		}
		
		
		return attributes;
	}

}
