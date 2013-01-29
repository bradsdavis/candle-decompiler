package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;

public class Declaration extends Expression {

	private final ObjectType type;
	private final Assignment assignment;
	
	public Declaration(InstructionHandle instructionHandle, ObjectType type, Assignment assignment) {
		super(instructionHandle);
		this.type = type;
		this.assignment = assignment;
	}
	
	public ObjectType getType() {
		return type;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}

	@Override
	public String generateSource() {
		String outputType = type.getType();
		if(StringUtils.contains(outputType, ".")) {
			outputType = StringUtils.substringAfterLast(outputType, ".");
		}
		
		StringBuilder builder = new StringBuilder(outputType);
		builder.append(" ");
		builder.append(assignment.generateSource());
		
		return builder.toString();
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(assignment);
		expressions.add(type);
		
		return expressions;
	}
	
}
