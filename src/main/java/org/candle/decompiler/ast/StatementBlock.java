package org.candle.decompiler.ast;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Expression;

public class StatementBlock extends SerializableBlock<StatementIntermediate> {

	public StatementBlock(StatementIntermediate statement) {
		super(statement);
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		Expression expression = intermediate.getExpression();
		
		builder.write(indent);
		expression.write(builder);
		builder.write(";");
	}
	
	@Override
	public void addChild(Block block) {
		throw new IllegalStateException("Not able to nest statements.");
	}
	
}
