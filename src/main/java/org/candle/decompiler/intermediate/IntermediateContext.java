package org.candle.decompiler.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.expression.Expression;

public class IntermediateContext {
	
	private final JavaClass clazz;
	private final MethodGen methodGen;
	
	private final Stack<Expression> expressions = new Stack<Expression>();
	private final Stack<AbstractIntermediate> intermediate = new Stack<AbstractIntermediate>();
	private final VariableResolver variableResolver;
	
	public IntermediateContext(JavaClass clazz, MethodGen methodGen) {
		this.clazz = clazz;
		this.methodGen = methodGen;
		this.variableResolver = new VariableResolver(this.methodGen);
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
	
	public Stack<AbstractIntermediate> getIntermediate() {
		return intermediate;
	}
	
	private InstructionHandle currentInstruction;
	
	public void setCurrentInstruction(InstructionHandle currentInstruction) {
		this.currentInstruction = currentInstruction;
	}
	
	public InstructionHandle getCurrentInstruction() {
		return currentInstruction;
	}
	
}
