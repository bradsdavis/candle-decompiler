package org.candle.decompiler.intermediate.graph.edge;

public class BooleanConditionEdge extends IntermediateEdge {

	private boolean condition;
	
	public boolean isCondition() {
		return condition;
	}
	
	public void setCondition(boolean condition) {
		this.condition = condition;
	}
}
