package org.candle.decompiler.intermediate.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public abstract class GraphIntermediateVisitor extends EmptyIntermediateVisitor {

	protected final IntermediateComparator comparator = new IntermediateComparator();
	protected final DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph;
	
	
	private List<AbstractIntermediate> retractVertex = new LinkedList<AbstractIntermediate>();
	private List<AbstractIntermediate> insertVertex = new LinkedList<AbstractIntermediate>();
	
	
	public GraphIntermediateVisitor(DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		this.intermediateGraph = intermediateGraph;
		boolean conditionalUpdates = true;
		
		while(conditionalUpdates) {
			Set<AbstractIntermediate> snapshot = new HashSet<AbstractIntermediate>(intermediateGraph.vertexSet());
			for(AbstractIntermediate vertex : snapshot) {
				vertex.accept(this);
			}
			
			if(retractVertex.size() == 0 && insertVertex.size() == 0) {
				conditionalUpdates = false;
			}
			for(AbstractIntermediate kv : retractVertex) {
				intermediateGraph.removeVertex(kv);
			}
			retractVertex.clear();
		}
	}
	
	protected void retract(AbstractIntermediate retract) {
		this.retractVertex.add(retract);
	}
	
	protected void insert(AbstractIntermediate insert) {
		this.insertVertex.add(insert);
	}
	
	protected void redirectPredecessors(AbstractIntermediate source, AbstractIntermediate target) {
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
	
	protected void redirectSuccessors(AbstractIntermediate source, AbstractIntermediate target) {
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
	
	
	protected void updatePredecessorConditional(AbstractIntermediate source, AbstractIntermediate target) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, source);
		
		for(AbstractIntermediate predecessor : predecessors) {
			updatePredecessorConditional(predecessor, source, target);
		}
	}

	private void updatePredecessorConditional(AbstractIntermediate predecessor, AbstractIntermediate source, AbstractIntermediate target) {
		if(predecessor instanceof ConditionalIntermediate) {
			ConditionalIntermediate conditionalPredecessor = (ConditionalIntermediate)predecessor;
			
			if(conditionalPredecessor.getTrueTarget() == source) {
				conditionalPredecessor.setTrueTarget(target);
			}
			
			if(conditionalPredecessor.getFalseTarget() == source) {
				conditionalPredecessor.setFalseTarget(target);
			}
			
		}
	}
	
}
