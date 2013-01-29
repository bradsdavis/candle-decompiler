package org.candle.decompiler.ast.trycatch;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.Statement;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Variable;

public class CatchBlock extends Block {

	private final InstructionHandle startHandle;
	private final InstructionHandle endHandle;
	
	public CatchBlock(InstructionHandle startHandle, InstructionHandle endHandle) {
		this.startHandle = startHandle;
		this.endHandle = endHandle;
	}
	
	@Override
	public InstructionHandle getInstruction() {
		return this.startHandle;
	}
	
	@Override
	public int getStartBlockPosition() {
		return this.startHandle.getPosition();
	}

	@Override
	public int getEndBlockPosition() {
		return this.endHandle.getPosition();
	}
	
	@Override
	public String generateSource() {
		final String indent = buildIndent();
		
		//the resolved intro always should be first in the catchblock.
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		for(int i=0, j=children.size(); i<j; i++) {
			if(i == 0) {
				builder.append("catch (");
				Statement statement = (Statement)children.get(i);
				StatementIntermediate line = statement.getLine();
				Declaration expression = (Declaration)line.getExpression();
				org.candle.decompiler.intermediate.expression.ObjectType type = expression.getType();
				builder.append(type.getType());
				builder.append(" ");
				builder.append(((Variable)(expression.getAssignment().getLeft())).getName());
				builder.append(") {");
			}
			else {
				builder.append(children.get(i).generateSource());
			}
			builder.append(Block.NL);
		}
		builder.append(indent);
		builder.append("}");
		builder.append(Block.NL);
		return builder.toString();
	}

}
