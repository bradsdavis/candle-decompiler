package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;

/***
 * For each instruction... check it's successors.  If there is one, eliminate the node
 * as long as the node doesn't contain an intermediate.
 * 
 * @author bradsdavis
 *
 */
public class NonIntermediateEliminator extends InstructionHandleEnhancer {

	public NonIntermediateEliminator(InstructionGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void process(InstructionHandle ih) {
		List<InstructionHandle> successors = igc.getSuccessors(ih);
		
		if(successors.size() == 1) {
			//now check to see whether there is an intermediate on the objects...
			if(igc.hasIntermediate(ih)) {
				//don't process if there is an intermediate.
				return;
			}
			
			InstructionHandle target = successors.get(0);
			System.out.println("Remove vertex:" + ih);
			igc.redirectPredecessors(ih, target);
			igc.getGraph().removeVertex(ih);
		}
		
		
		
	}
}
