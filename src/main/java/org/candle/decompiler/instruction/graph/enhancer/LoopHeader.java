package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.apache.bcel.generic.DuplicateHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.intermediate.graph.edge.EdgeType;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.Graphs;

public class LoopHeader extends InstructionHandleEnhancer {
	
	private static final Log LOG = LogFactory.getLog(LoopHeader.class);
	
	public LoopHeader(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void process(InstructionHandle ih) {
		//check to see whether a predecessor is a back edge.
		List<InstructionHandle> preds = Graphs.predecessorListOf(igc.getGraph(), ih);
		
		//only more than one predecessor.
		if(preds.size() < 2) {
			return;
		}
		
		for(InstructionHandle pred : preds) {
			IntermediateEdge ie = igc.getGraph().getEdge(pred, ih);
			
			if(ie.getType() == EdgeType.BACK) {
				LOG.debug("Has back edge.");
				splitLoopHeader(ih);
				break;
			}
		}
	}
	
	public void splitLoopHeader(InstructionHandle ih) {
		DuplicateHandle duplicate = new DuplicateHandle(ih);
		System.out.println(igc.getGraph().addVertex(duplicate));
		System.out.println(duplicate);
		
		
		List<InstructionHandle> preds = Graphs.predecessorListOf(igc.getGraph(), ih);
		for(InstructionHandle pred : preds) {
			IntermediateEdge ie = igc.getGraph().getEdge(pred, ih);
			
			IntermediateEdge in = igc.getGraph().addEdge(pred, duplicate);
			in.setType(ie.getType());
			
			//now remove the original edge.
			igc.getGraph().removeEdge(ie);
		}

		//add edge from duplicate to new.
		igc.getGraph().addEdge(duplicate, ih);
	}

}
