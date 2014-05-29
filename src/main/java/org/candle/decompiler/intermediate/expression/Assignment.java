package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;


public class Assignment extends Expression {

	private Expression leftHandSide;
	private Expression rightHandSide;
	
	public Assignment(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.leftHandSide = left;
		this.rightHandSide = right;
	}
	
	public Expression getLeftHandSide() {
		return leftHandSide;
	}
	
	public Expression getRightHandSide() {
		return rightHandSide;
	}
	
	public void setLeftHandSide(Expression leftHandSide) {
		this.leftHandSide = leftHandSide;
	}
	
	public void setRightHandSide(Expression rightHandSide) {
		this.rightHandSide = rightHandSide;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		leftHandSide.write(builder);
		builder.append(" = ");
		rightHandSide.write(builder);
	}

	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(getLeftHandSide());
		listener.accept(getRightHandSide());
	}
	
}
