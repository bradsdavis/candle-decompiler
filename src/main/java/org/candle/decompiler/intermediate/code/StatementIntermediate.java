package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class StatementIntermediate extends AbstractIntermediate implements BlockSerializable {

	private final BlockRange blockRange;
	private Expression expression;
	
	public StatementIntermediate(InstructionHandle instruction, Expression expression) {
		super(instruction);
		this.expression = expression;
		
		
		BlockRange blockRange = new BlockRange();
		blockRange.setStart(instruction);
		blockRange.setEnd(instruction);
		this.blockRange = blockRange;
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		
		try {
			expression.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sw.toString() + ";";
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitCompleteLine(this); 
	}

	@Override
	public BlockRange getBlockRange() {
		return blockRange;
	}
}
