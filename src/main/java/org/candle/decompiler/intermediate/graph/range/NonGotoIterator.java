package org.candle.decompiler.intermediate.graph.range;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class NonGotoIterator extends BreadthFirstIterator<AbstractIntermediate, IntermediateEdge> {

	public NonGotoIterator(Graph<AbstractIntermediate, IntermediateEdge> g, AbstractIntermediate startVertex) {
		super(g, startVertex);
	}

	@Override
	protected void encounterVertex(AbstractIntermediate vertex, IntermediateEdge edge) {
		if(vertex instanceof GoToIntermediate) {
			//don't add it.
		}
		else {
			super.encounterVertex(vertex, edge);
		}
	}
	
	
	

}
