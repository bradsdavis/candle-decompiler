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
		this.array = null;
		this.index = null;
		
		this.setArray(arrayReference);
		this.setIndex(arrayPosition);
	}
	
	public Expression getIndex() {
		return index;
	}
	
	public void setIndex(Expression index) {
		//unset parent to avoid leak.
		if(this.index != null) {
			this.index.setParent(null);
		}
		this.index = index;
		
		if(this.index != null) {
			this.index.setParent(this);
		}
	}
	
	public Expression getArray() {
		return array;
	}
	
	public void setArray(Expression array) {
		//unset parent to avoid leak.
		if(this.array != null) {
			this.array.setParent(null);
		}
		
		this.array = array;
		
		if(this.array != null) {
			this.array.setParent(this);
		}
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
		array.visit(listener);
		index.visit(listener);
	}
}
