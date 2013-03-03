package org.candle.decompiler.ast.conditional;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;

public class IfBlock extends SerializableBlock<IfIntermediate> {

	public IfBlock(IfIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("if(");
		intermediate.getExpression().write(builder);
		builder.append(") ");
		super.write(builder);
	}
}
