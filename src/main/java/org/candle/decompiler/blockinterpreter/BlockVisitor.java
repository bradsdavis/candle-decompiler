package org.candle.decompiler.blockinterpreter;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.Block;
import org.candle.decompiler.ast.Statement;
import org.candle.decompiler.ast.conditional.ElseBlock;
import org.candle.decompiler.ast.conditional.GotoBlock;
import org.candle.decompiler.ast.conditional.IfBlock;
import org.candle.decompiler.ast.loop.WhileBlock;
import org.candle.decompiler.ast.trycatch.CatchBlock;
import org.candle.decompiler.ast.trycatch.TryBlock;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateType;
import org.candle.decompiler.intermediate.visitor.EmptyIntermediateVisitor;

public class BlockVisitor extends EmptyIntermediateVisitor {
	private static final Log LOG = LogFactory.getLog(BlockVisitor.class);
	public final BlockContext context;
	
	public BlockVisitor(BlockContext context) {
		this.context = context;
	}

	@Override
	public void visitCompleteLine(StatementIntermediate line) {
		Statement statement = new Statement(context.getCurrentBlock(), line);
		context.getCurrentBlock().addChild(statement);
	}

	@Override
	public void visitGoToLine(GoToIntermediate line) {
		LOG.debug(line.getInstruction());
		
		//If we are in an if block and it's jumping forward, then this goto should really be an else.
		BranchHandle handle = (BranchHandle)line.getInstruction();
		if(handle.getPosition() < handle.getTarget().getPosition()) {
			//moving forward...
			
			
			if(context.getCurrentBlock() instanceof IfBlock
					&& handle.getTarget().getPosition() > context.getCurrentBlock().getEndBlockPosition()) {
				//TODO: this logic needs to be better tested.
				ElseBlock block = new ElseBlock(line);
				context.getCurrentBlock().addChild(block);
				context.setCurrentBlock(block);
				return;
			}
			

			if(context.getCatchBlockStart().containsKey(handle.getNext())) {
				LOG.info(context.getCatchBlockStart().get(handle.getNext()));
				
				if(context.getCurrentBlock() instanceof CatchBlock) {
					context.moveUp();
				}
				
				//oh great! this is a catch block.
				CatchBlock cb = new CatchBlock(handle.getNext(), handle.getTarget().getPrev());
				context.getCurrentBlock().addChild(cb);
				context.setCurrentBlock(cb);
				return;
				
			}
			
			/*
			//check the last statement.
			Block currentBlock = context.getCurrentBlock();
			Block candidateBlock = null;
			if(currentBlock.getChildren().size() > 0) {
				candidateBlock = currentBlock.getChildren().get(currentBlock.getChildren().size() -1);
			}
			
			LOG.info(candidateBlock);
			
			//see if we can join the block.
			if(candidateBlock instanceof TryBlock) {
				TryBlock tb = ((TryBlock)candidateBlock);
				
				//see if we have processed the catch.
				
				for(InstructionHandle ceg : context.getCatchBlockStart().keySet()) {
					
				}
			}*/
			
			
			//look back to see if the previous instruction was within the try block.
			//if it is currently the try block, and this is a go to that exists the try, do nothing to move the
			//current block out of the try.
			
			/*
			InstructionHandle ih = line.getInstruction().getPrev(); 
			for(CodeExceptionGen ceg : context.getProcessedExceptions()) {
				if(ceg.getEndPC() == ih) {
					LOG.info(ceg);
					LOG.info(ih);

					CatchBlock catchBlock = new CatchBlock(ih.getNext(), handle.getTarget());
					context.getCurrentBlock().addChild(catchBlock);
					context.setCurrentBlock(catchBlock);
					return;
				}
				
				
			}*/
			
			
			//Block targetBlock = context.findBlockByHandle(context.getCurrentBlock(), line.getInstruction().getPrev());
			
			
			//LOG.info(targetBlock);
			/*
				TryBlock tryBlock = (TryBlock)context.getCurrentBlock();
				
				//check if instruction is catch clause. 
				if(tryBlock.getCodeExceptionGen().getHandlerPC().equals(line.getInstruction())) {
					InstructionHandle endHandle = (((GotoInstruction)(line.getInstruction().getPrev().getInstruction())).getTarget().getPrev());
					
					CatchBlock catchBlock = new CatchBlock(line.getInstruction(), endHandle);
					context.getCurrentBlock().addChild(catchBlock);
					context.setCurrentBlock(catchBlock);
					
					return;
				}
			}*/
			
			
		}
		
		GotoBlock block = new GotoBlock((BranchHandle)line.getInstruction());
		context.getCurrentBlock().addChild(block);
		context.setCurrentBlock(block);
	}

