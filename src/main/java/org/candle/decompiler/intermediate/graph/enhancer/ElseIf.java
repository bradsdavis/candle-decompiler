package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchOutcome;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class ElseIf extends GraphIntermediateVisitor {

	private static final Log LOG = LogFactory.getLog(ElseIf.class);
	
	public ElseIf(IntermediateGraphContext igc) {
		super(igc, false);
	}
	
	@Override
	public void visitIfIntermediate(IfIntermediate line) {
		//check to see if the predecessor is an if block.
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		
		if(predecessors.size() != 1) {
			return;
		}
		
		//otherwise, see if it is another IF.
		if(predecessors.get(0) instanceof BooleanBranchOutcome) {
			
			//check to see whether it is on the ELSE side.
			BooleanBranchIntermediate parent = (BooleanBranchIntermediate)igc.getSinglePredecessor(((BooleanBranchOutcome)predecessors.get(0)));
			LOG.debug(parent.getClass());
			if(!(parent instanceof IfIntermediate)) {
				return;
			}
			
			
			if(igc.getFalseTarget(parent) == line) {
				//then this could be an IF block.
				ElseIfIntermediate eii = new ElseIfIntermediate(line.getInstruction(), line.getExpression());
				igc.getIntermediateGraph().addVertex(eii);
				
				igc.replaceBooleanBranchIntermediate(line, eii);
				igc.redirectPredecessors(line, eii);
				igc.redirectSuccessors(line, eii);
				
				igc.getIntermediateGraph().removeVertex(line);
			}
		}
		
	}

}
