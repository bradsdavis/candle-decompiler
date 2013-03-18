package org.candle.decompiler.instruction.graph;

import java.util.Iterator;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.UnconditionalBranch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.edge.InstructionEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

public class InstructionGraphFactory {

	private static final Log LOG = LogFactory.getLog(InstructionGraphFactory.class);
	private final InstructionList instructionList;
	private final CodeExceptionGen[] exceptions;
	
	public InstructionGraphFactory(InstructionList instructionList, CodeExceptionGen[] exceptions) {
		this.instructionList = instructionList;
		this.exceptions = exceptions;
	}
	
	public InstructionGraphContext process() {
		
		ListenableDirectedGraph<InstructionHandle, InstructionEdge> instructionHandleGraph = new ListenableDirectedGraph<InstructionHandle, InstructionEdge>(InstructionEdge.class);
		InstructionGraphContext igc = new InstructionGraphContext(instructionHandleGraph);

		for(InstructionHandle instructionHandle : instructionList.getInstructionHandles()) {
			InstructionHandle iv = instructionHandle;
			instructionHandleGraph.addVertex(iv);
		}
		
		Iterator<InstructionHandle> iter = instructionList.iterator();
		while(iter.hasNext()) {
			InstructionHandle ih = iter.next();
			InstructionHandle sourceVertex = igc.getPositionMap().get(ih.getPosition());
			
			if(ih instanceof BranchHandle) {
				//if this is an unconditional branch, only add the branch between the instruction and it's target.
				InstructionHandle targetVertex = igc.getPositionMap().get(((BranchHandle) ih).getTarget().getPosition());
				instructionHandleGraph.addEdge(sourceVertex, targetVertex);
			}
			if(!(ih.getInstruction() instanceof UnconditionalBranch)) {
				if(ih.getNext() != null) {
					InstructionHandle targetVertex = igc.getPositionMap().get((ih).getNext().getPosition());
					instructionHandleGraph.addEdge(sourceVertex, targetVertex);
				}
			}
		}
		
		
		/*
		//HAPPY PATH
		Set<InstructionHandle> happy = new HashSet<InstructionHandle>();
		InstructionHandle root = createVertex(instructionList.getStart());
		BreadthFirstIterator<InstructionHandle, InstructionEdge> bfi = new BreadthFirstIterator<InstructionHandle, InstructionEdge>(instructionHandleGraph, root);
		while(bfi.hasNext()) {
			happy.add(bfi.next());
		}
		
		InstructionTryCatch itc = new InstructionTryCatch(igc, this.exceptions);
		
		
		NamedDirectedSubgraph happyPath = new NamedDirectedSubgraph("Main", instructionHandleGraph, happy, null);
		
		
		for(CodeExceptionGen ceg : exceptions) {
			Set<InstructionHandle> cegG = new HashSet<InstructionHandle>();
			System.out.println("X"+ceg.getStartPC() + " -> "+ceg.getEndPC()+ ", "+ceg.getHandlerPC());
			
			for(InstructionHandle iv : instructionHandleGraph.vertexSet()) {
				
				if(iv.getPosition() <= ceg.getEndPC().getPosition() && iv.getPosition() >= ceg.getStartPC().getPosition()) {
					System.out.println("Position : "+iv.getPosition() );
					cegG.add(iv);
				}
			}
			
			subs.add(new NamedDirectedSubgraph("ExceptionHandler"+ceg.getCatchType().toString(), instructionHandleGraph, cegG, null));
		}
		
		*/
		return igc;
	}
}
