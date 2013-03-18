package org.candle.decompiler.instruction.graph.enhancer;

import java.util.List;

import org.apache.bcel.generic.DuplicateHandle;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.PopInstruction;
import org.apache.bcel.generic.PushInstruction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;

public class SplitInstructionEnhancer extends InstructionGraphEnhancer {
	private static final Log LOG = LogFactory.getLog(SplitInstructionEnhancer.class);
	
	public SplitInstructionEnhancer(InstructionGraphContext igc) {
		super(igc);
	}

	@Override
	public void visitPopInstruction(PopInstruction obj) {
		//check instruction in map.
		
		List<InstructionHandle> ivs = this.igc.getPredecessors(this.current);
		if(ivs.size() > 1) {
			//check to see if the predecessors pushed.
			
			int count = 0;
			for(InstructionHandle iv : ivs) {
				InstructionHandle jvmiv = (InstructionHandle)iv;
				jvmiv = findSource(jvmiv);
				
				if(jvmiv.getInstruction() instanceof PushInstruction) {
					count++;
				}
			}
			
			if(count > 1) {
				LOG.info("Split vertex.");
				splitVertex(this.current);
			}
		}
	}
	
	protected void splitVertex(InstructionHandle iv) {
		List<InstructionHandle> preds = this.igc.getPredecessors(this.current);
		List<InstructionHandle> sucs = this.igc.getSuccessors(this.current);
		
		//remove all edges.
		for(InstructionHandle source : preds) {
			this.igc.getGraph().removeEdge(source, iv);
		}
		
		int i=0;
		InstructionHandle target = iv;
		for(InstructionHandle source : preds) {
			if(i>0) {
				//clone IV.
				try {
					target = new DuplicateHandle(iv);
					igc.getGraph().addVertex(target);
					
					for(InstructionHandle successor : sucs) {
						igc.getGraph().addEdge(target, successor);
					}
					
				} catch (Exception e) {
					LOG.error("Exception cloning.", e);
				}
			}
			
			igc.getGraph().addEdge(source, target);
			i++;
		}
	}
	
	protected InstructionHandle findSource(InstructionHandle jiv) {
		while(jiv.getInstruction() instanceof GOTO) {
			jiv = (InstructionHandle)igc.getSinglePredecessor(jiv);
		}
		
		return jiv;
	}
	
	
}
