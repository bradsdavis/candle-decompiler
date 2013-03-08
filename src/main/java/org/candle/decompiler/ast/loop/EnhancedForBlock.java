package org.candle.decompiler.ast.loop;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.classfile.Utility;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.SerializableBlock;
import org.candle.decompiler.ast.SignatureUtility;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;

public class EnhancedForBlock extends SerializableBlock<EnhancedForIntermediate> {

	public EnhancedForBlock(EnhancedForIntermediate intermediate) {
		super(intermediate);
	}

	@Override
	public void write(Writer builder) throws IOException {
		final String indent = buildIndent();
		builder.append(indent);
		builder.append("for(");
		try {
			String outputType = SignatureUtility.signatureToString(intermediate.getVariable().getType().getSignature());
			
			if(StringUtils.contains(outputType, ".")) {
				outputType = StringUtils.substringAfterLast(outputType, ".");
			}
			builder.append(outputType);
			builder.append(" ");
			builder.append(intermediate.getVariable().getName());
			
			builder.append(" : ");
			intermediate.getRight().write(builder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		builder.append(") ");
		super.write(builder);
	}
}
