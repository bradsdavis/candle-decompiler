package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class ConditionExternalToWhileLoop extends GraphIntermediateVisitor {
	
	public ConditionExternalToWhileLoop(IntermediateGraphContext igc) {
		super(igc, true);
	}

	protected GoToIntermediate getCandidateGoto(TreeSet<GoToIntermediate> incomingGotoNonNested, TreeSet<GoToIntermediate> incomingGotoNested) {
		return incomingGotoNested.pollLast();
	}
}
