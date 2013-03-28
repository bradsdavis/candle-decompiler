package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.intermediate.graph.edge.ConditionEdge;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;

public class ConditionEdgeEnhancer extends InstructionHandleEnhancer {

	public ConditionEdgeEnhancer(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void process(InstructionHandle ih) {
		if(ih instanceof BranchHandle) {
			//ok, now we need to replace existing successor edges appropriately.
			BranchHandle bh = (BranchHandle)ih;
			
			List<InstructionHandle> successors = igc.getSuccessors(ih);
			//find the next instruction..
			
			if(successors.size() == 2) {
				for(InstructionHandle successor : successors) {
					IntermediateEdge ie = igc.getGraph().getEdge(ih, successor);
					igc.getGraph().removeEdge(ie);
					
					if(successor.getPosition() == bh.getTarget().getPosition()) {
						//false case.
						ConditionEdge ce = createConditionalEdge(ie, false);
						
						//remove existing edge.
						boolean added = igc.getGraph().addEdge(ih, successor, ce);
						System.out.println(added+ " from "+ie);
						
						System.out.println(ReflectionToStringBuilder.toString(igc.getGraph().getEdge(ih, successor)));
					}
					else {
						//false case.
						ConditionEdge ce = createConditionalEdge(ie, true);
						
						//remove existing edge.
						boolean added = igc.getGraph().addEdge(ih, successor, ce);
						System.out.println(added+ " from "+ie);
						
						System.out.println(ReflectionToStringBuilder.toString(igc.getGraph().getEdge(ih, successor)));
					}
				}
			}
		}
	}

	public ConditionEdge createConditionalEdge(IntermediateEdge ie, boolean condition) {
		ConditionEdge ce = new ConditionEdge();
		ce.setCondition(condition);
		ce.setType(ie.getType());
		ce.getAttributes().putAll(ie.getAttributes());
		
		return ce;
	}
}
