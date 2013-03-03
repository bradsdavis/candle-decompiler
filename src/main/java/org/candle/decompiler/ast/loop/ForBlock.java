package org.candle.decompiler.ast.loop;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;

public class ForBlock extends SerializableBlock<ForIntermediate> {

	public ForBlock(ForIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		
		builder.append(indent);
		builder.append("for(");
		intermediate.getInit().write(builder);
		builder.append("; ");
		intermediate.getExpression().write(builder);
		builder.append("; ");
		if(intermediate.getUpdate() != null) {
			intermediate.getUpdate().write(builder);
		}
		builder.append(") ");
		super.write(builder);
	}
}
