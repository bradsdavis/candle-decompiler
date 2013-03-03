package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.expression.Switch;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class MultiBranchIntermediate extends AbstractIntermediate {

	private final Switch expression;
	private Case defaultCase;
	private Set<Case> cases;
	
	public MultiBranchIntermediate(InstructionHandle instruction, Switch expression) {
		super(instruction);
		this.expression = expression;
	}
	
	public void setCases(Set<Case> cases) {
		this.cases = cases;
	}
	
	public void setDefaultCase(Case defaultCase) {
		this.defaultCase = defaultCase;
	}
	
	public Case getDefaultCase() {
		return defaultCase;
	}
	
	public Set<Case> getCases() {
		return cases;
	}
	
	public Switch getExpression() {
		return expression;
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.expression.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "TestSwitch: "+sw.toString();
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitMultiBranchIntermediate(this);
	}
}
