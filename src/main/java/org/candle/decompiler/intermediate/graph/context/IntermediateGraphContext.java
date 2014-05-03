package org.candle.decompiler.intermediate.graph.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.expression.DefaultCase;
import org.candle.decompiler.intermediate.graph.edge.BooleanConditionEdge;
import org.candle.decompiler.intermediate.graph.edge.EdgeType;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.edge.SwitchEdge;
import org.candle.decompiler.util.GraphUtil;
import org.jgrapht.Graphs;
import org.jgrapht.graph.ListenableDirectedGraph;

public class IntermediateGraphContext extends GraphUtil<AbstractIntermediate> {

	private final TreeSet<AbstractIntermediate> orderedIntermediate;
	private final Map<Integer, InstructionHandle> instructionHandles;
	
	public IntermediateGraphContext(ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> graph) {
		super(graph);
		this.orderedIntermediate = new TreeSet<AbstractIntermediate>(new IntermediateComparator());
		this.instructionHandles = new TreeMap<Integer, InstructionHandle>();
		
		for(AbstractIntermediate ai : graph.vertexSet()) {
			Integer position = ai.getInstruction().getPosition();
			this.instructionHandles.put(position, ai.getInstruction());
		}
		
		this.orderedIntermediate.addAll(this.graph.vertexSet());
		this.graph.addGraphListener(new PositionalIntermediateListener(this));
	}
	
	public TreeSet<AbstractIntermediate> getOrderedIntermediate() {
		return this.orderedIntermediate;
	}
	
	public Map<Integer, InstructionHandle> getInstructionHandles() {
		return instructionHandles;
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
		//find true edge...
		List<AbstractIntermediate> successors = getSuccessors(ci);
		
		for(AbstractIntermediate successor : successors) {
			IntermediateEdge ie  = graph.getEdge(ci, successor);
			
			if(ie instanceof BooleanConditionEdge) {
				if(((BooleanConditionEdge) ie).isCondition()) {
					return successor;
				}
			}
		}
		
		return null;
	}
	
	public AbstractIntermediate getTarget(GoToIntermediate gt) {
		return getSingleSuccessor(gt);
	}
	
	public AbstractIntermediate getFalseTarget(BooleanBranchIntermediate ci) {
		//find true edge...
				List<AbstractIntermediate> successors = getSuccessors(ci);
				
				for(AbstractIntermediate successor : successors) {
					IntermediateEdge ie  = graph.getEdge(ci, successor);
					
					if(ie instanceof BooleanConditionEdge) {
						//false condition
						if(!((BooleanConditionEdge) ie).isCondition()) {
							return successor;
						}
					}
				}
				
				return null;
	}
	
	public List<CatchIntermediate> getCatchClauses(TryIntermediate tryIntermediate) {
		//of all the successors, get the catch...
		List<AbstractIntermediate> candidates = Graphs.successorListOf(graph, tryIntermediate);

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
		List<AbstractIntermediate> candidates = Graphs.successorListOf(graph, tryIntermediate);

		for(AbstractIntermediate candidate : candidates) {
			if(candidate instanceof FinallyIntermediate) {
				return (FinallyIntermediate)candidate;
			}
		}
		return null;		
	}
	
	public void validateBackEdge(IntermediateEdge ie) {
		if(ie.getType() == EdgeType.EXCEPTION) {
			return;
		}
		
		int source = ie.getSourceIntermediate().getInstruction().getPosition();
		int target = ie.getTargetIntermediate().getInstruction().getPosition();
		
		if(target < source) {
			ie.setType(EdgeType.BACK);
		}
		else {
			ie.setType(EdgeType.NORMAL);
		}
	}

	public SwitchEdge getDefaultCase(SwitchIntermediate si) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(graph, si);
 		
		for(AbstractIntermediate successor : successors) {
			IntermediateEdge ie = this.getGraph().getEdge(si, successor);
			
			if(ie instanceof SwitchEdge) {
				if(((SwitchEdge) ie).getSwitchCase() instanceof DefaultCase) {
					return (SwitchEdge)ie;
				}
			}
		}
		
		return null;
	}
	
	public Set<SwitchEdge> getCases(SwitchIntermediate si) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(graph, si);

		Set<SwitchEdge> edges = new HashSet<SwitchEdge>();
 		for(AbstractIntermediate successor : successors) {
			IntermediateEdge ie = this.getGraph().getEdge(si, successor);
			
			if(ie instanceof SwitchEdge) {
				edges.add((SwitchEdge)ie);
			}
		}
 		
 		return edges;
	}
	
	public List<CaseIntermediate> getCaseIntermediates(SwitchIntermediate si) {
		Set<SwitchEdge> ses = getCases(si);
		
		List<CaseIntermediate> switchCases = new ArrayList<CaseIntermediate>();
		for(SwitchEdge se : ses) {
			if(se.getTarget() instanceof CaseIntermediate) {
				switchCases.add((CaseIntermediate)se.getTargetIntermediate());
			}
			else {
				throw new RuntimeException("SwitchIntermediate should point to Cases.");
			}
		}

		return switchCases;
	}
	
	
	
	public Set<AbstractIntermediate> getNodesWithinRange(BlockRange blockRange) {
		AbstractIntermediate start = findNextNode(blockRange.getStart());
		AbstractIntermediate end = findPreviousNode(blockRange.getEnd());
		
		return orderedIntermediate.subSet(start, false, end, false);
	}
	
	
}
