package org.candle.decompiler.instruction;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;

public class InstructionTransversalListener extends TraversalListenerAdapter<InstructionHandle, InstructionEdge> {
	
	private final IntermediateContext intermediateContext;
	private final MethodIntermediateVisitor miv;
	public InstructionTransversalListener(IntermediateContext ic) {
		this.intermediateContext = ic;
		this.miv = new MethodIntermediateVisitor(ic);
	}
	
	@Override
	public void vertexTraversed(VertexTraversalEvent<InstructionHandle> e) {
		InstructionHandle jvm = e.getVertex();
		intermediateContext.setCurrentInstruction(jvm);
		jvm.getInstruction().accept(miv);
	}
}
