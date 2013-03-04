package org.candle.decompiler.ast.swtch;

import java.io.IOException;
import java.io.Writer;

import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.intermediate.code.CaseIntermediate;

public class SwitchCaseBlock extends SerializableBlock<CaseIntermediate> {

	public SwitchCaseBlock(CaseIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		intermediate.getCaseEntry().write(builder);
		
		for(Block child : children) {
			builder.append(NL);
			child.write(builder);
		}
		
	}
}
