package org.candle.decompiler.instruction;

import java.util.List;
import java.util.Stack;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.candle.decompiler.intermediate.expression.Expression;
import org.jgrapht.event.VertexTraversalEvent;

public class StackClonePointListener {
	public final String STACK_KEY = "STACK_KEY";
	
	private final IntermediateContext ic;
	private final InstructionGraphContext igc;
	
	public StackClonePointListener(InstructionGraphContext igc, IntermediateContext ic) {
		this.igc = igc;
		this.ic = ic;
	}
	
	public void setup(VertexTraversalEvent<InstructionHandle> e) {
		InstructionHandle ih = e.getVertex();
		
		//set new stack on first...
		if(igc.getPredecessors(ih).size() == 0) {
			addExpressionStack(ih, new Stack<Expression>());
			System.out.println("Must be first: "+ih);
			
		}
		
		//set current stack.
		switchStack(ih);
		
	}
	
	public void finish(VertexTraversalEvent<InstructionHandle> e) {
		InstructionHandle ih = e.getVertex();
		
		List<InstructionHandle> successors = igc.getSuccessors(ih);
		if(successors.size() > 1) {
			//duplicate the stack to all children...
			System.out.println("Must duplicate stack to all children: "+ih);
			
			for(InstructionHandle successor : successors) {
				addExpressionStack(successor, (Stack)ic.getExpressions().clone());
			}
		}
	}
	
	public void addExpressionStack(InstructionHandle ih, Stack<Expression> expressions) {
		ih.addAttribute(STACK_KEY, expressions);
	}
	
	public void switchStack(InstructionHandle ih) {
		if(ih.getAttribute(STACK_KEY) != null)
		{
			Stack<Expression> expressionStack = (Stack<Expression>)ih.getAttribute(STACK_KEY);
			
			System.out.println("@ Instruction: "+ih+ "   | Switching : ["+ic.getExpressions() +"] to ... ["+expressionStack+"]");
			ic.setExpressions(expressionStack);
		}
	}
}
