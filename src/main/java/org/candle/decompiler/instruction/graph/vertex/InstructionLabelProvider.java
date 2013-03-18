package org.candle.decompiler.instruction.graph.vertex;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.jgrapht.ext.VertexNameProvider;

public class InstructionLabelProvider implements VertexNameProvider<InstructionHandle> {

	@Override
	public String getVertexName(InstructionHandle vertex) {
		String line = StringUtils.replace(vertex.toString(), "\"", "'");
		line = StringUtils.replace(line, "\n", "  ->  ");
		
		if(vertex.getAttribute(IntermediateContext.INTERMEDIATE_KEY) != null) {
			String temp = vertex.getAttribute(IntermediateContext.INTERMEDIATE_KEY).toString();
			temp = StringUtils.replace(temp, "\"", "'");
			temp = StringUtils.replace(temp, "\n", "  ->  ");
			temp = StringUtils.replace(temp, ";", "");
			line = line + " -> " + temp;
		}
		
		return line;
	}
} 


