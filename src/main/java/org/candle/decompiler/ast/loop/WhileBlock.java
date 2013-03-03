package org.candle.decompiler.ast.loop;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;

public class WhileBlock extends SerializableBlock<WhileIntermediate> {

	public WhileBlock(WhileIntermediate whileIntermediate) {
		super(whileIntermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("while(");
		intermediate.getExpression().write(builder);
		builder.append(") ");
		super.write(builder);
	}
}
