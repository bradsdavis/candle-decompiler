package org.candle.decompiler.ast;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.intermediate.code.SwitchIntermediate;

public class SwitchBlock extends SerializableBlock<SwitchIntermediate> {

	public SwitchBlock(SwitchIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		intermediate.getExpression().write(builder);
		builder.append(" ");
		super.write(builder);
	}
	
}
