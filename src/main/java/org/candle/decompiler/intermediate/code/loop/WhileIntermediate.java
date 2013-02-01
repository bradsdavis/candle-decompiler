package org.candle.decompiler.intermediate.code.loop;

import java.io.IOException;
import java.io.StringWriter;

import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class WhileIntermediate extends ConditionalIntermediate {

	public WhileIntermediate(GoToIntermediate g, ConditionalIntermediate ci) {
		super(g.getInstruction(), ci.getExpression());
		this.setTrueTarget(ci.getTrueTarget());
		this.setFalseTarget(ci.getFalseTarget());
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
		visitor.visitWhileLoopLine(this);
	}

}
