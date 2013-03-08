package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Resolved extends Expression implements TypedExpression {
	private static final Log LOG = LogFactory.getLog(Resolved.class);
	private final Type type;
	private final String value;
	
	public Resolved(InstructionHandle instructionHandle, Type type, String value) {
		super(instructionHandle);
		this.type = type;
		this.value = value;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append(value);
	}
	
	public Type getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
}
