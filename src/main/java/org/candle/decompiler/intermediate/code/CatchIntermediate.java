package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.SignatureUtility;
import org.candle.decompiler.intermediate.expression.Variable;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class CatchIntermediate extends AbstractIntermediate implements BlockSerializable {

	protected final BlockRange blockRange;
	protected final Variable catchVariable;
	protected final CodeExceptionGen ceg;
	
	public CatchIntermediate(InstructionHandle instruction, CodeExceptionGen ceg, Variable catchVariable) {
		super(instruction);
		this.catchVariable = catchVariable;
		this.blockRange = new BlockRange();
		
		this.ceg = ceg;
		this.blockRange.setStart(instruction);
	}
	
	public Variable getCatchVariable() {
		return catchVariable;
	}

	public CodeExceptionGen getCeg() {
		return ceg;
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		
		String outputType = SignatureUtility.signatureToString(catchVariable.getType().getSignature());
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
		visitor.visitAbstractIntermediate(this);
		visitor.visitCatchIntermediate(this);
	}

	@Override
	public BlockRange getBlockRange() {
		return blockRange;
	}

}
