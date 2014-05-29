package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;


public class FieldReference extends Expression {
	protected Expression target;
	protected String fieldName;

	public FieldReference(InstructionHandle instructionHandle, Expression target, String fieldName) {
		super(instructionHandle);
		this.target = target;
		this.fieldName = fieldName;
	}
	
	public Expression getTarget() {
		return target;
	}
	
	public void setTarget(Expression target) {
		this.target = target;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void write(Writer builder) throws IOException {
		target.write(builder);
		builder.append(".");
		builder.append(fieldName);
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(target);
	}
	
}
