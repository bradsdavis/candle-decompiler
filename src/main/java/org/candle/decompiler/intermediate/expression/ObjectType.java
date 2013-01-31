package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

public class ObjectType extends Expression implements TypedExpression {

	protected final Type type;

	public ObjectType(InstructionHandle instructionHandle, Type type) {
		super(instructionHandle);
		this.type = type; 
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		String signature = Utility.signatureToString(type.getSignature());
		writer.append(signature);
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
