package org.candle.decompiler.instruction.graph.enhancer;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.intermediate.InstructionTransversalListener;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

/**
 * Walks the instruction graph and populates intermediate instructions.
 * 
 * @author bradsdavis
 *
 */
public class InstructionToIntermediateEnhancer extends InstructionGraphEnhancer {

	private final IntermediateContext intermediateContext;
	
	public InstructionToIntermediateEnhancer(InstructionGraphContext igc, IntermediateContext intermediateContext) {
		super(igc);
		this.intermediateContext = intermediateContext;
	}

	@Override
	public void process() {
		GraphIterator<InstructionHandle, IntermediateEdge> iterator = new DepthFirstIterator<InstructionHandle, IntermediateEdge>(igc.getGraph());
		iterator.addTraversalListener(new InstructionTransversalListener(igc, intermediateContext));
		
		while (iterator.hasNext()) {
			iterator.next();
		}
		
	}
}
