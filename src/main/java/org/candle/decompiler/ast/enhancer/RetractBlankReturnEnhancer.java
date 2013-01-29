package org.candle.decompiler.ast.enhancer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.MethodBlock;
import org.candle.decompiler.ast.Statement;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.expression.Return;

public class RetractBlankReturnEnhancer implements BlockEnhancer {
	private static final Log LOG = LogFactory.getLog(RetractBlankReturnEnhancer.class);
	@Override
	public Block enhanceBlock(Block block) {
		if(!(block instanceof MethodBlock)) {
			return block;
		}
		
		MethodBlock mb = (MethodBlock)block;
		
		if(mb.getChildren().size() > 0) {
			//get last child.
			int lastIndex = mb.getChildren().size() -1;
			Block lastStatement = mb.getChildren().get(lastIndex);
			
			if(lastStatement instanceof Statement) {
				//check to see if the statement's expression is a return expression.
				Expression exp = ((Statement)lastStatement).getLine().getExpression();
				
				if(exp instanceof Return) 
				{
					//now check the return type.
					
					if(((Return)exp).getChild() == null) {
						//we can remove it.
						
						LOG.info("Retracting last return.");
						mb.getChildren().remove(lastStatement);
					}
				}
			}
		}
		
		
		return block;
	}

}
