package org.candle.decompiler.instruction.graph.enhancer;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;

public abstract class InstructionHandleEnhancer extends InstructionGraphEnhancer {

	protected InstructionHandle current = null;
	
	public InstructionHandleEnhancer(InstructionGraphContext igc) {
		super(igc);
	}
	
	public void process() {
		Set<InstructionHandle> ivs = new HashSet<InstructionHandle>(igc.getGraph().vertexSet());
		for(InstructionHandle ih : ivs) {
			this.current = ih;
			process(ih);
		}
	}
	
	public abstract void process(InstructionHandle ih);
}
