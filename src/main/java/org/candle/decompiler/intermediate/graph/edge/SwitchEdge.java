package org.candle.decompiler.intermediate.graph.edge;

import org.candle.decompiler.intermediate.expression.Case;

public class SwitchEdge extends IntermediateEdge {

	protected Case switchCase;
	
	public SwitchEdge(Case switchCase) {
		this.switchCase = switchCase;
	}
	
	public Case getSwitchCase() {
		return switchCase;
	}
}
