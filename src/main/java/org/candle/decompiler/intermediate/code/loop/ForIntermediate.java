package org.candle.decompiler.intermediate.code.loop;

import java.io.IOException;
import java.io.StringWriter;

import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class ForIntermediate extends WhileIntermediate {

	private final Expression init;
	private final ConditionalIntermediate expression;
	private final Expression update;
	
	public ForIntermediate(WhileIntermediate whileIntermediate, Expression init, Expression update) {
		super(whileIntermediate.getInstruction(), whileIntermediate.getConditionalIntermediate());
		
		
		this.expression = whileIntermediate.getConditionalIntermediate();
		this.init = init;
		
		this.update = update;
	}
	
	@Override
	public ConditionalIntermediate getConditionalIntermediate() {
		// TODO Auto-generated method stub
		return super.getConditionalIntermediate();
	}
	
	public Expression getInit() {
		return init;
	}
	
	public Expression getUpdate() {
		return update;
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitForLoopLine(this);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.init.write(sw);
			sw.append("; ");
			this.expression.getExpression().write(sw);
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
