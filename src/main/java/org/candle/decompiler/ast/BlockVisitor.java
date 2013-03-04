package org.candle.decompiler.ast;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.conditional.ElseBlock;
import org.candle.decompiler.ast.conditional.ElseIfBlock;
import org.candle.decompiler.ast.conditional.IfBlock;
import org.candle.decompiler.ast.loop.EnhancedForBlock;
import org.candle.decompiler.ast.loop.ForBlock;
import org.candle.decompiler.ast.loop.WhileBlock;
import org.candle.decompiler.ast.swtch.SwitchBlock;
import org.candle.decompiler.ast.swtch.SwitchCaseBlock;
import org.candle.decompiler.ast.tcf.CatchBlock;
import org.candle.decompiler.ast.tcf.FinallyBlock;
import org.candle.decompiler.ast.tcf.TryBlock;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchOutcome;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

/***
 * Walks the graph and creates Blocks from Intermediates.
 * 
 * @author bradsdavis
 *
 */
public class BlockVisitor extends GraphIntermediateVisitor {
	private static final Log LOG = LogFactory.getLog(BlockVisitor.class);
	
	private final Set<AbstractIntermediate> seen = new HashSet<AbstractIntermediate>();
	private AbstractIntermediate start;
	private Block current;
	
	public BlockVisitor(IntermediateGraphContext igc, MethodBlock method) {
		super(igc);
		this.current = method;
	}
	
	
	
	public void process() {
		if(igc.getOrderedIntermediate().size() < 1) {
			return;
		}
		
		this.start = igc.getOrderedIntermediate().first();
		this.start.accept(this);
	}

	
	
