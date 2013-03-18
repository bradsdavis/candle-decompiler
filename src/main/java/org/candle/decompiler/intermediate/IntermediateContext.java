package org.candle.decompiler.intermediate;

import java.util.Stack;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.expression.Expression;

public class IntermediateContext {
	public static final String INTERMEDIATE_KEY = "INTERMEDIATE_KEY";
	
	private final JavaClass clazz;
	private final MethodGen methodGen;
	
	private Stack<Expression> expressions;
	private final VariableResolver variableResolver;
	
	public IntermediateContext(JavaClass clazz, MethodGen methodGen) {
		this.clazz = clazz;
		this.methodGen = methodGen;
		this.variableResolver = new VariableResolver(this.methodGen);
	}
	
	public void setExpressions(Stack<Expression> expressions) {
		this.expressions = expressions;
	}

	public VariableResolver getVariableResolver() {
		return variableResolver;
	}
	
	public JavaClass getJavaClass() {
		return clazz;
	}
	
	public MethodGen getMethodGen() {
		return methodGen;
	}
	
	public Stack<Expression> getExpressions() {
		return expressions;
	}
	
	private InstructionHandle currentInstruction;
	
	public void setCurrentInstruction(InstructionHandle currentInstruction) {
		this.currentInstruction = currentInstruction;
	}
	
	public InstructionHandle getCurrentInstruction() {
		return currentInstruction;
	}

	public void pushIntermediateToInstruction(final AbstractIntermediate ai) {
		System.out.println("Pushing: "+ai+" to: "+this.currentInstruction);
		this.currentInstruction.addAttribute(INTERMEDIATE_KEY, ai);
	}
	
}
