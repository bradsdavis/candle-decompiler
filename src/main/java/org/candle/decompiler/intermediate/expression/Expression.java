package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.Sourceable;

public abstract class Expression implements Sourceable, Cloneable {

	protected InstructionHandle instructionHandle;
	
	public Expression(InstructionHandle instructionHandle) {
		this.instructionHandle = instructionHandle;
	}
	
	public InstructionHandle getInstructionHandle() {
		return instructionHandle;
	}
	
	public void setInstructionHandle(InstructionHandle instructionHandle) {
		this.instructionHandle = instructionHandle;
	}
	
	public abstract Set<Expression> nestedExpression();
	
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
