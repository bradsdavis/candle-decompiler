package org.candle.decompiler.ast;

import org.candle.decompiler.intermediate.code.BlockSerializable;

public abstract class SerializableBlock<T extends BlockSerializable> extends Block {

	protected final T intermediate;
	
	public SerializableBlock(T intermediate) {
		this.intermediate = intermediate;
	}
	
	@Override
	public int getEndBlockPosition() {
		return intermediate.getBlockRange().getEnd().getPosition();
	}

	@Override
	public int getStartBlockPosition() {
		return intermediate.getBlockRange().getStart().getPosition();
	}

}
