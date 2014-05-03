package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;

/***
 * For each instruction... check it's successors.  If there is one, eliminate the node
 * as long as the node doesn't contain an intermediate.
 * 
 * @author bradsdavis@gmail.com
 *
 */
public class NonIntermediateEliminator extends InstructionHandleEnhancer {

	private static final Log LOG = LogFactory.getLog(NonIntermediateEliminator.class);
	
	public NonIntermediateEliminator(InstructionGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void process(InstructionHandle ih) {
		if(igc.hasIntermediate(ih)) {
			//don't process if there is an intermediate.
			return;
		}
		
		List<InstructionHandle> successors = igc.getSuccessors(ih);
		
		if(successors.size() == 1) {
			//now check to see whether there is an intermediate on the objects...
			InstructionHandle target = successors.get(0);
			
			if(LOG.isDebugEnabled()) {
				LOG.debug("Remove vertex:" + ih);
			}
			
			igc.redirectPredecessors(ih, target);
			igc.getGraph().removeVertex(ih);
		}
		else { 
			List<InstructionHandle> predecessors = igc.getPredecessors(ih);
			
			if(predecessors.size() == 1) {
				//compress backwards...
				igc.redirectSuccessors(ih, predecessors.get(0));
				
				igc.getGraph().removeVertex(ih);
			}
		}
		
		
	}
}