	@Override
	public void visitAbstractIntermediate(AbstractIntermediate line) {
		while(!current.within(line.getInstruction())) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("Line: "+ReflectionToStringBuilder.toString(line)+" not within: "+ReflectionToStringBuilder.toString(current));
			}
			moveUp();
		}
	}

	
	@Override
	public void visitEnhancedForLoopIntermediate(EnhancedForIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}
		
		EnhancedForBlock efb = new EnhancedForBlock(line);
		current.addChild(efb);
		current = efb;
		
		processBoolean(efb, line);
	}
	
	@Override
	public void visitForIntermediate(ForIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}
		
		ForBlock forBlock = new ForBlock(line);
		current.addChild(forBlock);
		current = forBlock;
	
		processBoolean(forBlock, line);
	}
	
	@Override
	public void visitWhileIntermediate(WhileIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		WhileBlock whileBlock = new WhileBlock(line);
		current.addChild(whileBlock);
		current = whileBlock;
	
		processBoolean(whileBlock, line);
	}
	
	@Override
	public void visitStatementIntermediate(StatementIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		//create the block from the statement.
		StatementBlock statement = new StatementBlock(line);
		current.addChild(statement);
		
		//now, visit the successor, if any.
		List<AbstractIntermediate> candidates = getUnseenSuccessors(line);
		
		if(candidates.size() > 0) {
			for(AbstractIntermediate candidate : candidates) {
				//move to the next.
				candidate.accept(this);
			}
		}
	}
	
	@Override
	public void visitSwitchIntermediate(SwitchIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		SwitchBlock switchBlock = new SwitchBlock(line);
		current.addChild(switchBlock);
		current = switchBlock;
		
		List<CaseIntermediate> cases = igc.getCases(line);
		
		for(CaseIntermediate switchCase : cases) {
			switchCase.accept(this);
			
			//reset current to switch
			current = switchBlock;
		}
	}
	
	@Override
	public void visitCaseIntermediate(CaseIntermediate line) {
		SwitchCaseBlock switchCaseBlock = new SwitchCaseBlock(line);
		current.addChild(switchCaseBlock);
		current = switchCaseBlock;


		//now, visit the successor, if any.
		List<AbstractIntermediate> candidates = getUnseenSuccessors(line);
		
		if(candidates.size() > 0) {
			for(AbstractIntermediate candidate : candidates) {
				//move to the next.
				candidate.accept(this);
			}
		}
	}
	
	
	@Override
	public void visitElseIntermediate(ElseIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}
		
		ElseBlock elseBlock = new ElseBlock(line);
		current.addChild(elseBlock);
		current = elseBlock;
		
		//now, visit the successor, if any.
		List<AbstractIntermediate> candidates = getUnseenSuccessors(line);
		
		if(candidates.size() > 0) {
			for(AbstractIntermediate candidate : candidates) {
				//move to the next.
				candidate.accept(this);
			}
		}
		moveUp();
	}
	
	protected void processBoolean(Block block, BooleanBranchIntermediate bbi) {
		List<AbstractIntermediate> successors = getUnseenSuccessors(bbi);
		//assign true... and go through true.
		
		BooleanBranchOutcome trueOutcome = null;
		BooleanBranchOutcome falseOutcome = null;
		for(AbstractIntermediate successor : successors) {
			if(successor instanceof BooleanBranchOutcome) {
				if(((BooleanBranchOutcome) successor).getExpressionOutcome() == Boolean.TRUE) {
					trueOutcome = (BooleanBranchOutcome)successor;
				}
				else {
					falseOutcome = (BooleanBranchOutcome)successor;
				}
			}
			else {
				throw new IllegalStateException("Outcome of If expected to be boolean.");
			}
		}
		
		trueOutcome.accept(this);
		
		//now, go back to if...
		this.current = block;
		falseOutcome.accept(this);

		if(this.current == block) {
			moveUp();
		}
	}
	
	@Override
	public void visitTryIntermediate(TryIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		//set current block...
		TryBlock tryBlock = new TryBlock(line);
		
		//add it as a child of current..
		this.current.addChild(tryBlock);
		
		//set the current...
		this.current = tryBlock;
		
		//now, get the nested blocks...
		List<AbstractIntermediate> successors = getUnseenSuccessors(line);
		
		AbstractIntermediate inner = null;
		List<AbstractIntermediate> catchBlocks = new LinkedList<AbstractIntermediate>();
		AbstractIntermediate finallyIntermediate = null;
		
		//find the non-catch/finally...
		for(AbstractIntermediate successor : successors) {
			if(successor instanceof CatchIntermediate) {
				catchBlocks.add(successor);
			}
			else if(successor instanceof FinallyIntermediate) {
				finallyIntermediate = successor;
			}
			else {
				if(inner != null) {
					throw new IllegalStateException("Inner direction already set.");
				}
				inner = successor;
			}
		}
		Collections.sort(catchBlocks, new IntermediateComparator());
		
		if(inner == null) {
			throw new IllegalStateException("Inner is not set.");
		}
		
		inner.accept(this);
		
		//set the current up.
		moveUp();
		for(AbstractIntermediate catchBlock : catchBlocks) {
			catchBlock.accept(this);
		}
		
		if(finallyIntermediate != null) {
			finallyIntermediate.accept(this);
		}
	}
	
	@Override
	public void visitCatchIntermediate(CatchIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		
		CatchBlock catchBlock = new CatchBlock(line);
		
		this.current.addChild(catchBlock);
		this.current = catchBlock;
		
		//children...
		List<AbstractIntermediate> successors = getUnseenSuccessors(line);
		for(AbstractIntermediate successor : successors) {
			successor.accept(this);
		}
		
		if(this.current == catchBlock) {
			moveUp();
		}
	}
	
	@Override
	public void visitFinallyIntermediate(FinallyIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}


		FinallyBlock finallyBlock = new FinallyBlock(line);
		
		current.addChild(finallyBlock);
		this.current = finallyBlock;
		
		List<AbstractIntermediate> successors = getUnseenSuccessors(line);
		for(AbstractIntermediate successor : successors) {
			successor.accept(this);
		}

		if(this.current == finallyBlock) {
			moveUp();
		}
	}
	
	@Override
	public void visitElseIfIntermediate(ElseIfIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		ElseIfBlock elseIfBlock = new ElseIfBlock(line);
		current.addChild(elseIfBlock);
		this.current = elseIfBlock;
		
		List<AbstractIntermediate> successors = getUnseenSuccessors(line);
		//assign true... and go through true.
		
		BooleanBranchOutcome trueOutcome = null;
		BooleanBranchOutcome falseOutcome = null;
		for(AbstractIntermediate successor : successors) {
			if(successor instanceof BooleanBranchOutcome) {
				if(((BooleanBranchOutcome) successor).getExpressionOutcome() == Boolean.TRUE) {
					trueOutcome = (BooleanBranchOutcome)successor;
				}
				else {
					falseOutcome = (BooleanBranchOutcome)successor;
				}
			}
			else {
				throw new IllegalStateException("Outcome of If expected to be boolean.");
			}
		}
		
		trueOutcome.accept(this);
		
		//now, go back to if...
		this.current = elseIfBlock;
		falseOutcome.accept(this);

		if(this.current == elseIfBlock) {
			moveUp();
		}
		
	}
	
	@Override
	public void visitIfIntermediate(IfIntermediate line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}


		IfBlock ifBlock = new IfBlock(line);
		current.addChild(ifBlock);
		this.current = ifBlock;
		
		List<AbstractIntermediate> successors = getUnseenSuccessors(line);
		//assign true... and go through true.
		
		BooleanBranchOutcome trueOutcome = null;
		BooleanBranchOutcome falseOutcome = null;
		for(AbstractIntermediate successor : successors) {
			if(successor instanceof BooleanBranchOutcome) {
				if(((BooleanBranchOutcome) successor).getExpressionOutcome() == Boolean.TRUE) {
					trueOutcome = (BooleanBranchOutcome)successor;
				}
				else {
					falseOutcome = (BooleanBranchOutcome)successor;
				}
			}
			else {
				throw new IllegalStateException("Outcome of If expected to be boolean.");
			}
		}
		
		trueOutcome.accept(this);
		
		//now, go back to if...
		this.current = ifBlock;
		falseOutcome.accept(this);

		if(this.current == ifBlock) {
			moveUp();
		}
		
	}
	
	@Override
	public void visitBooleanBranchOutcome(BooleanBranchOutcome line) {
		if(seen.contains(line)) {
			//do nothing.
			return;
		}
		else {
			seen.add(line);
		}

		//process successors...
		List<AbstractIntermediate> successors = getUnseenSuccessors(line);
		for(AbstractIntermediate successor : successors) {
			successor.accept(this);
		}
	}
	
	protected List<AbstractIntermediate> getUnseenSuccessors(AbstractIntermediate line) {
		List<AbstractIntermediate> candidates = Graphs.successorListOf(igc.getIntermediateGraph(), line);
		return candidates;
	}
	
	protected void moveUp() {
		this.current = current.getParent();
	}
	
	
}
