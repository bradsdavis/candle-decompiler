package org.candle.decompiler.ast.enhancer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.Statement;
import org.candle.decompiler.ast.loop.ForBlock;
import org.candle.decompiler.ast.loop.WhileBlock;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Increment;

public class WhileToForBlockEnhancer implements BlockEnhancer {
	private static final Log LOG = LogFactory.getLog(WhileToForBlockEnhancer.class);
	
	@Override
	public Block enhanceBlock(Block block) {

		Block lastChildBlock = null;
		Block precedingParentBlock = null;
		
		//check preconditions...
		
		//handle while blocks.
		if(!(block instanceof WhileBlock)) {
			return block;
		}
		
		//go to parent, and look for the statement before this block.
		if(block.getParent() == null) {
			//return because it must have parent.
			return block;
		}
		//end preconditions.
		
		List<Block> children = block.getChildren();
		if(children.size() > 0) {
			int lastPointer = children.size() - 1;
			
			//check last block.
			lastChildBlock = children.get(lastPointer);
		}
		
		//find preceding statement.
		int index = block.getParent().getChildren().indexOf(block);
		if(index == 0) {
			return block;
		}
		precedingParentBlock = block.getParent().getChildren().get(index - 1);
		
		
		//now, check the parent and child to see if it matches the pattern of assignment and increment.
		if(lastChildBlock instanceof Statement) {
			Statement lastStatement = ((Statement)lastChildBlock);
			
			if(lastStatement.getLine().getExpression() instanceof Increment) {
				Increment increment = (Increment)lastStatement.getLine().getExpression();
				
				//now, check that the line preceding is 
				if(precedingParentBlock instanceof Statement && ((Statement)precedingParentBlock).getLine().getExpression() instanceof Declaration) {
					Declaration declaration = (Declaration) ((Statement)precedingParentBlock).getLine().getExpression();
					
					if(declaration.getType().equals(increment.getVariable())) {
						LOG.info("Should be a for loop!");
						
						ForBlock forBlock = new ForBlock(precedingParentBlock, ((WhileBlock)block).getConditional(), lastChildBlock);
						//now populate children.
						forBlock.getChildren().addAll(block.getChildren());
						forBlock.getChildren().remove(lastChildBlock);
						
						//remove the preceding block from parent.
						forBlock.setParent(block.getParent());
						
						block.getParent().getChildren().remove(precedingParentBlock);
						return forBlock;
					}
				}
				
				
				
			}
		}
		
		
		
		return block;
	}

	
}
