package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InstructionHandle;
public class Ternary extends Expression {

	public Ternary(InstructionHandle instructionHandle,
			Type type, Expression logic, Expression left, Expression right) {
		super(instructionHandle);
		
		this.type = type;
		this.logic = logic;
		this.left = left;
		this.right = right;
	}

	private Type type;
	private Expression logic;
	private Expression left;
	private Expression right;
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(getLogic());
		listener.accept(getLeft());
		listener.accept(getRight());
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.write("(");
		logic.write(writer);
		writer.write(")");
		
		writer.write(" ? ");
		
		left.write(writer);
		
		writer.write(" : ");
		
		right.write(writer);
	}
	
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Expression getLeft() {
		return left;
	}
	
	public void setLeft(Expression left) {
		this.left = left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	public void setRight(Expression right) {
		this.right = right;
	}
	
	public Expression getLogic() {
		return logic;
	}
	
	public void setLogic(Expression logic) {
		this.logic = logic;
	}

}
