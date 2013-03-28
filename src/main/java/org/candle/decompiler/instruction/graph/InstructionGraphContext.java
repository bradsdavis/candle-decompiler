package org.candle.decompiler.instruction.graph;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.context.PositionalInstructionListener;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.util.GraphUtil;
import org.jgrapht.graph.ListenableDirectedGraph;

public class InstructionGraphContext extends GraphUtil<InstructionHandle> {

	private final Map<Integer, InstructionHandle> positionMap = new HashMap<Integer, InstructionHandle>();
	
	public InstructionGraphContext(ListenableDirectedGraph<InstructionHandle, IntermediateEdge> graph) {
		super(graph);
		this.graph.addGraphListener(new PositionalInstructionListener(positionMap));
	}

	public Map<Integer, InstructionHandle> getPositionMap() {
		return positionMap;
	}
	
	public AbstractIntermediate getIntermediateFromInstruction(InstructionHandle ih) {
		return (AbstractIntermediate)ih.getAttribute(IntermediateContext.INTERMEDIATE_KEY);
	}

	public void addIntermediate(InstructionHandle ih, AbstractIntermediate ai) {
		ih.addAttribute(IntermediateContext.INTERMEDIATE_KEY, ai);
	}
	
	public boolean hasIntermediate(InstructionHandle ih) {
		return (ih.getAttribute(IntermediateContext.INTERMEDIATE_KEY) != null);
	}
}
