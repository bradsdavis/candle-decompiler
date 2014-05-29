package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class ArrayAccess extends Expression {

	private Expression array;
	private Expression index;
	
	public ArrayAccess(InstructionHandle instructionHandle, Expression arrayReference, Expression arrayPosition) {
		super(instructionHandle);
		this.array = arrayReference;
		this.index = arrayPosition;
	}
	
	public Expression getIndex() {
		return index;
	}
	
	public void setIndex(Expression index) {
		this.index = index;
	}
	
	public Expression getArray() {
		return array;
	}
	
	public void setArray(Expression array) {
		this.array = array;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		array.write(builder);
		builder.append("[");
		index.write(builder);
		builder.append("]");
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(array);
		listener.accept(index);
	}
}
