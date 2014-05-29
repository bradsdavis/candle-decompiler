package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.HashSet;
import java.util.Set;

import org.candle.decompiler.instruction.graph.edge.EdgeType;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;

/** 
 * Go through all intermediates, and see if the source is an exception edge...
 * TODO: Enhance the visitor to visit edge types...
 */
public class Try extends GraphIntermediateVisitor {

	public Try(IntermediateGraphContext igc) {
		super(igc);
	}

	@Override
	public void visitAbstractIntermediate(AbstractIntermediate line) {
		
		//such poor performance...
		final Set<IntermediateEdge> edges = new HashSet<IntermediateEdge>(igc.getIncomingEdges(line));
		
		for(IntermediateEdge ie : edges) {
			if(ie.getType() == EdgeType.EXCEPTION) {
				System.out.println("EXCEEEEEPTIONS!");
				
				
				TryIntermediate ti = new TryIntermediate(line.getInstruction());
				igc.getGraph().addVertex(ti);
				igc.redirectPredecessors(line, ti);
			}
		}
	}
}
