package org.candle.decompiler.intermediate.code.loop;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class WhileIntermediate extends ConditionalIntermediate {
	
	private ConditionalIntermediate conditionalIntermediate;
	
	public WhileIntermediate(InstructionHandle bi, ConditionalIntermediate ci) {
		super(bi, ci.getExpression());
		this.conditionalIntermediate = ci;
		
		this.setTrueTarget(ci.getTrueTarget());
		this.setFalseTarget(ci.getFalseTarget());
	}
	
	public ConditionalIntermediate getConditionalIntermediate() {
		return conditionalIntermediate;
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
		return "While: "+sw.toString();
	}
	
	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitWhileLoopLine(this);
	}

}
