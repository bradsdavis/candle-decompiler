package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class CaseIntermediate extends AbstractIntermediate {

	private final Case caseEntry;
	
	public CaseIntermediate(InstructionHandle instruction, Case caseVal) {
		super(instruction);
		this.caseEntry = caseVal;
	}
	
	public Case getCaseEntry() {
		return caseEntry;
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.caseEntry.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Case: "+sw.toString();
	}
	
	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitCaseLine(this);
	}

}
