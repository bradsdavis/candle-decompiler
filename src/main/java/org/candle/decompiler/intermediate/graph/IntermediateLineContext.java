package org.candle.decompiler.intermediate.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;

public class IntermediateLineContext {

	private final Map<InstructionHandle, AbstractIntermediate> intermediateMap;
	public IntermediateLineContext(List<AbstractIntermediate> lines) {
		this.intermediateMap = new HashMap<InstructionHandle, AbstractIntermediate>();
		for(AbstractIntermediate intermediate : lines) {
			System.out.println("ADDING HANDLE LINE: "+intermediate);
			for(InstructionHandle handle : intermediate.getAllHandles()) {
				System.out.println("ADDING HANDLE: "+handle +" to "+intermediate);
				
				//add map entry.
				this.intermediateMap.put(handle, intermediate);
			}
		}
		
		
	}
	
	public Set<AbstractIntermediate> linesWithinBounds(int lower, int upper) {
		Set<AbstractIntermediate> s = new HashSet<AbstractIntermediate>();
		
		for(AbstractIntermediate a : intermediateMap.values()) {
			boolean withinBounds = true;
			for(InstructionHandle ih : a.getAllHandles()) {
				if(ih.getPosition() > upper || ih.getPosition() < lower) {
					withinBounds = false;
					break;
				}
			}
			if(withinBounds) {
				s.add(a);
			}
		}
		
		return s;
	}
	
	public Map<InstructionHandle, AbstractIntermediate> getIntermediateMap() {
		return intermediateMap;
	}
}
