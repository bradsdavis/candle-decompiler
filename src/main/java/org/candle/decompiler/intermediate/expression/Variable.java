package org.candle.decompiler.intermediate.expression;

import org.apache.bcel.generic.InstructionHandle;




public class Variable extends ObjectType {

	private final String name;
	
	public Variable(InstructionHandle instructionHandle, String type, String name) {
		super(instructionHandle, type);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String generateSource() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return generateSource();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (getType() == null) {
			if (other.getType() != null)
				return false;
		} else if (!getType().equals(other.getType()))
			return false;
		
		return true;
	}

	

	
}
