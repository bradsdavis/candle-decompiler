package org.candle.decompiler.intermediate.graph;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Select;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchOutcome;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.expression.DefaultCase;
import org.candle.decompiler.intermediate.expression.Resolved;
import org.candle.decompiler.intermediate.expression.Return;
import org.candle.decompiler.intermediate.expression.Throw;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;
import org.jgrapht.graph.ListenableDirectedGraph;

public class IntermediateGraphFactory extends EmptyIntermediateVisitor {

	private static final Log LOG = LogFactory.getLog(IntermediateGraphFactory.class);
	
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
	public void visitStatementIntermediate(StatementIntermediate line) {
		AbstractIntermediate next = ilc.getNext(line);
		
		//check to see if it is a return statement.
		if(line.getExpression() instanceof Return) {
			//don't add a line to next.
			return;
		}
		if(line.getExpression() instanceof Throw) {
			//don't add a line to next.
			return;
		}
		
		if(next != null) {
			//find how that actually maps to the abstract line..
			AbstractIntermediate intermediate = next;
			
			//now, we just add this into the graph.
			igc.getGraph().addVertex(intermediate);
			igc.getGraph().addEdge(line, intermediate);
		}
	}

	@Override
	public void visitGoToIntermediate(GoToIntermediate line) {
		
		//find how that actually maps to the abstract line..
		AbstractIntermediate intermediate = ilc.getNext(line);
		igc.getGraph().addVertex(intermediate);
		line.setTarget(intermediate);
		
		//now, we just add this into the graph.
		igc.getGraph().addEdge(line, intermediate);
	}

	@Override
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line) {
		InstructionHandle next = line.getInstruction().getNext();
		
		//find how that actually maps to the abstract line..
		AbstractIntermediate nextIntermediate = ilc.getNext(next.getPosition());
		igc.getGraph().addVertex(nextIntermediate);
		
		BranchHandle bi = ((BranchHandle)line.getInstruction());
		AbstractIntermediate targetIntermediate = ilc.getNext(bi.getTarget().getPosition());
		igc.getGraph().addVertex(targetIntermediate);
		
		AbstractIntermediate lowest = targetIntermediate.getInstruction().getPosition() < nextIntermediate.getInstruction().getPosition() ? targetIntermediate : nextIntermediate;
		AbstractIntermediate highest = targetIntermediate.getInstruction().getPosition() > nextIntermediate.getInstruction().getPosition() ? targetIntermediate : nextIntermediate;
		
		
		
		//add true path... (Conditional) -> (True) -> (Node A)
		BooleanBranchOutcome trueOutcome = new BooleanBranchOutcome(line.getInstruction(), line, Boolean.TRUE);
		line.setTrueBranch(trueOutcome);
		igc.getGraph().addVertex(trueOutcome);
		igc.getGraph().addEdge(line, trueOutcome);
		igc.getGraph().addEdge(trueOutcome, lowest);
		
		
		//add false path... (Conditional) -> (False) -> (Node A)
		BooleanBranchOutcome falseOutcome = new BooleanBranchOutcome(line.getInstruction(), line, Boolean.FALSE);
		line.setFalseBranch(falseOutcome);
		igc.getGraph().addVertex(falseOutcome);
		igc.getGraph().addEdge(line, falseOutcome);
		igc.getGraph().addEdge(falseOutcome, highest);
	}
	
	@Override
	public void visitMultiBranchIntermediate(MultiBranchIntermediate line) {
		
		Select select = (Select)line.getInstruction().getInstruction();
		
		Set<Case> cases = new HashSet<Case>();
		InstructionHandle[] handles = select.getTargets();
		int[] matches = select.getMatchs();
		
		for(int i=0, j=handles.length; i<j; i++) {
			InstructionHandle ih = handles[i];
			int match = matches[i];

			Resolved resolved = new Resolved(line.getInstruction(), BasicType.INT, ""+match);
			Case caseEntry = new Case(line.getInstruction(), ih, resolved);
			cases.add(caseEntry);
		}
		
		if(select.getTarget()!=null) {
			DefaultCase defaultCase = new DefaultCase(line.getInstruction(), select.getTarget());
			line.setDefaultCase(defaultCase);
		}
		
		//now, create the graph.
		line.setCases(cases);
		
		//now, create the graph.
		
		igc.getGraph().addVertex(line);
		if(line.getDefaultCase() != null) {
			CaseIntermediate si = new CaseIntermediate(line.getInstruction(), line.getDefaultCase());
			igc.getGraph().addVertex(si);
			
			//add an edge.
			igc.getGraph().addEdge(line, si);

			//add edge from outcome to edge.
			LOG.debug(si);
			AbstractIntermediate target = ilc.getNext(line.getDefaultCase().getTarget().getPosition());
			
			LOG.debug("TargeT:"+target);
			igc.getGraph().addVertex(target);
			igc.getGraph().addEdge(si, target);
		}
		
		
		for(Case caseVal : line.getCases()) {
			CaseIntermediate si = new CaseIntermediate(line.getInstruction(), caseVal);
			igc.getGraph().addVertex(si);
			
			//add an edge.
			igc.getGraph().addEdge(line, si);
			
			//add edge from outcome to edge.
			AbstractIntermediate target = ilc.getNext(caseVal.getTarget().getPosition());
			igc.getGraph().addVertex(target);
			igc.getGraph().addEdge(si, target);
		}
	}

	@Override
	public void visitAbstractIntermediate(AbstractIntermediate line) {
		//add the vertex.
		igc.getGraph().addVertex(line);
	}
}
