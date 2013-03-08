package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.SignatureUtility;

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
		String outputType = SignatureUtility.signatureToString(variable.getType().getSignature());
		
		if(StringUtils.contains(outputType, ".")) {
			outputType = StringUtils.substringAfterLast(outputType, ".");
		}
		
		builder.append(outputType);
		builder.append(" ");
		assignment.write(builder);
	}
	
	
}
