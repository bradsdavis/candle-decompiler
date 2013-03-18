package org.candle.decompiler.instruction.graph.edge;

import org.jgrapht.graph.DefaultEdge;

public class InstructionEdge extends DefaultEdge {

	protected EdgeType type;
	
	public EdgeType getType() {
		return type;
	}
	
	public void setType(EdgeType type) {
		this.type = type;
	}
	
	public Object getSource() {
		return super.getSource();
	}
	
	public Object getTarget() {
		return super.getTarget();
	}
	
}
