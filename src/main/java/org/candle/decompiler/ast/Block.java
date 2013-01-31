package org.candle.decompiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.Sourceable;

public abstract class Block implements Sourceable {
	private static final Log LOG = LogFactory.getLog(Block.class);
	
	protected String indent = null;
	
	protected static final String TAB = "\t";
	protected static final String NL = "\n";
	
	protected Block parent;
	protected List<Block> children = new ArrayList<Block>();
	
	public Block getParent() {
		return parent;
	}
	
	public List<Block> getChildren() {
		return children;
	}
	
	public void addChild(Block block) {
		block.setParent(this);
		this.getChildren().add(block);
	}
	
	public void replaceChild(Block candidate, Block replacement) {
		int blockPosition = children.indexOf(candidate);
		
		children.remove(blockPosition);
		children.add(blockPosition, replacement);
	}
	
	
	public void setParent(Block parent) {
		this.parent = parent;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		
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

	public abstract InstructionHandle getInstruction();
	
	//return last child instruction
	public abstract int getEndBlockPosition();
	public abstract int getStartBlockPosition();
	
	public boolean within(InstructionHandle ih) {
		int position = ih.getPosition();

		if(position < getStartBlockPosition() || position > getEndBlockPosition()) {
			return false;
		}
		
		return true;
	}
	
	//helper methods.
	protected String buildIndent() {
		if(this.indent != null) {
			return indent;
		}
		int count = countPathToRoot();
		
		//now, build.
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<count; i++) {
			builder.append(TAB);
		}
		this.indent = builder.toString();
		return indent;
	}
	
	public int countPathToRoot() {
		//count path to root..
		int parent = this.getParent().countPathToRoot();
		return parent + 1;
	}
}
