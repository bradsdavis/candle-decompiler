package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class StatementBlock extends Expression {
	private Expression left;
	private Expression right;
	
	public StatementBlock(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	public Expression getLeft() {
		return left;
	}
	
	public void setLeft(Expression left) {
		this.left = left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	public void setRight(Expression right) {
		this.right = right;
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(getLeft());
		listener.accept(getRight());
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		left.write(writer);
		writer.append("\n");
		right.write(writer);
	}
}

