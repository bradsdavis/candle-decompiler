package org.candle.decompiler.intermediate.graph;

import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.BlockSerializable;
import org.jgrapht.ext.VertexNameProvider;

public class IntermediateLabelProvider implements VertexNameProvider<AbstractIntermediate> {

	@Override
	public String getVertexName(AbstractIntermediate vertex) {
		String line = StringUtils.replace(vertex.getInstruction().getPosition()+" : "+vertex.toString(), "\"", "'");
		
		if(vertex instanceof BlockSerializable) {
			BlockSerializable block = (BlockSerializable)vertex;
			BlockRange range = block.getBlockRange();
			line += range.toString();
		}
		
		line = StringUtils.replace(line, "\n", "  ->  ");
		line = StringUtils.replace(line, ";", " | ");
		
		return line;
	}
}
