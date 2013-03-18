package org.candle.decompiler.instruction;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;

public class InstructionTransversalListener extends TraversalListenerAdapter<InstructionHandle, InstructionEdge> {
	
	private final StackClonePointListener cpl;
	private final InstructionGraphContext igc;
	private final IntermediateContext intermediateContext;
	private final MethodIntermediateVisitor miv;
	public InstructionTransversalListener(InstructionGraphContext igc, IntermediateContext ic) {
		this.cpl = new StackClonePointListener(igc, ic);
		this.igc = igc;
		this.intermediateContext = ic;
		this.miv = new MethodIntermediateVisitor(ic);
	}
	
	
	
	@Override
	public void vertexTraversed(VertexTraversalEvent<InstructionHandle> e) {
		//setup.
		cpl.setup(e);
		InstructionHandle jvm = e.getVertex();
		intermediateContext.setCurrentInstruction(jvm);
		jvm.getInstruction().accept(miv);

		//finish.
		cpl.finish(e);
		
		super.vertexFinished(e);
	}
}
