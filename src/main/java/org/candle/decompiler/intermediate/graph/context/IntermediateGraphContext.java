package org.candle.decompiler.intermediate.graph.context;

import java.util.List;
import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.Graphs;
import org.jgrapht.graph.ListenableDirectedGraph;

public class IntermediateGraphContext {

	private final ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph;
	private final TreeSet<AbstractIntermediate> orderedIntermediate;
	
	public IntermediateGraphContext(ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		this.intermediateGraph = intermediateGraph;
		this.intermediateGraph.addGraphListener(new ConditionalBranchListener(this));
		this.orderedIntermediate = new TreeSet<AbstractIntermediate>(new IntermediateComparator());
		
		this.orderedIntermediate.addAll(this.intermediateGraph.vertexSet());
		this.intermediateGraph.addGraphListener(new PositionalIntermediateListener(this));
	}
	
	public TreeSet<AbstractIntermediate> getOrderedIntermediate() {
		return this.orderedIntermediate;
	}
	
	public ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> getIntermediateGraph() {
		return intermediateGraph;
	}
	
	public void redirectPredecessors(AbstractIntermediate source, AbstractIntermediate target) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, source);
		
		//remove edges between predecessor and source.
		for(AbstractIntermediate p : predecessors) {
			//remove the edge to ci, add one to line.
			if(!intermediateGraph.containsEdge(p, target)) {
				intermediateGraph.addEdge(p, target);
			}
			intermediateGraph.removeEdge(p, source);
		}
	}
	
	public void redirectSuccessors(AbstractIntermediate source, AbstractIntermediate target) {
		List<AbstractIntermediate> candidate = Graphs.successorListOf(intermediateGraph, source);

		//remove edges between successor and source.
		for(AbstractIntermediate s : candidate) {
			//remove the edge to ci, add one to line.
			if(!intermediateGraph.containsEdge(target, s)) {
				intermediateGraph.addEdge(target, s);
			}
			intermediateGraph.removeEdge(source, s);
		}
	}
	
}
