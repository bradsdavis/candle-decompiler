package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.SignatureUtility;

public class NewArrayInstance extends NewInstance {

	protected final List<Expression> counts = new LinkedList<Expression>();
	
	public NewArrayInstance(InstructionHandle instructionHandle, Type type, Expression count) {
		super(instructionHandle, type);
		this.counts.add(count);
	}
	
	public NewArrayInstance(InstructionHandle instructionHandle, Type type, List<Expression> counts) {
		super(instructionHandle, type);
		this.counts.addAll(counts);
	}

	@Override
	public void write(Writer builder) throws IOException {
		builder.append("new ");
		
		int dimensions = StringUtils.countMatches(type.getSignature(), "[");
		
		String signature = SignatureUtility.signatureToString(type.getSignature());
		signature = StringUtils.substringBefore(signature, "[]");
		builder.append(signature);
		
		
		for(int i=0; i<dimensions; i++) {
			builder.append("[");
			if(i < counts.size()) {
				Expression exp = counts.get(i);
				exp.write(builder);
			}
			builder.append("]");
		}
		
	}
	
}
