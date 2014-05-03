package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.HashSet;
import java.util.Set;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

/**
 * In the case that CASE 1 does not contain a break to the instruction
 * that succeeds CASE 2, remove the edge for serialization.
 * 
 * @author bradsdavis@gmail.com
 *
 */
public class RemoveCaseToCaseEdge extends GraphIntermediateVisitor {

	public RemoveCaseToCaseEdge(IntermediateGraphContext igc) {
		super(igc);
	}

	@Override
	public void visitCaseIntermediate(CaseIntermediate line) {
		//find successor of Case...
		AbstractIntermediate ai = igc.getSingleSuccessor(line);
		
		//find the previous node...
		Set<AbstractIntermediate> predecessors = new HashSet<AbstractIntermediate>(Graphs.predecessorListOf(igc.getGraph(), ai));
		predecessors.remove(line);
		
		//remove line.
		for(AbstractIntermediate predecessor : predecessors) {
			//remove line...
			igc.getGraph().removeEdge(predecessor, ai);
		}
	}
}
