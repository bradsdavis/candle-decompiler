package org.candle.decompiler.ast.conditional;

import org.candle.decompiler.intermediate.code.ConditionalIntermediate;

public class ElseIfBlock extends IfBlock {

	public ElseIfBlock(ConditionalIntermediate conditional) {
		super(conditional);
	}

	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder("else ");
		builder.append(super.generateSource());
		return builder.toString();
	}
}
