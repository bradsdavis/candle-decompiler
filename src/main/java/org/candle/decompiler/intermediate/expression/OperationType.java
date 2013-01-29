package org.candle.decompiler.intermediate.expression;


public enum OperationType {	
	GREATER(">"),
	GREATER_EQUAL(">="),
	LESS("<"),
	LESS_EQUAL("<="),
	EQ("=="),
	NE("!=");
	
	private final String operand;
	
	OperationType(String operand) {
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		return this.operand;
	}
}