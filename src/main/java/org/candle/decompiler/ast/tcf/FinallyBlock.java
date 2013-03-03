package org.candle.decompiler.ast.tcf;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;

public class FinallyBlock extends SerializableBlock<FinallyIntermediate> {

	public FinallyBlock(FinallyIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("finally ");
		super.write(builder);
	}
	
}
