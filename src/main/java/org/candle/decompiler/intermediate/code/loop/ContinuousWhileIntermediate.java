package org.candle.decompiler.intermediate.code.loop;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.BlockSerializable;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.expression.Resolved;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class ContinuousWhileIntermediate extends AbstractIntermediate implements BlockSerializable {
	
	private Expression expression;
	
	public ContinuousWhileIntermediate(InstructionHandle instruction) {
		super(instruction);
		this.expression = new Resolved(instruction, Type.BOOLEAN, "true");
		this.blockRange = new BlockRange();
	}

	private final BlockRange blockRange;

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.expression.write(sw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "While: "+sw.toString();
	}
	
	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitContinuousWhileIntermediate(this);
	}

	public BlockRange getBlockRange() {
		return blockRange;
	}
}
