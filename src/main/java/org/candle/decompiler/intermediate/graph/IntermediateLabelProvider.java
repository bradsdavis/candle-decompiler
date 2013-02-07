package org.candle.decompiler.intermediate.graph;

import org.apache.bcel.generic.BranchHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.jgrapht.ext.VertexNameProvider;

public class IntermediateLabelProvider implements VertexNameProvider<AbstractIntermediate> {

	@Override
	public String getVertexName(AbstractIntermediate vertex) {
		String line = StringUtils.replace(vertex.getInstruction().getPosition()+" : "+vertex.toString(), "\"", "'");
		
		/*
		if(vertex instanceof ConditionalIntermediate) {
			BranchHandle bh = (BranchHandle)vertex.getInstruction();
			line = StringUtils.replace("Branch ["+bh.getInstruction().getName()+"] to ["+bh.getNext().getPosition()+"] or ["+bh.getTarget().getPosition()+"] : "+vertex.toString(), "\"", "'");
		}
		*/
		
		line = StringUtils.replace(line, "\n", "  ->  ");
		line = StringUtils.replace(line, ";", " | ");
		
		return line;
	}
}
