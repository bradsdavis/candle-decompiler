package org.candle.decompiler.intermediate.code;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.blockinterpreter.Visitor;
import org.candle.decompiler.intermediate.expression.Expression;

public class GoToIntermediate extends AbstractIntermediate {

	private AbstractIntermediate target;
	
	public GoToIntermediate(InstructionHandle instruction) {
		super(instruction);
	}
	
	public AbstractIntermediate getTarget() {
		return target;
	}
	
	public void setTarget(AbstractIntermediate target) {
		this.target = target;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitGoToLine(this);
	}

	@Override
	public Set<Expression> nestedExpression() {
		return new HashSet<Expression>(1);
	}
	
	@Override
	public String toString() {
		String t = null;
		if(this.target == null) {
			t = "Unresolved Position: "+((BranchHandle)getInstruction()).getTarget().getPosition();
		}
		else {
			t = target.toString();
		}
		
		return "Goto ["+this.getInstruction().getPosition() + " -> " + t+"]";
	}
}
