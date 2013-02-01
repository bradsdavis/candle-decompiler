package org.candle.decompiler.intermediate.code;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.blockinterpreter.Visitor;
import org.candle.decompiler.intermediate.InstructionHandleReference;
import org.candle.decompiler.intermediate.expression.Expression;

public abstract class AbstractIntermediate implements InstructionHandleReference, Comparable<AbstractIntermediate> {

	private final InstructionHandle instruction;
	
	public AbstractIntermediate(InstructionHandle instruction) {
		this.instruction = instruction;
	}
	
	public InstructionHandle getInstruction() {
		return instruction;
	}
	
	public abstract void accept(Visitor visitor);
	

	public abstract Set<Expression> nestedExpression();
	
	public Set<InstructionHandle> getAllHandles() {
		Set<InstructionHandle> handles = new HashSet<InstructionHandle>();
		handles.add(instruction);
		
		//needs to recurse.
		for(Expression expression : nestedExpression()) {
			handles.addAll(expression.getAllHandles());
		}
		
		return handles;
	}
	
	@Override
	public int compareTo(AbstractIntermediate o) {
		return this.getInstruction().getPosition() - o.getInstruction().getPosition();
	}
}
