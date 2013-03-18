package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.Type;
import org.candle.decompiler.ast.SignatureUtility;
import org.apache.bcel.generic.InstructionHandle;


public class NewInstance extends ObjectType {

	public NewInstance(InstructionHandle instructionHandle, Type type) {
		super(instructionHandle, type);
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("new ");
		
		String signature = SignatureUtility.signatureToString(type.getSignature());
		writer.append(signature);
	}
	

}
