package org.candle.decompiler.instruction;

import java.util.List;
import java.util.Stack;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.EdgeType;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.candle.decompiler.instruction.graph.enhancer.ExceptionEdgeEnhancer;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.candle.decompiler.intermediate.expression.Expression;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.VertexTraversalEvent;

public class StackClonePointListener {
	private static final Log LOG = LogFactory.getLog(StackClonePointListener.class);
	
	public final String STACK_KEY = "STACK_KEY";
	
	private final IntermediateContext ic;
	private final InstructionGraphContext igc;
	
	public StackClonePointListener(InstructionGraphContext igc, IntermediateContext ic) {
		this.igc = igc;
		this.ic = ic;
	}
	
	public void setup(InstructionHandle ih) {
		//set new stack on first...
		if(igc.getPredecessors(ih).size() == 0) {
			addExpressionStack(ih, new Stack<Expression>());
			LOG.debug("Must be first: "+ih);
		}
		switchStack(ih);
	}
	
	public void setup(EdgeTraversalEvent<InstructionHandle, InstructionEdge> e) {
		switchStack((InstructionHandle)e.getEdge().getTarget());
		if(e.getEdge().getType() == EdgeType.EXCEPTION) {
			Expression exExp = (Expression)e.getEdge().getAttributes().get(ExceptionEdgeEnhancer.EXCEPTION_STACK_KEY);
			ic.getExpressions().add(exExp);
		}
	}
	
	public void finish(VertexTraversalEvent<InstructionHandle> e) {
		InstructionHandle ih = e.getVertex();
		
		List<InstructionHandle> successors = igc.getSuccessors(ih);
		if(successors.size() > 1) {
			//duplicate the stack to all children...
			LOG.debug("Must duplicate stack to all children: "+ih);
			
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
			
			LOG.debug("@ Instruction: "+ih+ "   | Switching : ["+ic.getExpressions() +"] to ... ["+expressionStack+"]");
			ic.setExpressions(expressionStack);
		}
	}
}
