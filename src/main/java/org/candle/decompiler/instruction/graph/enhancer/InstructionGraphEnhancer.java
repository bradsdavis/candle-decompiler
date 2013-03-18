package org.candle.decompiler.instruction.graph.enhancer;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;

public abstract class InstructionGraphEnhancer extends EmptyVisitor {

	protected final InstructionGraphContext igc;
	protected InstructionHandle current = null;
	
	public InstructionGraphEnhancer(InstructionGraphContext igc) {
		this.igc = igc;
	}
	
	public void process() {
		
		Set<InstructionHandle> ivs = new HashSet<InstructionHandle>(igc.getGraph().vertexSet());
		
		for(InstructionHandle iv : ivs) {
			current = iv;
			Instruction instruction = iv.getInstruction();
			instruction.accept(this);
		}
	}
}
