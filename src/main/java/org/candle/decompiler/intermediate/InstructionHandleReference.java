package org.candle.decompiler.intermediate;

import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public interface InstructionHandleReference {
	public Set<InstructionHandle> getAllHandles();
}
