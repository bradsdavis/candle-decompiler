package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class ObjectType extends Expression {

	protected final String type;

	public ObjectType(InstructionHandle instructionHandle, String type) {
		super(instructionHandle);
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String generateSource() {
		return type;
	}
	
	@Override
	public String toString() {
		return generateSource();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectType other = (ObjectType) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		return expressions;
	}
}