	@Override
	public void visitBiConditionalLine(BooleanBranchIntermediate line) {
		//by default, set it as an if block.

		boolean addIf = true;
		
		//check the current pc; if the target is less than the current, then it probably is a for loop.
		BranchHandle bi = ((BranchHandle)line.getInstruction());
		

		//is this headed backgwards...
		if(bi.getPosition() > bi.getTarget().getPosition()) {
			//check to see whether the target's previous was a goto, and whether it pushes backwards.  Then, this indicates a loop.
			LOG.debug("Headed backwards.  likely a loop.");
			
			InstructionHandle ih = bi.getTarget().getPrev();
			LOG.debug("Handle: "+ih);
			context.setCurrentBlockByHandle(ih);
			
			Block gotoBlock = context.getCurrentBlock();
			//now, we need to convert the GOTO into a While loop.
			WhileBlock whileBlock = new WhileBlock(line);
			whileBlock.getChildren().addAll(gotoBlock.getChildren());
			
			//move up...
			context.moveUp();
			whileBlock.setParent(context.getCurrentBlock());
			context.getCurrentBlock().replaceChild(gotoBlock, whileBlock);
			context.setCurrentBlock(whileBlock);
			
			//don't add the if.
			return;
		}
		
		//check whether it is an OR statement.
		
		//if there are two IF blocks in a row...
		if(context.getCurrentBlock() instanceof IfBlock) {
			IfBlock currentIfBlock = (IfBlock)context.getCurrentBlock();
			
			//if they have the same targets, merge them.
			if(bi.getTarget().getPosition() == currentIfBlock.getTargetBlockPosition()) {
				//this is also an IF statement, but we don't need to negate it.
				
				ConditionalExpression existingCE = currentIfBlock.getConditional().getExpression();
				ConditionalExpression currentCE = line.getExpression();
				LogicalGateConditionalExpression lgce = new LogicalGateConditionalExpression(existingCE, currentCE, LogicalGateType.OR);

				BooleanBranchIntermediate ci = currentIfBlock.getConditional();
				ci.setExpression(lgce);
				
				//if you need to grow the parent, do so.
				if(bi.getTarget().getPosition() >= currentIfBlock.getEndBlockPosition())
				{
					currentIfBlock.setEndBlockPosition(bi.getTarget().getPosition());
					lgce.negate();
				}
				
				return;
			}
			
			//and the target of the candidate's if block == the current if block's target...
			if(bi.getNext().getPosition() == currentIfBlock.getTargetBlockPosition()) {
				LOG.info("This is an OR BLOCK!!!");

				//in this case, the target is currently not negated.
				if(bi.getNext().getPosition() < bi.getTarget().getPosition()) {
					//first, negate the expression.
					line.getExpression().negate();
					currentIfBlock.setTargetBlockPosition(bi.getNext().getPosition());
					
					//next, remove the existing if, and add the or condition.
					
					//grab both conditionalexpressions.
					ConditionalExpression existingCE = currentIfBlock.getConditional().getExpression();
					ConditionalExpression currentCE = line.getExpression();
					LogicalGateConditionalExpression lgce = new LogicalGateConditionalExpression(existingCE, currentCE, LogicalGateType.OR);

					BooleanBranchIntermediate ci = currentIfBlock.getConditional();
					ci.setExpression(lgce);
					
					//now, if the end of the current line is greater than the IF, then extend the IF.
					if(bi.getTarget().getPosition() > currentIfBlock.getEndBlockPosition())
					{
						currentIfBlock.setEndBlockPosition(bi.getTarget().getPosition());
						lgce.negate();
					}
					
					return;
				}
			}
			
		}
		

		

		
	
		IfBlock ifBlock = new IfBlock(line);
		context.getCurrentBlock().addChild(ifBlock);
		context.setCurrentBlock(ifBlock);
	}

	@Override
	public void visitAbstractLine(AbstractIntermediate line) {
		
		while(!context.getCurrentBlock().within(line.getInstruction())) {
			//move up.
			context.moveUp();
		}

		//inject the try block if possible.
		InstructionHandle current = null;
		//check to see if it is a try block.
		for(InstructionHandle eh : context.getTryBlockStart().keySet()) {
			CodeExceptionGen ceg = context.getTryBlockStart().get(eh);
			
			if(withinCodeExceptionBlock(ceg, line.getInstruction())) {
				if(!currentlyInCodeException(ceg, context.getCurrentBlock())) {
					LOG.info("If this isn't currently in the try block, make it so.");
					TryBlock tryBlock = new TryBlock(ceg);
					context.getCurrentBlock().addChild(tryBlock);
					context.setCurrentBlock(tryBlock);
					current = eh;
				}
			}
		}
		
		//already processed the exception handler.  remove it.
		if(current != null) {
			context.getTryBlockStart().remove(current);
		}
	}
	
	protected boolean withinCodeExceptionBlock(CodeExceptionGen ceg, InstructionHandle ih) {
		//check to see if it is within position.
		return (ih.getPosition() >= ceg.getStartPC().getPosition() && ih.getPosition() <= ceg.getEndPC().getPosition());
	}
	
	protected boolean currentPCInBoundsExclusive(int pc, int blockEnd) {
		return pc >= blockEnd;
	}
	
	protected boolean currentPCInBoundsInclusive(int pc, int blockEnd) {
		return pc > blockEnd;
	}
	
	protected boolean currentlyInCodeException(CodeExceptionGen eh, Block currentBlock) {
		boolean withinCodeException = false;
		
		if(currentBlock == null) {
			return false;
		}
		if(currentBlock instanceof TryBlock) {
			TryBlock tryBlock = (TryBlock) currentBlock;
			//need to recurse up.
			withinCodeException = tryBlock.getCodeExceptionGen().equals(eh);
		}
		else if(currentBlock instanceof CatchBlock) {
			TryBlock tryBlock = (TryBlock)((CatchBlock)currentBlock).getParent();
			withinCodeException = tryBlock.getCodeExceptionGen().equals(eh);
		}

		if(withinCodeException) {
			return withinCodeException;
		}
		
		//recurse up.
		return currentlyInCodeException(eh, currentBlock.getParent());
	}

	
}
