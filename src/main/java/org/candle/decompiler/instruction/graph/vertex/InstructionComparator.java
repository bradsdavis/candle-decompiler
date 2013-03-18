package org.candle.decompiler.instruction.graph.vertex;

import java.util.Comparator;

import org.apache.bcel.generic.InstructionHandle;

public class InstructionComparator implements Comparator<InstructionHandle> {

	@Override
	public int compare(InstructionHandle v1, InstructionHandle v2) {
		return v1.getPosition() - v2.getPosition();
	}

}
