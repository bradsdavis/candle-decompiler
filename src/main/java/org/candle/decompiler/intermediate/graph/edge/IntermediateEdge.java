package org.candle.decompiler.intermediate.graph.edge;

import java.util.HashMap;
import java.util.Map;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.jgrapht.graph.DefaultEdge;

public class IntermediateEdge extends DefaultEdge {

	protected EdgeType type;
	protected final Map<String, Object> attributes;
	
	public IntermediateEdge() {
		attributes = new HashMap<String, Object>();
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public EdgeType getType() {
		return type;
	}
	
	public void setType(EdgeType type) {
		this.type = type;
	}
	
	public AbstractIntermediate getSourceIntermediate() {
		return (AbstractIntermediate)getSource();
	}
	
	public AbstractIntermediate getTargetIntermediate() {
		return (AbstractIntermediate)getTarget();
	}
	
	public Object getSource() {
		return super.getSource();
	}
	
	public Object getTarget() {
		return super.getTarget();
	}
	
	@Override
	public Object clone() {
		return super.clone();
	}
}
