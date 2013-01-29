package org.candle.decompiler.ast.enhancer;

import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.ClassBlock;

public class RemoveExtendsObject implements BlockEnhancer {

	@Override
	public Block enhanceBlock(Block block) {
		if(block instanceof ClassBlock) {
			ClassBlock cb = ((ClassBlock)block);
			String superClass = cb.getSuperClassName();
			
			//if the superclass is object...
			if(StringUtils.equals(superClass, "java.lang.Object")) {
				//we can remove this from the signature...
				
				cb.setSuperClassName(null);
			}
		}
		
		return block;
	}

}
