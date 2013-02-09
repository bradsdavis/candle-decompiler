package org.candle.decompiler.intermediate.graph;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Select;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Return;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;
import org.jgrapht.graph.ListenableDirectedGraph;

public class IntermediateGraphFactory extends EmptyIntermediateVisitor {

	private final IntermediateLineContext ilc;
	private final IntermediateGraphContext igc;
	
	public IntermediateGraphFactory(IntermediateLineContext ilc) {
		//first, populate the map.
		ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph = new ListenableDirectedGraph<AbstractIntermediate, IntermediateEdge>(IntermediateEdge.class);
		this.igc = new IntermediateGraphContext(intermediateGraph);
		this.ilc = ilc;
		
		Set<AbstractIntermediate> lines = new HashSet<AbstractIntermediate>(ilc.getIntermediate().values());
		for(AbstractIntermediate line : lines) {
			line.accept(this);
		}
	}
	
	public IntermediateGraphContext getIntermediateGraph() {
		return igc;
	}
	
	@Override
	public void visitCompleteLine(StatementIntermediate line) {
		AbstractIntermediate next = ilc.getNext(line);
		
		//check to see if it is a return statement.
		if(line.getExpression() instanceof Return) {
			//don't add a line to next.
			return;
		}
		
		if(next != null) {
			//find how that actually maps to the abstract line..
			AbstractIntermediate intermediate = next;
			
			//now, we just add this into the graph.
			igc.getIntermediateGraph().addVertex(intermediate);
			igc.getIntermediateGraph().addEdge(line, intermediate);
		}
	}

	@Override
	public void visitGoToLine(GoToIntermediate line) {
		
		//find how that actually maps to the abstract line..
		AbstractIntermediate intermediate = ilc.getNext(line);
		igc.getIntermediateGraph().addVertex(intermediate);
		line.setTarget(intermediate);
		
		//now, we just add this into the graph.
		igc.getIntermediateGraph().addEdge(line, intermediate);
	}

	@Override
	public void visitBiConditionalLine(BooleanBranchIntermediate line) {
		InstructionHandle next = line.getInstruction().getNext();
		
		//find how that actually maps to the abstract line..
		AbstractIntermediate nextIntermediate = ilc.getNext(next.getPosition());
		//now, we just add this into the graph.

		BranchHandle bi = ((BranchHandle)line.getInstruction());
		AbstractIntermediate targetIntermediate = ilc.getNext(bi.getTarget().getPosition());
		
		igc.getIntermediateGraph().addVertex(nextIntermediate);
		igc.getIntermediateGraph().addEdge(line, nextIntermediate);

		//also add the target.

		if(targetIntermediate == null) {
			System.out.println(line);
		}
		

		igc.getIntermediateGraph().addVertex(targetIntermediate);
		igc.getIntermediateGraph().addEdge(line, targetIntermediate);
	}
	
	@Override
	public void visitMultiConditionalLine(MultiBranchIntermediate line) {
	
	}

	@Override
	public void visitAbstractLine(AbstractIntermediate line) {
		//add the vertex.
		igc.getIntermediateGraph().addVertex(line);
	}
}
