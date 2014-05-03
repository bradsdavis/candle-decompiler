package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

/**
 * Looks at branch intermediate instructions.  If the proceeding is an if statement, this is on the "false" path of the if statement, 
 * and there is conditional logic, convert the vertex to an ElseIf statement.
 * 
 * @author bradsdavis
 *
 */
public class ElseIf extends GraphIntermediateVisitor {

	private static final Log LOG = LogFactory.getLog(ElseIf.class);
	
	public ElseIf(IntermediateGraphContext igc) {
		super(igc, false);
	}
	
	@Override
	public void visitIfIntermediate(IfIntermediate line) {
		//check to see if the predecessor is an if block.
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getGraph(), line);
		
		if(predecessors.size() != 1) {
			return;
		}
		
		//otherwise, see if it is another IF.
		if(predecessors.get(0) instanceof BooleanBranchIntermediate) {
			
			//check to see whether it is on the ELSE side.
			BooleanBranchIntermediate parent = (BooleanBranchIntermediate)predecessors.get(0);
			LOG.debug(parent.getClass());
			if(!(parent instanceof IfIntermediate)) {
				return;
			}
			
			
			if(igc.getFalseTarget(parent) == line) {
				//then this could be an IF block.
				ElseIfIntermediate eii = new ElseIfIntermediate(line.getInstruction(), line.getExpression());
				igc.getGraph().addVertex(eii);
				
				igc.redirectPredecessors(line, eii);
				igc.redirectSuccessors(line, eii);
				
				igc.getGraph().removeVertex(line);
			}
		}
		
	}

}
