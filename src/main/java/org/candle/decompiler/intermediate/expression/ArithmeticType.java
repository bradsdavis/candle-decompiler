package org.candle.decompiler.intermediate.expression;

public enum ArithmeticType {
	ADD("+"),
	SUBTRACT("-"),
	MULTIPLY("*"),
	REMAINDER("%"),
	SHIFT_RIGHT_SIGNED(">>"),
	SHIFT_RIGHT_UNSIGNED(">>>"),
	SHIFT_LEFT("<<"),
	BITWISE_XOR("^"),
	BITWISE_OR("|"),
	BITWISE_AND("&"),
	DIVIDE("/");
	

	private final String operand;
	
	ArithmeticType(String operand) {
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		return this.operand;
	}
}
