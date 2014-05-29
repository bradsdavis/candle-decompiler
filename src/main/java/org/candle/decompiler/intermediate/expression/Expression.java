package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.Sourceable;

public abstract class Expression extends ASTNode implements Sourceable, Cloneable {

	protected Expression parent;
	protected InstructionHandle instructionHandle;
	
	public Expression(InstructionHandle InstructionHandle) {
		this.instructionHandle = InstructionHandle;
	}
	
	public InstructionHandle getInstructionHandle() {
		return instructionHandle;
	}
	
	public void setInstructionHandle(InstructionHandle instructionHandle) {
		this.instructionHandle = instructionHandle;
	}
	
	public Expression getParent() {
		return this.parent;
	}
	
	public void setParent(Expression parent) {
		this.parent = parent;
	}
	
	public void visit(ASTListener listener) {
		listener.accept(this);
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
