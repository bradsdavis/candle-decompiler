package org.candle.decompiler.intermediate.expression;

public class SimpleName extends Name {

	public SimpleName(String value) {
		super(value);
	}
	
	@Override
	public String toString() {
		return value;
	}
}
