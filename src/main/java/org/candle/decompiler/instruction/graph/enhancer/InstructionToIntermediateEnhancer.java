package org.candle.decompiler.instruction.graph.enhancer;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.InstructionTransversalListener;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.candle.decompiler.intermediate.IntermediateContext;
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
		GraphIterator<InstructionHandle, InstructionEdge> iterator = new DepthFirstIterator<InstructionHandle, InstructionEdge>(igc.getGraph());
		iterator.addTraversalListener(new InstructionTransversalListener(igc, intermediateContext));
		
		while (iterator.hasNext()) {
			iterator.next();
		}
		
	}
}
