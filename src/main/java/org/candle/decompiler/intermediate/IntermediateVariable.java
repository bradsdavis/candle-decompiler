package org.candle.decompiler.intermediate;

import org.apache.bcel.generic.Type;

public class IntermediateVariable {

	private final String name;
	private final Type type;
	
	public IntermediateVariable(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
}
