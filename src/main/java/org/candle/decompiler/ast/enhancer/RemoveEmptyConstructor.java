package org.candle.decompiler.ast.enhancer;

import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.ConstructorBlock;

public class RemoveEmptyConstructor implements BlockEnhancer {

	@Override
	public Block enhanceBlock(Block block) {
		if(block instanceof ConstructorBlock) {
			if(block.getChildren().size() == 0) {
				return null;
			}
		}
		
		return block;
	}

}
