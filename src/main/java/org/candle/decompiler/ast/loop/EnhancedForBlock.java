package org.candle.decompiler.ast.loop;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;

public class EnhancedForBlock extends SerializableBlock<EnhancedForIntermediate> {

	public EnhancedForBlock(EnhancedForIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("for(");
		intermediate.getExpression().write(builder);
		builder.append(") ");
		super.write(builder);
	}
}
