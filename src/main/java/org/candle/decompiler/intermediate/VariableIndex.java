package org.candle.decompiler.intermediate;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;

public class VariableIndex {

	private final int index;
	private final int start;
	private final int end;
	
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
				ih = next;
			}
			//check ih.
			if(ih instanceof BranchHandle) 
			{
				//get the max... either branch. 
				int t1 = ih.getPosition();
				int t2 = ((BranchHandle) ih).getTarget().getPosition();
				
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
