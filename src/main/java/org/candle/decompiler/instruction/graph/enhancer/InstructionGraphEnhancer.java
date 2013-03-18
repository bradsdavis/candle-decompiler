package org.candle.decompiler.instruction.graph.enhancer;

import org.candle.decompiler.instruction.graph.InstructionGraphContext;

public abstract class InstructionGraphEnhancer {

	protected final InstructionGraphContext igc;

	public InstructionGraphEnhancer(InstructionGraphContext igc) {
		this.igc = igc;
	}
	
	public abstract void process();
}
