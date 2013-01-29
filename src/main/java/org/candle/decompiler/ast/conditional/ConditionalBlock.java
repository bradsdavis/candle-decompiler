package org.candle.decompiler.ast.conditional;

import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;

public abstract class ConditionalBlock extends Block {
	
	public abstract ConditionalIntermediate getConditional();
	public abstract int getTargetBlockPosition();
	
}
