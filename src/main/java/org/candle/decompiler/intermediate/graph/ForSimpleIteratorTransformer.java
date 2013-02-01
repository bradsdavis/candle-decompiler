package org.candle.decompiler.intermediate.graph;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class ForSimpleIteratorTransformer extends GraphIntermediateVisitor {

	public ForSimpleIteratorTransformer(DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph) {
		super(intermediateGraph);
	}

	@Override
	public void visitWhileLoopLine(WhileIntermediate line) {
		
	}
	
}
