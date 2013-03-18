package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class RetractOrphanGoto extends GraphIntermediateVisitor {

	public RetractOrphanGoto(IntermediateGraphContext igc) {
		super(igc);
	}

	@Override
	public void visitGoToIntermediate(GoToIntermediate line) {
		List<AbstractIntermediate> candidates = Graphs.predecessorListOf(igc.getGraph(), line);
		if(candidates.size() == 0) {
			igc.getGraph().removeVertex(line);
		}
	}
}
