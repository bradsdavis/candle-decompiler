package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.Type;
import org.candle.decompiler.ast.SignatureUtility;
import org.apache.bcel.generic.InstructionHandle;

public class ObjectType extends Expression implements TypedExpression {

	protected Type type;

	public ObjectType(InstructionHandle instructionHandle, Type type) {
		super(instructionHandle);
		this.type = type; 
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		String signature = SignatureUtility.signatureToString(type.getSignature());
		writer.append(signature);
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
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
}
