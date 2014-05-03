package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.intermediate.code.loop.ContinuousWhileIntermediate;
import org.candle.decompiler.intermediate.graph.edge.EdgeType;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.Graphs;

public class ContinuousLoop extends InstructionHandleEnhancer {

	public ContinuousLoop(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void process(InstructionHandle ih) {
		List<InstructionHandle> preds = Graphs.predecessorListOf(igc.getGraph(), ih);
		
		//only more than one predecessor.
		if(preds.size() < 2) {
			return;
		}
		
		for(InstructionHandle pred : preds) {
			IntermediateEdge ie = igc.getGraph().getEdge(pred, ih);
			
			if(ie.getType() == EdgeType.BACK) {
				if(!igc.hasIntermediate(ih)) {
					//this is probably a while(true);
					ContinuousWhileIntermediate cwi = new ContinuousWhileIntermediate(ih);
					igc.addIntermediate(ih, cwi);
					break;
				}
			}
		}
		
	}

}
