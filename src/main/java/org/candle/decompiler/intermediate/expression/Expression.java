package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.Sourceable;

public abstract class Expression implements Sourceable {

	protected final InstructionHandle instructionHandle;
	
	public Expression(InstructionHandle instructionHandle) {
		this.instructionHandle = instructionHandle;
	}
	
	public InstructionHandle getInstructionHandle() {
		return instructionHandle;
	}
	
	public abstract Set<Expression> nestedExpression();
	
	public Set<InstructionHandle> getAllHandles() {
		Set<InstructionHandle> handles = new HashSet<InstructionHandle>();
		handles.add(instructionHandle);
		
		for(Expression expression : nestedExpression()) {
			handles.addAll(expression.getAllHandles());
		}
		return handles;
	}
}
