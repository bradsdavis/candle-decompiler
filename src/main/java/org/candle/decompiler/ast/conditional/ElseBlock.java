package org.candle.decompiler.ast.conditional;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.conditional.ElseIntermediate;

public class ElseBlock extends SerializableBlock<ElseIntermediate> {

	public ElseBlock(ElseIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("else ");
		super.write(builder);
	}
}
