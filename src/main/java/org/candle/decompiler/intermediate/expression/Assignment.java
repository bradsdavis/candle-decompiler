package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;


public class Assignment extends Expression {

	private Expression leftHandSide = null;
	private Expression rightHandSide = null;
	
	public Assignment(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.setLeftHandSide(left);
		this.setRightHandSide(right);
	}
	
	public Expression getLeftHandSide() {
		return leftHandSide;
	}
	
	public Expression getRightHandSide() {
		return rightHandSide;
	}
	
	public void setLeftHandSide(Expression l) {
		if(leftHandSide != null) {
			this.leftHandSide.setParent(null);
		}
		
		this.leftHandSide = l;
		
		if(leftHandSide != null) {
			this.leftHandSide.setParent(this);
		}
	}
	
	public void setRightHandSide(Expression r) {
		if(rightHandSide != null) {
			this.rightHandSide.setParent(null);
		}
		
		this.rightHandSide = r;
		
		if(rightHandSide != null) {
			this.rightHandSide.setParent(this);
		}
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
