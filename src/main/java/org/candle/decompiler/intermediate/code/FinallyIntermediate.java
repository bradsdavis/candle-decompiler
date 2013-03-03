package org.candle.decompiler.intermediate.code;

import java.io.StringWriter;
import java.util.Set;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class FinallyIntermediate extends AbstractIntermediate implements BlockSerializable {

	private final Set<CodeExceptionGen> codeExceptions;
	private final BlockRange blockRange;
	//private final Variable catchVariable;
	
	public FinallyIntermediate(InstructionHandle instruction, Set<CodeExceptionGen> cegs) {
		super(instruction);
		this.blockRange = new BlockRange();
		this.blockRange.setStart(instruction);
		
		this.codeExceptions = cegs;
	}
	
	public Set<CodeExceptionGen> getCodeExceptions() {
		return codeExceptions;
	}

	@Override
	public BlockRange getBlockRange() {
		return blockRange;
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitFinallyIntermediate(this);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.append("Finally");
		for(CodeExceptionGen ceg : codeExceptions) {
			sw.append(" | Handler["+ceg.getStartPC().getPosition()+", "+ceg.getEndPC().getPosition()+"]");
		}
		return sw.toString() + ";";
	}
	
}
