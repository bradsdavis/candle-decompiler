package org.candle.decompiler.intermediate;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VariableIndex {
	private static final Log LOG = LogFactory.getLog(VariableIndex.class);
	private final int index;
	private final int start;
	private final int end;
	
	
	
	@Override
	public String toString() {
		return "VariableIndex [index=" + index + ", start=" + start + ", end="
				+ end + "]";
	}

	public VariableIndex(int index, int start, int end) {
		this.index= index;
		this.start = start;
		this.end = end;
	}
	
	public boolean withinBounds(int index, int pc) {
		if(index == this.index) {
			if(pc >= start && pc <=end) {
				return true;
			}
		}
		
		return false;
	}
	
	public static class Factory {
		
		public static VariableIndex createFromInstructionHandle(InstructionHandle ih, int index) {
			//read ahead.
			int start = ih.getPosition();
			int end = start;
			while(!(ih instanceof BranchHandle)) {
				InstructionHandle next = ih.getNext();
				if(next == null) {
					break;
				}
				if(LOG.isDebugEnabled()) {
					LOG.debug("Skipping forward: "+next);
				}
				ih = next;
			}
			//check ih.
			if(ih instanceof BranchHandle) 
			{
				//get the max... either branch. 
				int t1 = ih.getPosition();
				
				InstructionHandle t2handle = ((BranchHandle) ih).getTarget();
				if(t2handle.getNext() != null) {
					t2handle = t2handle.getNext();
				}
				
				int t2 = t2handle.getPosition();
				if(LOG.isDebugEnabled()) {
					LOG.debug("Skipping forward: "+t2);
				}
				end = t1 > t2 ? t1 : t2;
			}
			else {
				end = ih.getPosition();
			}
			
			VariableIndex vi = new VariableIndex(index, start, end);
			return vi;
		}
		
		
	}
	
}
