package org.candle.decompiler.intermediate.graph.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.Graphs;
import org.jgrapht.graph.ListenableDirectedGraph;

public class IntermediateGraphContext {

	private final ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph;
	private final TreeSet<AbstractIntermediate> orderedIntermediate;
	private final Map<Integer, InstructionHandle> instructionHandles;
	
	public IntermediateGraphContext(ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		this.intermediateGraph = intermediateGraph;
		this.orderedIntermediate = new TreeSet<AbstractIntermediate>(new IntermediateComparator());
		this.instructionHandles = new TreeMap<Integer, InstructionHandle>();
		
		for(AbstractIntermediate ai : intermediateGraph.vertexSet()) {
			Integer position = ai.getInstruction().getPosition();
			this.instructionHandles.put(position, ai.getInstruction());
		}
		
		this.orderedIntermediate.addAll(this.intermediateGraph.vertexSet());
		this.intermediateGraph.addGraphListener(new PositionalIntermediateListener(this));
	}
	
	public TreeSet<AbstractIntermediate> getOrderedIntermediate() {
		return this.orderedIntermediate;
	}
	
	public ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> getIntermediateGraph() {
		return intermediateGraph;
	}
	
	public Map<Integer, InstructionHandle> getInstructionHandles() {
		return instructionHandles;
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
	
	public AbstractIntermediate getSingleSuccessor(AbstractIntermediate ai) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(intermediateGraph, ai);
		
		if(successors.size() == 1) {
			return successors.get(0);
		}
		
		throw new IllegalStateException("Should have exactly 1 outgoing edge; actually: "+successors.size());
	}
	
	public AbstractIntermediate getSinglePredecessor(AbstractIntermediate ai) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, ai);
		
		if(predecessors.size() == 1) {
			return predecessors.get(0);
		}
		
		throw new IllegalStateException("Should only have 1 incoming edge.");
	}
	
	public InstructionHandle getInstructionHandle(Integer position) {
		return this.instructionHandles.get(position);
	}
	
	public AbstractIntermediate findNextNode(InstructionHandle ci) {
		NullIntermediate nullIntermediate = new NullIntermediate(ci);
		return this.orderedIntermediate.ceiling(nullIntermediate);
	}
	
	public AbstractIntermediate findPreviousNode(InstructionHandle ci) {
		NullIntermediate nullIntermediate = new NullIntermediate(ci);
		return this.orderedIntermediate.floor(nullIntermediate);
	}
	
	public AbstractIntermediate getTrueTarget(BooleanBranchIntermediate ci) {
		return getSingleSuccessor(ci.getTrueBranch());
	}
	
	public AbstractIntermediate getFalseTarget(BooleanBranchIntermediate ci) {
		return getSingleSuccessor(ci.getFalseBranch());
	}
	
	public void replaceBooleanBranchIntermediate(BooleanBranchIntermediate a, BooleanBranchIntermediate b) {
		b.setFalseBranch(a.getFalseBranch());
		b.setTrueBranch(a.getTrueBranch());
	}
	
	public List<CatchIntermediate> getCatchClauses(TryIntermediate tryIntermediate) {
		//of all the successors, get the catch...
		List<AbstractIntermediate> candidates = Graphs.successorListOf(intermediateGraph, tryIntermediate);

		List<CatchIntermediate> catchClauses = new ArrayList<CatchIntermediate>();
		for(AbstractIntermediate candidate : candidates) {
			if(candidate instanceof CatchIntermediate) {
				catchClauses.add((CatchIntermediate)candidate);
			}
		}
		return catchClauses;
	}
	
	public FinallyIntermediate getFinallyClause(TryIntermediate tryIntermediate) {
		//of all the successors, get the catch...
		List<AbstractIntermediate> candidates = Graphs.successorListOf(intermediateGraph, tryIntermediate);

		for(AbstractIntermediate candidate : candidates) {
			if(candidate instanceof FinallyIntermediate) {
				return (FinallyIntermediate)candidate;
			}
		}
		return null;		
	}
	
}
