package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.bcel.generic.InstructionHandle;


public class MultiConditional extends ConditionalExpression {
	private static final Log LOG = LogFactory.getLog(MultiConditional.class);
	
	private Expression left;
	private Expression right;
	
	private OperationType operation;
	
	public MultiConditional(InstructionHandle instructionHandle, Expression left, Expression right, OperationType operation) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
		this.operation = operation;
	}

	public Expression getLeft() {
		return left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	public void setLeft(Expression left) {
		this.left = left;
	}
	
	public void setRight(Expression right) {
		this.right = right;
	}
	
	public OperationType getOperation() {
		return operation;
	}
	
	public void setOperation(OperationType operation) {
		this.operation = operation;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		left.write(writer);
		writer.append(" ").append(operation.toString()).append(" ");
		right.write(writer);
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(getLeft());
		listener.accept(getRight());
	}
	

	@Override
	public void negate() {
		switch (operation) {
		case GREATER:
			operation = OperationType.LESS_EQUAL;
			break;
		case LESS:
			operation = OperationType.GREATER_EQUAL;
			break;
		case GREATER_EQUAL: 
			operation = OperationType.LESS;
			break;
		case LESS_EQUAL:
			operation = OperationType.GREATER;
			break;
		case EQ:
			operation = OperationType.NE;
			break;
		case NE:
			operation = OperationType.EQ;
			break;
		default:
			break;
		}
		
		if(left instanceof ConditionalExpression) {
			((ConditionalExpression) left).negate();
		}
		
		if(right instanceof ConditionalExpression) {
			((ConditionalExpression) right).negate();
		}
	}
	
}
