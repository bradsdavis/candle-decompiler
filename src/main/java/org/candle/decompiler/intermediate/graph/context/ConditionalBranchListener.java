package org.candle.decompiler.intermediate.graph.context;

import java.util.TreeSet;

import org.apache.bcel.generic.BranchHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.Graphs;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

public class ConditionalBranchListener implements GraphListener<AbstractIntermediate, IntermediateEdge> {

	private final IntermediateGraphContext igc;

	public ConditionalBranchListener(IntermediateGraphContext igc) {
		this.igc = igc;
	}
	
	@Override
	public void vertexAdded(GraphVertexChangeEvent<AbstractIntermediate> e) {

	}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<AbstractIntermediate> e) {

	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<AbstractIntermediate, IntermediateEdge> e) {
		IntermediateEdge ie = e.getEdge();
		Object source = ie.getSource();
		
		//check if source is conditional...
		if(source instanceof ConditionalIntermediate) {
			updateTargets((ConditionalIntermediate)source);
		}
	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<AbstractIntermediate, IntermediateEdge> e) {
		IntermediateEdge ie = e.getEdge();
		Object source = ie.getSource();
		
		//check if source is conditional...
		if(source instanceof ConditionalIntermediate) {
			updateTargets((ConditionalIntermediate)source);
		}
		
	}


	protected void updateTargets(ConditionalIntermediate ci) {
		/*
		TreeSet<AbstractIntermediate> ordered = new TreeSet<AbstractIntermediate>(new IntermediateComparator());
		ordered.addAll(Graphs.successorListOf(igc.getIntermediateGraph(), ci));
		
		boolean swapped = false;
		BranchHandle bh = (BranchHandle)ci.getInstruction();
		if(bh.getTarget().getPosition() > bh.getNext().getPosition()) {
			swapped = true;
		}
		
		
		if(ordered.size() != 2) {
			return;
		}
		
		System.out.println("Updating targets for: "+ci);
		
		if(!swapped) {
			ci.setTrueTarget(ordered.pollFirst());
			ci.setFalseTarget(ordered.pollFirst());
		}
		else {
			ci.setFalseTarget(ordered.pollFirst());
			ci.setTrueTarget(ordered.pollFirst());
		}
		System.out.println("\t True: "+ci.getTrueTarget());
		System.out.println("\t False: "+ci.getFalseTarget());
		*/
	}


	
}
