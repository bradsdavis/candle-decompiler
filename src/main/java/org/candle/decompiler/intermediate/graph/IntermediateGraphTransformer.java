package org.candle.decompiler.intermediate.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

public class IntermediateGraphTransformer {

	private static final Log LOG = LogFactory.getLog(IntermediateGraphTransformer.class);
	
	private final InstructionGraphContext igc;
	
	public IntermediateGraphTransformer(InstructionGraphContext igc) {
		this.igc = igc;
	}
	
	public IntermediateGraphContext getIntermediateGraphContext() {
		Map<InstructionHandle, AbstractIntermediate> nodeMapping = new HashMap<InstructionHandle, AbstractIntermediate>();
		
		ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph = new ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge>(IntermediateEdge.class);
		
		//walk the instruction graph and generate the intermediate graph.  start by walking and adding all vertices.  Then, add the connections between the vertices.
		for(InstructionHandle ih : igc.getGraph().vertexSet()) {
			if(igc.hasIntermediate(ih)) {
				AbstractIntermediate vertex = igc.getIntermediateFromInstruction(ih);
				intermediateGraph.addVertex(vertex);
				nodeMapping.put(ih, vertex);
			}
		}
		
		//now, add the links.
		for(InstructionHandle ih : igc.getGraph().vertexSet()) {
			AbstractIntermediate nodeIntermediate = nodeMapping.get(ih);
			
			if(nodeIntermediate == null) {
				LOG.warn("This shouldn't be...");
				continue;
			}
			
			List<InstructionHandle> predecessors = igc.getPredecessors(ih);
			List<InstructionHandle> successors = igc.getSuccessors(ih);
			
			for(InstructionHandle predecessor : predecessors) {
				//find it's AbstractIntermediate.
				AbstractIntermediate predIntermediate = nodeMapping.get(predecessor);
				
				
				if(predIntermediate == null) {
					//then something is wrong.
					LOG.warn("This shouldn't be...");
					continue;
				}
				
				IntermediateEdge insEdge = igc.getGraph().getEdge(predecessor, ih);
				
				//add an edge to the intermediate graph.
				if(intermediateGraph.containsEdge(predIntermediate, nodeIntermediate)) {
					continue;
				}
					
				IntermediateEdge ie = intermediateGraph.addEdge(predIntermediate, nodeIntermediate);
				mapFields(insEdge, ie);
			}
			
			for(InstructionHandle successor : successors) {
				//find it's AbstractIntermediate.
				AbstractIntermediate successorIntermediate = nodeMapping.get(successor);
				
				if(successorIntermediate == null) {
					LOG.warn("This shouldn't be...");
					continue;	
				}
				if(intermediateGraph.containsEdge(nodeIntermediate, successorIntermediate)) {
					continue;
				}
				IntermediateEdge insEdge = igc.getGraph().getEdge(ih, successor);
				
				//add an edge to the intermediate graph.
				IntermediateEdge ie = intermediateGraph.addEdge(nodeIntermediate, successorIntermediate);
				mapFields(insEdge, ie);
			}
		}
		
		return new IntermediateGraphContext(intermediateGraph);
	}
	
	private void mapFields(IntermediateEdge insEdge, IntermediateEdge intEdge) {
		intEdge.setType(insEdge.getType());
		intEdge.getAttributes().putAll(insEdge.getAttributes());
	}
}
