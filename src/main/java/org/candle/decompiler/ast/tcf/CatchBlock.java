package org.candle.decompiler.ast.tcf;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.classfile.Utility;
import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.ast.SignatureUtility;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.expression.Variable;

public class CatchBlock extends SerializableBlock<CatchIntermediate> {

	public CatchBlock(CatchIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("catch(");
		
		Variable catchVariable = intermediate.getCatchVariable();
		String type = SignatureUtility.signatureToString(catchVariable.getType().getSignature());
		
		builder.append(type);
		builder.append(" ");
		builder.append(catchVariable.getName());
		builder.append(") ");
		super.write(builder);
	}
	
}
