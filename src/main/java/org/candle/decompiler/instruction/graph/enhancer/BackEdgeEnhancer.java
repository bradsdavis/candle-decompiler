package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.EdgeType;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.apache.bcel.generic.InstructionHandle;
import org.jgrapht.Graphs;

public class BackEdgeEnhancer extends InstructionHandleEnhancer {

	public BackEdgeEnhancer(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void process(InstructionHandle ih) {
		List<InstructionHandle> successors = Graphs.successorListOf(igc.getGraph(), ih);
		
		for(InstructionHandle successor : successors) {
			InstructionEdge ie = igc.getGraph().getEdge(ih, successor);
			
			//color back...
			int t1 = ih.getPosition();
			int t2 = successor.getPosition();
			
			if(t2 < t1) {
				ie.setType(EdgeType.BACK);
			}
			
		}
	}
}
