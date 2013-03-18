package org.candle.decompiler.instruction.graph.enhancer;

import org.apache.bcel.generic.GOTO;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.apache.bcel.generic.InstructionHandle;

public class HealGotoEnhancer extends InstructionGraphEnhancer {

	public HealGotoEnhancer(InstructionGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitGOTO(GOTO obj) {
		
		InstructionHandle source = igc.getSinglePredecessor(this.current);
		InstructionHandle target = igc.getSingleSuccessor(this.current);
		
		igc.getGraph().removeVertex(this.current);
		igc.getGraph().addEdge(source, target);
	}

}
