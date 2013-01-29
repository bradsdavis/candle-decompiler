package org.candle.decompiler.ast.enhancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.candle.decompiler.ast.Block;

public class RecursiveClassEnhancer implements BlockEnhancer {

	private final List<BlockEnhancer> enhancers;
	public RecursiveClassEnhancer() {
		enhancers = new ArrayList<BlockEnhancer>();
		enhancers.add(new RetractBlankReturnEnhancer());
		enhancers.add(new WhileToForBlockEnhancer());
		enhancers.add(new RemoveEmptyConstructor());
		enhancers.add(new RemoveExtendsObject());
	}
	
	@Override
	public Block enhanceBlock(Block block) {
		
		Map<Block, Block> updates = new HashMap<Block, Block>();
		for(Block child : block.getChildren()) {
			Block returned = this.enhanceBlock(child);
			//if they aren't the same object, register for update.
			if(child != returned) {
				updates.put(child, returned);
			}
		}

		//replace updates.
		for(Block child : updates.keySet()) {
			//update the child reference.
			int index = block.getChildren().indexOf(child);
			//set that index.
			
			if(updates.get(child) == null) {
				//remove.
				block.getChildren().remove(child);
			}
			else {
				block.getChildren().set(index, updates.get(child));
			}
			
		}
		
		
		for(BlockEnhancer enhancer : enhancers) {
			block = enhancer.enhanceBlock(block);
		}
		
		return block;
	}

}
