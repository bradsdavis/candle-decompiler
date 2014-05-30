package org.candle.decompiler.intermediate.expression;

public class NumberType<T extends Number> extends Type<Number> {

	private boolean primative = false;
	private T value;
	
	public NumberType(T value, boolean primative) {
		super(value);
		this.primative = primative;
	}
	
	public NumberType(T value) {
		super(value);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
