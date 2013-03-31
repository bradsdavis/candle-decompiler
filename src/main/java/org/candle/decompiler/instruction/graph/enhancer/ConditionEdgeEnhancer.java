package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;
import java.util.TreeSet;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.vertex.InstructionComparator;
import org.candle.decompiler.intermediate.graph.edge.ConditionEdge;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;

public class ConditionEdgeEnhancer extends InstructionHandleEnhancer {

	private static final Log LOG = LogFactory.getLog(ConditionEdgeEnhancer.class);
	
	public ConditionEdgeEnhancer(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void process(InstructionHandle ih) {
		if(ih instanceof BranchHandle) {
			//ok, now we need to replace existing successor edges appropriately.
			BranchHandle bh = (BranchHandle)ih;
			
			List<InstructionHandle> successors = igc.getSuccessors(ih);
			TreeSet<InstructionHandle> orderedSuccessors = new TreeSet<InstructionHandle>(new InstructionComparator());
			orderedSuccessors.addAll(successors);
			
			if(successors.size() == 2) {
				//lowest will be true condition.... 
				IntermediateEdge truePath = igc.getGraph().getEdge(ih, orderedSuccessors.first());
				ConditionEdge trueCondition = createConditionalEdge(truePath, true);
				igc.getGraph().removeEdge(truePath);
				igc.getGraph().addEdge(ih, orderedSuccessors.first(), trueCondition);
				
				//highest will be false condition....
				IntermediateEdge falsePath = igc.getGraph().getEdge(ih, orderedSuccessors.last());
				ConditionEdge falseCondition = createConditionalEdge(falsePath, false);
				igc.getGraph().removeEdge(falsePath);
				igc.getGraph().addEdge(ih, orderedSuccessors.last(), falseCondition);
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
