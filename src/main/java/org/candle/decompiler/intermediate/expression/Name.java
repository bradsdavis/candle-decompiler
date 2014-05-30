package org.candle.decompiler.intermediate.expression;

public abstract class Name {

	protected String value;
	
	public Name(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public abstract String toString();
}
