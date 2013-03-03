package org.candle.decompiler.ast;

import java.io.IOException;
import java.io.Writer;

public class MethodBlock extends Block {

	private final int lastPosition;
	private final String methodSignature;
	
	public MethodBlock(String methodSignature, int lastPosition) {
		this.methodSignature = methodSignature;
		this.lastPosition = lastPosition;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		String indent = buildIndent();
		builder.append(indent);
		builder.append(methodSignature);
		builder.append(" ");
		builder.append("{");
		
		for(Block child : children) {
			builder.append(NL);
			child.write(builder);
		}
		builder.append(NL);
		builder.append(indent);
		builder.append("}");
		builder.append(NL);
	}
	
	@Override
	public int getStartBlockPosition() {
		return 0;
	}
	
	@Override
	public int getEndBlockPosition() {
		return lastPosition;
	}
}
