package org.candle.decompiler.instruction.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.context.PositionalInstructionListener;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.util.GraphUtil;
import org.jgrapht.Graphs;
import org.jgrapht.graph.ListenableDirectedGraph;

public class InstructionGraphContext extends GraphUtil<InstructionHandle, InstructionEdge> {

	private final Map<Integer, InstructionHandle> positionMap = new HashMap<Integer, InstructionHandle>();
	
	public InstructionGraphContext(ListenableDirectedGraph<InstructionHandle, InstructionEdge> graph) {
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
	
	@Override
	public void redirectPredecessors(InstructionHandle source, InstructionHandle target) {
		List<InstructionHandle> predecessors = Graphs.predecessorListOf(this.graph, source);
		
		//remove edges between predecessor and source.
		for(InstructionHandle p : predecessors) {
			InstructionEdge existing = graph.getEdge(source, target);
			
			//remove the edge to ci, add one to line.
			if(!graph.containsEdge(p, target)) {
				InstructionEdge newEdge = graph.addEdge(p, target);
				
				if(existing != null) {
					newEdge.setType(existing.getType());
					newEdge.getAttributes().putAll(existing.getAttributes());
				}
			}
			
			graph.removeEdge(existing);
		}
	}
}
