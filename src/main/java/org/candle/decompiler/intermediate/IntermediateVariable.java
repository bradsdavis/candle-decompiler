package org.candle.decompiler.intermediate;

public class IntermediateVariable {

	private final String name;
	private final String type;
	
	public IntermediateVariable(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
}
