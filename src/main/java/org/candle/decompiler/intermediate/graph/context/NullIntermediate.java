package org.candle.decompiler.intermediate.graph.context;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class NullIntermediate extends AbstractIntermediate {

	public NullIntermediate(InstructionHandle instruction) {
		super(instruction);
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		throw new IllegalStateException("Unimplemneted.");
	}

	
}
