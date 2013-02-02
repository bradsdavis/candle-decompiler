package org.candle.decompiler.intermediate.code.loop;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class ForIntermediate extends AbstractIntermediate {

	private final Expression init;
	private final ConditionalExpression expression;
	private final Expression update;
	
	public ForIntermediate(InstructionHandle instruction, Expression init, ConditionalExpression expression, Expression update) {
		super(instruction);
		
		this.init = init;
		this.expression = expression;
		this.update = update;
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitForLoopLine(this);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.init.write(sw);
			sw.append("; ");
			this.expression.write(sw);
			sw.append("; ");
			if(update != null) {
				this.update.write(sw);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "For: "+sw.toString();
	}
}
