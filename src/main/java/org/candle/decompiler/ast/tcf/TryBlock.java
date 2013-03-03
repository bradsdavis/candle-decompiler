package org.candle.decompiler.ast.tcf;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.TryIntermediate;

public class TryBlock extends SerializableBlock<TryIntermediate> {

	public TryBlock(TryIntermediate tryIntermediate) {
		super(tryIntermediate);
	}


	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("try ");
		super.write(builder);
	}
	
}
