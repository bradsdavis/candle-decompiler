package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class MultiConditional extends ConditionalExpression {
	private static final Log LOG = LogFactory.getLog(MultiConditional.class);
	
	private final Expression left;
	private final Expression right;
	
	private OperationType operation;
	
	public MultiConditional(InstructionHandle instructionHandle, Expression left, Expression right, OperationType operation) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
		this.operation = operation;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		left.write(writer);
		writer.append(" ").append(operation.toString()).append(" ");
		right.write(writer);
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
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
}
