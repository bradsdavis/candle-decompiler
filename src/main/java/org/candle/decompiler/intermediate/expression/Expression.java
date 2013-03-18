package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.Sourceable;

public abstract class Expression implements Sourceable, Cloneable {

	protected InstructionHandle InstructionHandle;
	
	public Expression(InstructionHandle InstructionHandle) {
		this.InstructionHandle = InstructionHandle;
	}
	
	public InstructionHandle getInstructionHandle() {
		return InstructionHandle;
	}
	
	public void setInstructionHandle(InstructionHandle instructionHandle) {
		this.InstructionHandle = instructionHandle;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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
}
