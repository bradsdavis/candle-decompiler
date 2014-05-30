package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Arithmetic extends Expression {

	private final ArithmeticType operation;
	private Expression left;
	private Expression right;
	
	public Arithmetic(InstructionHandle instructionHandle, Expression left, Expression right, ArithmeticType operation) {
		super(instructionHandle);
		this.operation = operation;
		this.left = null;
		this.right = null;
		
		this.setLeft(left);
		this.setRight(right);
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		left.write(builder);
		builder.append(" ").append(operation.toString()).append(" ");
		right.write(builder);
	}

	public Expression getLeft() {
		return left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	public void setLeft(Expression left) {
		if(this.left != null) {
			//unset left's parent
			this.left.setParent(null);
		}
		
		this.left = left;
		
		if(left != null) {
			left.setParent(this);
		}
	}
	
	public void setRight(Expression right) {
		if(this.right != null) {
			//unset left's parent
			this.right.setParent(null);
		}

		this.right = right;
		
		if(right != null) {
			right.setParent(this);
		}
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		getLeft().visit(listener);
		getRight().visit(listener);
	}
}
