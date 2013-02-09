package org.candle.decompiler.ast.conditional;

import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;

public abstract class ConditionalBlock extends Block {
	
	public abstract BooleanBranchIntermediate getConditional();
	public abstract int getTargetBlockPosition();
	
}
