package org.candle.decompiler.instruction.graph.enhancer;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Select;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.expression.DefaultCase;
import org.candle.decompiler.intermediate.expression.Resolved;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.edge.SwitchEdge;

public class SwitchEdgeEnhancer extends InstructionHandleEnhancer {

	public SwitchEdgeEnhancer(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void process(InstructionHandle ih) {
		//ok, now we need to replace existing successor edges appropriately.
		
		if(!(ih.getInstruction() instanceof Select)) {
			return;
		}
		Select select = (Select)ih.getInstruction();

		for(int i=0, j=select.getTargets().length; i<j; i++) {
			InstructionHandle target = select.getTargets()[i];
			int match = select.getMatchs()[i];
			
			IntermediateEdge path = igc.getGraph().getEdge(ih, target);
			SwitchEdge sce = createConditionalEdge(path, match);
			igc.getGraph().removeEdge(path);
			igc.getGraph().addEdge(ih, target, sce);
		}
		
		
		//now the default case...
		
		if(select.getTarget() != null) {
			InstructionHandle target = select.getTarget();
			IntermediateEdge path = igc.getGraph().getEdge(ih, target);
			
			SwitchEdge sde = createDefaultEdge(path);
			igc.getGraph().removeEdge(path);
			igc.getGraph().addEdge(ih, target, sde);
		}
		
	}

	public SwitchEdge createDefaultEdge(IntermediateEdge ie) {
		DefaultCase defaultCase = new DefaultCase((InstructionHandle)ie.getSource());
		
		SwitchEdge ce = new SwitchEdge(defaultCase);
		ce.setType(ie.getType());
		ce.getAttributes().putAll(ie.getAttributes());
		
		return ce;
	}
	
	public SwitchEdge createConditionalEdge(IntermediateEdge ie, int match) {
		
		Resolved resolved = new Resolved((InstructionHandle)ie.getTarget(), BasicType.INT, ""+match);
		Case caseEntry = new Case((InstructionHandle)ie.getTarget(), resolved);
		SwitchEdge ce = new SwitchEdge(caseEntry);
		
		ce.setType(ie.getType());
		ce.getAttributes().putAll(ie.getAttributes());
		
		return ce;
	}
}
