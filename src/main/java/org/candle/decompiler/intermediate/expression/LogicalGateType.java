package org.candle.decompiler.intermediate.expression;

public enum LogicalGateType {
	AND("&&"),
	OR("||");

	private final String operand;
	
	LogicalGateType(String operand) {
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		return this.operand;
	}
}
