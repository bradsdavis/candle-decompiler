package org.candle.decompiler.ast.loop;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;

public class ForBlock extends Block {

	private final Block forInit;
	private final ConditionalIntermediate forExpression;
	private final Block forUpdate;
	
	public ForBlock(Block forInit, ConditionalIntermediate forExpression, Block forUpdate) {
		this.forInit = forInit;
		this.forExpression = forExpression;
		this.forUpdate = forUpdate;
	}
	
	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder();
		final String indent = buildIndent();
		builder.append(indent);
		
		builder.append("for(");
		
		//initialize.
		String increment = StringUtils.trim(forInit.generateSource());
		builder.append(increment);
		builder.append(" ");
		
		//expression.
		String expression = StringUtils.trim(forExpression.getExpression().generateSource());
		builder.append(expression);
		builder.append("; ");
		
		//increment.
		String update = StringUtils.trim(forUpdate.generateSource());
		update = StringUtils.removeEnd(update, ";");
		builder.append(update);
		
		
		builder.append(")");
		builder.append(super.generateSource());
		return builder.toString();
	}
	
	@Override
	public InstructionHandle getInstruction() {
		return forInit.getInstruction();
	}

	@Override
	public int getStartBlockPosition() {
		return forInit.getInstruction().getPosition();
	}

	@Override
	public int getEndBlockPosition() {
		return forUpdate.getEndBlockPosition();
	}

}
