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
	public void visitGoToLine(GoToIntermediate line) {
		List<AbstractIntermediate> candidates = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		if(candidates.size() == 0) {
			igc.getIntermediateGraph().removeVertex(line);
		}
	}
}
