package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchOutcome;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class RetractOrphanOutcomes extends GraphIntermediateVisitor {
	
	public RetractOrphanOutcomes(IntermediateGraphContext igc) {
		super(igc);
	}

	@Override
	public void visitBooleanBranchOutcome(BooleanBranchOutcome line) {
		List<AbstractIntermediate> candidates = Graphs.predecessorListOf(igc.getGraph(), line);
		if(candidates.size() == 0) {
			igc.getGraph().removeVertex(line);
		}
	}
}
