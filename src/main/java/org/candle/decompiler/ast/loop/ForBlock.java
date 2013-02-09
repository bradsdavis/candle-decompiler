package org.candle.decompiler.ast.loop;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;

public class ForBlock extends Block {

	private final Block forInit;
	private final BooleanBranchIntermediate forExpression;
	private final Block forUpdate;
	
	public ForBlock(Block forInit, BooleanBranchIntermediate forExpression, Block forUpdate) {
		this.forInit = forInit;
		this.forExpression = forExpression;
		this.forUpdate = forUpdate;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		
		builder.append("for(");
		
		//initialize.
		forInit.write(builder);
		builder.append(" ");
		
		//expression.
		forExpression.getExpression().write(builder);
		builder.append("; ");
		
		//increment.
		forUpdate.write(builder);
		
		builder.append(")");
		super.write(builder);
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
