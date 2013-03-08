package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.traverse.BreadthFirstIterator;

public class RetractDuplicateFinally extends GraphIntermediateVisitor {

	private static final Log LOG = LogFactory.getLog(RetractDuplicateFinally.class);
	
	public RetractDuplicateFinally(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitFinallyIntermediate(FinallyIntermediate line) {
		//TODO: Remove the highest finally statements first; won't fit this visitor
		//pattern.
		
		
		//get the bounds of the finally... associate the try.
		
		//finally is part of the try if the try + catch bounds has:
		//Try[0,13]
		//Catch[19,50]
		//Catch[56,65]
		//Finally | Handler[56, 65]| Handler[0, 42] |  | Range[66, 76]
		
		//in the case above, finally should match because lower bound == finally lower handler bound.
		//and upper bound of Catch matches upper bound of Finally Handler.
		
		//first, match the Try block that applies...
		
		//get lowest bound.
		InstructionHandle min = getLowestBound(line.getCodeExceptions());
		InstructionHandle max = getHighestBound(line.getCodeExceptions());
		
		LOG.debug("Finally Range: "+min.getPosition() + " -> "+max.getPosition());
		
		TryIntermediate matched = matchTryBlock(min, max);
		if(matched != null) {
			final Set<Integer> offsets = collectOffsets(line);
			//ok, now we need to eliminate finally blocks from this try and all of the catches.  thanks Java!
			List<CatchIntermediate> catchClauses = igc.getCatchClauses(matched);
			//for each catch clause...
			for(CatchIntermediate catchClause : catchClauses) {
				processCatch(catchClause, line, offsets);
			}
			
			processTry(matched, line, offsets);
		}
		
		//now, add the edge between end of FINALLY and the next statement.
		InstructionHandle finallyEnd = line.getBlockRange().getEnd();
		//get the next.. then search for next node in graph.
		
		AbstractIntermediate finallyLast = igc.findNextNode(finallyEnd);
		AbstractIntermediate afterFinally = igc.findNextNode(finallyEnd.getNext());
		
		igc.redirectPredecessors(finallyLast, afterFinally);
		igc.getIntermediateGraph().removeVertex(finallyLast);
	}
	
	protected void processTry(TryIntermediate tryIntermediate, FinallyIntermediate finallyIntermediate, Set<Integer> offsets) {
		//ok, now let's handle the try...
		InstructionHandle end = tryIntermediate.getBlockRange().getEnd();
		//next should be GOTO.
		AbstractIntermediate tryEndNode = igc.findNextNode(end);
		
		AbstractIntermediate gotoIntermediate = null;
		//check to see if this is loop...
		if(tryEndNode instanceof StatementIntermediate) {
			LOG.debug("Position: "+tryEndNode.getInstruction().getPosition()+" Value: "+tryEndNode);
			gotoIntermediate = igc.getSingleSuccessor(tryEndNode);
		}
		else if(tryEndNode instanceof BooleanBranchIntermediate) {
			BooleanBranchIntermediate bbi = (BooleanBranchIntermediate)tryEndNode;
			
			//find higher target...
			AbstractIntermediate trueTarget = igc.getTrueTarget(bbi);
			AbstractIntermediate falseTarget = igc.getFalseTarget(bbi);
			
			int trueTargetPosition = trueTarget.getInstruction().getPosition();
			int falseTargetPosition = falseTarget.getInstruction().getPosition();
			
			gotoIntermediate = (trueTargetPosition > falseTargetPosition) ? trueTarget : falseTarget; 
		}
		
		//validate it is a GOTO.
		if(!(gotoIntermediate instanceof GoToIntermediate)) {
			LOG.warn("Expected GOTO.  But this isn't: "+gotoIntermediate);
		}
		else {
			AbstractIntermediate tryFinallyFirst = igc.getSingleSuccessor(gotoIntermediate);
			eliminateNode(tryFinallyFirst, offsets);
			
			//now, eliminate the GOTO.
			igc.getIntermediateGraph().removeVertex(gotoIntermediate);
			igc.getIntermediateGraph().addEdge(tryIntermediate, finallyIntermediate);
		}
	}
	
	protected void processCatch(CatchIntermediate catchIntermediate, FinallyIntermediate finallyIntermediate, Set<Integer> offsets) {
		for(CodeExceptionGen ceg : finallyIntermediate.getCodeExceptions()) {
			int position = ceg.getEndPC().getPosition();
			if(catchIntermediate.getBlockRange().containsNumber(position))  {
				LOG.debug("Relevant: "+position);
				
				//we found the relevant position, now we need to find the next...
				AbstractIntermediate lastNode = igc.findNextNode(ceg.getEndPC());
				
				AbstractIntermediate finallyStart = igc.getSingleSuccessor(lastNode);
				LOG.debug("Finally start: "+finallyStart);
				
				//start eliminating nodes from this point.
				eliminateNode(finallyStart, offsets);
			}
		}
		
		
	}
	
	protected Set<Integer> collectOffsets(AbstractIntermediate line) {
		//this is the finally template.
		AbstractIntermediate first = igc.getSingleSuccessor(line);
		//now, store the instruction position.
		int position = first.getInstruction().getPosition();
		
		BreadthFirstIterator<AbstractIntermediate, IntermediateEdge> bfi = new BreadthFirstIterator<AbstractIntermediate, IntermediateEdge>(igc.getIntermediateGraph(), first);
		
		Set<Integer> offsets = new TreeSet<Integer>();
		while(bfi.hasNext()) {
			AbstractIntermediate next = bfi.next();
			int nextPosition = next.getInstruction().getPosition();
			
			int offset = nextPosition - position;
			LOG.debug("Offset: "+offset);
			offsets.add(offset);
		}
		
		return offsets;
	}
	
	protected void eliminateNode(AbstractIntermediate first, Set<Integer> offsets) {
		InstructionHandle ih = first.getInstruction();
		int position = first.getInstruction().getPosition();
	
		for(Integer offset : offsets) {
			int target = position + offset;
			
			ih = igc.getInstructionHandle(target);
			if(ih == null) {
				LOG.warn("Not found expected InstructionHandle: "+(position+offset));
				continue;
			}
			
			//loop through...
			AbstractIntermediate ai = igc.findNextNode(ih);
			if(ai.getInstruction().getPosition() == target) {
				igc.getIntermediateGraph().removeVertex(ai);
			}
			else {
				LOG.warn("Did not find vertex: "+target);
			}
		}
	}
	
	
	
	protected TryIntermediate matchTryBlock(InstructionHandle min, InstructionHandle max) {
		LinkedList<TryIntermediate> matches = new LinkedList<TryIntermediate>();
		//find the try block...
		for(AbstractIntermediate ai : igc.getIntermediateGraph().vertexSet()) {
			if(ai instanceof TryIntermediate) {
				TryIntermediate tryIntermediate = ((TryIntermediate) ai);
				LOG.debug("Finally: "+tryIntermediate+ " , "+tryIntermediate.getInstruction().getPosition()+" , "+tryIntermediate.getBlockRange().getStart());
				if(tryIntermediate.getBlockRange().getStart().getPosition() == min.getPosition()) {
					
					//only add where max > try's max range...
					if(tryIntermediate.getBlockRange().getEnd().getPosition() < max.getPosition()) {
						matches.add(tryIntermediate);
					}
				}
			}
		}
		
		//return the smaller range...
		if(matches.size() > 0) {
			Collections.sort(matches, new Comparator<TryIntermediate>() {
				@Override
				public int compare(TryIntermediate t1, TryIntermediate t2) {
					if(t1.getBlockRange().getEnd().getPosition() > t2.getBlockRange().getEnd().getPosition()) {
						return -1;
					}
					return 0;
				}
			});
			LOG.debug("Match: "+matches.peekFirst()+" Range: "+matches.peekFirst().getBlockRange());
			return matches.getFirst();
		}
		
		return null;
	}

	protected InstructionHandle getHighestBound(Collection<CodeExceptionGen> cegs) {
		InstructionHandle highest = null;
		for(CodeExceptionGen ceg : cegs) {
			if(highest != null) {
				if(ceg.getEndPC().getPosition() > highest.getPosition()) {
					highest = ceg.getEndPC();
				}
			}
			else {
				highest = ceg.getEndPC();
			}
		}
		
		return highest;
	}
	
	protected InstructionHandle getLowestBound(Collection<CodeExceptionGen> cegs) {
		InstructionHandle lowest = null;
		for(CodeExceptionGen ceg : cegs) {
			if(lowest != null) {
				if(ceg.getStartPC().getPosition() < lowest.getPosition()) {
					lowest = ceg.getStartPC();
				}
			}
			else {
				lowest = ceg.getStartPC();
			}
		}
		
		return lowest;
	}
}
