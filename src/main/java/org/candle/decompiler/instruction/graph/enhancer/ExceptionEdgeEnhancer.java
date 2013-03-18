package org.candle.decompiler.instruction.graph.enhancer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.CodeExceptionGen;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.EdgeType;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.apache.bcel.generic.InstructionHandle;

public class ExceptionEdgeEnhancer extends InstructionGraphEnhancer {

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
			
			InstructionEdge ie = new InstructionEdge();
			ie.setType(EdgeType.EXCEPTION);
			igc.getGraph().addEdge(source, target, ie);
		}
		
		
	}
}
