package org.candle.decompiler.intermediate.graph.enhancer;

import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class MultiConditionalToSwitchIntermediate extends GraphIntermediateVisitor {

	public MultiConditionalToSwitchIntermediate(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitMultiBranchIntermediate(MultiBranchIntermediate line) {
		SwitchIntermediate si = new SwitchIntermediate(line.getInstruction(), line);
		igc.getIntermediateGraph().addVertex(si);
		igc.redirectPredecessors(line, si);
		igc.redirectSuccessors(line, si);
		igc.getIntermediateGraph().removeVertex(line);
	}

}
