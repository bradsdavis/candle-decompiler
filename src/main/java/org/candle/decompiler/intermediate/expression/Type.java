package org.candle.decompiler.intermediate.expression;

public class Type<T> {

	private T value;
	
	public Type(T value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
}
