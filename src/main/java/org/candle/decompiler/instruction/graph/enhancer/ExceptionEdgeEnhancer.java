	package org.candle.decompiler.instruction.graph.enhancer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.EdgeType;
import org.candle.decompiler.intermediate.expression.Resolved;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;

public class ExceptionEdgeEnhancer extends InstructionGraphEnhancer {

	public static final String CEG_KEY = "CEG_KEY";
	public static final String EXCEPTION_STACK_KEY = "EXCEPTION_STACK_KEY";
	 
	private final CodeExceptionGen[] exceptions;
	
	public ExceptionEdgeEnhancer(InstructionGraphContext igc, CodeExceptionGen[] exceptions) {
		super(igc);
		this.exceptions = exceptions;
	}

	@Override
	public void process() {
		Set<Integer> positions = new HashSet<Integer>();
		
		for(CodeExceptionGen ceg : exceptions) {
			int t1 = ceg.getStartPC().getPosition();
			int t2 = ceg.getHandlerPC().getPosition();
			positions.add(t1);
			positions.add(t2);
		}

		Map<Integer, InstructionHandle> ivc = new HashMap<Integer, InstructionHandle>();
		
		for(InstructionHandle iv : igc.getGraph().vertexSet()) {
			if(positions.contains(iv.getPosition())) {
				ivc.put(iv.getPosition(), iv);
			}
		}
		
		//now, we will map the CEG.
		for(CodeExceptionGen ceg : exceptions) {
			int t1 = ceg.getStartPC().getPosition();
			int t2 = ceg.getHandlerPC().getPosition();
			
			InstructionHandle source = ivc.get(t1);
			InstructionHandle target = ivc.get(t2);

			IntermediateEdge ie = new IntermediateEdge();
			addExceptionHandle(ie, ceg);
			ie.setType(EdgeType.EXCEPTION);
			igc.getGraph().addEdge(source, target, ie);
		}
	}
	
	private void addExceptionHandle(IntermediateEdge ie, CodeExceptionGen ceg) {
		ObjectType ot = ceg.getCatchType();
		Resolved resolved = null;
		if(ot == null) {
			resolved = new Resolved((InstructionHandle)ie.getTarget(), Type.THROWABLE, "e");
		}
		else {
			resolved = new Resolved((InstructionHandle)ie.getTarget(), ot, ot.toString());
		}
		
		ie.getAttributes().put(EXCEPTION_STACK_KEY, resolved);
	}
}
