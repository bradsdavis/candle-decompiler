package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class ElseIf extends GraphIntermediateVisitor {

	public ElseIf(IntermediateGraphContext igc) {
		super(igc, false);
	}
	
	@Override
	public void visitIfLine(IfIntermediate line) {
		//check to see if the predecessor is an if block.
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		
		if(predecessors.size() != 1) {
			return;
		}
		
		//otherwise, see if it is another IF.
		if(predecessors.get(0) instanceof IfIntermediate || predecessors.get(0) instanceof ElseIfIntermediate) {
			//check to see whether it is on the ELSE side.
			IfIntermediate parent = (IfIntermediate)predecessors.get(0);
			
			if(igc.getFalseTarget(parent) == line) {
				//then this could be an IF block.
				
				ElseIfIntermediate eii = new ElseIfIntermediate(line.getInstruction(), line.getExpression());
				
				igc.getIntermediateGraph().addVertex(eii);

				igc.redirectPredecessors(line, eii);
				igc.redirectSuccessors(line, eii);
				
				igc.getIntermediateGraph().removeVertex(line);
			}
		}
		
	}

}
