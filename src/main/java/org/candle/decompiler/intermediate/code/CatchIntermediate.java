package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.intermediate.expression.Variable;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class CatchIntermediate extends AbstractIntermediate implements BlockSerializable {

	protected final BlockRange blockRange;
	protected final Variable catchVariable;
	
	public CatchIntermediate(InstructionHandle instruction, Variable catchVariable) {
		super(instruction);
		this.catchVariable = catchVariable;
		this.blockRange = new BlockRange();
		
		this.blockRange.setStart(instruction);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		
		String outputType = Utility.signatureToString(catchVariable.getType().getSignature());
		if(StringUtils.contains(outputType, ".")) {
			outputType = StringUtils.substringAfterLast(outputType, ".");
		}
		
		sw.append(outputType+" ");
		
		try {
			catchVariable.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Catch: "+sw.toString();
	}

	
	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitCatchLine(this);
	}

	@Override
	public BlockRange getBlockRange() {
		return blockRange;
	}

}
