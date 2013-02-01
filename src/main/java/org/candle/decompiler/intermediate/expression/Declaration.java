package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;

public class Declaration extends Expression {

	private final Variable variable;
	private final Assignment assignment;
	
	public Declaration(InstructionHandle instructionHandle, Variable variable, Assignment assignment) {
		super(instructionHandle);
		this.variable = variable;
		this.assignment = assignment;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}

	@Override
	public void write(Writer builder) throws IOException {
		String outputType = Utility.signatureToString(variable.getType().getSignature());
		
		if(StringUtils.contains(outputType, ".")) {
			outputType = StringUtils.substringAfterLast(outputType, ".");
		}
		
		builder.append(outputType);
		builder.append(" ");
		assignment.write(builder);
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(assignment);
		expressions.add(variable);
		
		return expressions;
	}
	
}
