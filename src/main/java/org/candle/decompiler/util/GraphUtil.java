package org.candle.decompiler.util;

import java.util.List;

import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

public class GraphUtil<T> {

	protected final ListenableDirectedGraph<T, IntermediateEdge> graph;
	
	public GraphUtil(ListenableDirectedGraph<T, IntermediateEdge> graph) {
		this.graph = graph;
	}
	
	public ListenableDirectedGraph<T, IntermediateEdge> getGraph() {
		return graph;
	}
	
	public T getSingleSuccessor(T ai) {
		List<T> successors = Graphs.successorListOf(graph, ai);
		
		if(successors.size() == 1) {
			return successors.get(0);
		}
		
		throw new IllegalStateException("Should have exactly 1 outgoing edge; actually: "+successors.size());
	}
	
	public T getSinglePredecessor(T ai) {
		List<T> predecessors = Graphs.predecessorListOf(graph, ai);
		
		if(predecessors.size() == 1) {
			return predecessors.get(0);
		}
		
		throw new IllegalStateException("Should only have 1 incoming edge.");
	}

	public void redirectPredecessors(T source, T target) {
		List<T> predecessors = Graphs.predecessorListOf(this.graph, source);
		
		//remove edges between predecessor and source.
		for(T p : predecessors) {
			IntermediateEdge existing = graph.getEdge(p, source);
			
			//remove the edge to ci, add one to line.
			if(!graph.containsEdge(p, target)) {
				graph.addEdge(p, target, (IntermediateEdge)existing.clone());
			}
			
			graph.removeEdge(existing);
		}
	}
	
	public void redirectSuccessors(T source, T target) {
		List<T> successors = Graphs.successorListOf(this.graph, source);
		
		//remove edges between predecessor and source.
		for(T s : successors) {
			IntermediateEdge existing = graph.getEdge(source, s);
			
			//remove the edge to ci, add one to line.
			if(!graph.containsEdge(s, target)) {
				graph.addEdge(target, s, (IntermediateEdge)existing.clone());
			}
			
			graph.removeEdge(existing);
		}
	}
	public List<T> getSuccessors(T iv) {
		return Graphs.successorListOf(this.graph, iv);
	}
	
	public List<T> getPredecessors(T iv) {
		return Graphs.predecessorListOf(this.graph, iv);
	}
	

}
