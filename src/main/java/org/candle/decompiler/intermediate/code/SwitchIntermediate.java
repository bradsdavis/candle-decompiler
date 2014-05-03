package org.candle.decompiler.intermediate.code;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class SwitchIntermediate extends MultiBranchIntermediate implements BlockSerializable {

	private final BlockRange blockRange;
	protected final MultiBranchIntermediate conditionalIntermediate;
	
	public SwitchIntermediate(InstructionHandle bi, MultiBranchIntermediate ci) {
		super(bi, ci.getExpression());
		this.conditionalIntermediate = ci;
		this.blockRange = new BlockRange();
		
		this.blockRange.setStart(bi);
	}
	
	@Override
	public BlockRange getBlockRange() {
		return this.blockRange;
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitSwitchIntermediate(this);
	}
}
