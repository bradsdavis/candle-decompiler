package org.candle.decompiler.intermediate.graph;

import java.util.Collection;
import java.util.TreeMap;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;

public class IntermediateLineContext {

	
	private final TreeMap<Integer, AbstractIntermediate> intermediate = new TreeMap<Integer, AbstractIntermediate>();
	
	public IntermediateLineContext(Collection<AbstractIntermediate> lines) {
		for(AbstractIntermediate al : lines) {
			Integer position = al.getInstruction().getPosition();
			intermediate.put(position, al);
		}
	}
	
	
	public AbstractIntermediate getNext(AbstractIntermediate ai) {
		if(ai instanceof GoToIntermediate) {
			BranchHandle bh = (BranchHandle)ai.getInstruction();
			Integer position = bh.getTarget().getPosition();
			return getNext(position);
		}

		//otherwise, get it from the next position.
		InstructionHandle next = ai.getInstruction().getNext();

		//return either null, if next is null (last next).  Otherwise, send next position.
		return next == null ? null : getNext(next.getPosition());
	}
	
	public AbstractIntermediate getNext(Integer position) {
		Integer next = intermediate.tailMap(position).firstKey();
		
		if(next != null) {
			return intermediate.get(next);
		}
		
		return null;
	}
	
	public TreeMap<Integer, AbstractIntermediate> getIntermediate() {
		return intermediate;
	}
}
