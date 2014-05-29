package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.bcel.generic.InstructionHandle;


public class Resolved extends Expression implements TypedExpression {
	private static final Log LOG = LogFactory.getLog(Resolved.class);
	private Type type;
	private String value;
	
	public Resolved(InstructionHandle instructionHandle, Type type, String value) {
		super(instructionHandle);
		this.type = type;
		this.value = value;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append(value);
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
	}
	
	public Type getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
