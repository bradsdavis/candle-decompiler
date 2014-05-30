package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.generic.Type;
import org.apache.commons.lang.StringUtils;
import org.candle.decompiler.ast.SignatureUtility;
import org.apache.bcel.generic.InstructionHandle;

public class ArrayCreation extends NewInstance {

	protected List<Expression> dimensions;
	
	public ArrayCreation(InstructionHandle instructionHandle, Type type, Expression count) {
		super(instructionHandle, type);
		
		List<Expression> exps = new LinkedList<Expression>();
		exps.add(count);
		this.setDimensions(exps);
	}
	
	public ArrayCreation(InstructionHandle instructionHandle, Type type, List<Expression> counts) {
		super(instructionHandle, type);
		this.setDimensions(counts);
	}
	
	public List<Expression> getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(List<Expression> d) {
		if(this.dimensions != null) {
			for(Expression exp : dimensions) {
				exp.setParent(null);
			}
		}
		
		this.dimensions = d;
		
		if(this.dimensions != null) {
			for(Expression exp : dimensions) {
				exp.setParent(this);
			}
		}
	}

	@Override
	public void write(Writer builder) throws IOException {
		builder.append("new ");
		
		final int dimensionCounts = StringUtils.countMatches(type.getSignature(), "[");
		
		String signature = SignatureUtility.signatureToString(type.getSignature());
		signature = StringUtils.substringBefore(signature, "[]");
		builder.append(signature);
		
		
		for(int i=0; i< dimensionCounts; i++) {
			builder.append("[");
			if(i < dimensions.size()) {
				Expression exp = dimensions.get(i);
				exp.write(builder);
			}
			builder.append("]");
		}
		
	}
	
}
