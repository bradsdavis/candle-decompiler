package org.candle.decompiler.util;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.ListenableDirectedGraph;

public class GraphUtil<T, E> {

	protected final ListenableDirectedGraph<T, E> graph;
	
	public GraphUtil(ListenableDirectedGraph<T, E> graph) {
		this.graph = graph;
	}
	
	public ListenableDirectedGraph<T, E> getGraph() {
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
		List<T> predecessors = Graphs.predecessorListOf(graph, source);
		
		//remove edges between predecessor and source.
		for(T p : predecessors) {
			//remove the edge to ci, add one to line.
			if(!graph.containsEdge(p, target)) {
				graph.addEdge(p, target);
			}
			graph.removeEdge(p, source);
		}
	}
	
	public void redirectSuccessors(T source, T target) {
		List<T> candidate = Graphs.successorListOf(graph, source);

		//remove edges between successor and source.
		for(T s : candidate) {
			
			//remove the edge to ci, add one to line.
			if(!graph.containsEdge(target, s)) {
				graph.addEdge(target, s);
			}
			graph.removeEdge(source, s);
		}
	}
}
