package org.candle.decompiler.intermediate.expression;


public interface ASTListener {
	public void accept(Expression e);
}
