package org.candle.decompiler.ast;

import org.apache.bcel.generic.InstructionHandle;

public class MethodBlock extends Block {

	private final int lastPosition;
	private final String methodSignature;
	
	public MethodBlock(String methodSignature, int lastPosition) {
		this.methodSignature = methodSignature;
		this.lastPosition = lastPosition;
	}
	
	@Override
	public String generateSource() {
		String indent = buildIndent();
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		builder.append(methodSignature);
		builder.append(" ");
		builder.append("{");
		
		for(Block child : children) {
			builder.append(NL);
			builder.append(child.generateSource());
		}
		builder.append(NL);
		builder.append(indent);
		builder.append("}");
		builder.append(NL);
		
		return builder.toString();
	}

	@Override
	public InstructionHandle getInstruction() {
		return null;
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
