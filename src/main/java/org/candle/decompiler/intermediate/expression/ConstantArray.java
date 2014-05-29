package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;

public class ConstantArray extends Expression {

	private final List<Expression> values = new LinkedList<Expression>();

	public ConstantArray(InstructionHandle instruction, List<Expression> values) {
		super(instruction);
		if(values != null) {
			this.values.addAll(values);
		}
	}
	
	public List<Expression> getValues() {
		return values;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("{");
		
		int count = 0;
		for(Expression expression : values) {
			if(count > 0) {
				writer.write(", ");
			}
			expression.write(writer);
			count++;
		}
		writer.append("}");
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		for(Expression exp: values) {
			listener.accept(exp);
		}
	}
	
}
