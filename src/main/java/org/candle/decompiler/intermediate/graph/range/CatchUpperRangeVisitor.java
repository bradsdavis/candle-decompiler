package org.candle.decompiler.intermediate.graph.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.ast.trycatch.CatchBlock;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

/***
 * Sets the upper bounds of the catch blocks.
 * 
 * This is done by marking the catch block upper by ordering the catch blocks by
 * their instruction handle, and then all non-last catch blocks would end with the next
 * catch blocks instruction, non-inclusive. 
 * 
 * The last catch block is bound by the try-block's last statement's GOTO target.
 * 
 * @author bradsdavis
 *
 */
public class CatchUpperRangeVisitor extends GraphIntermediateVisitor {

	public CatchUpperRangeVisitor(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitTryIntermediate(TryIntermediate line) {
		List<AbstractIntermediate> candidates = Graphs.successorListOf(igc.getIntermediateGraph(), line);
		
		//now, order catch blocks by their instruction handle.
		//eliminate non-catch blocks.
		
		List<CatchIntermediate> catchBlocks = new ArrayList<CatchIntermediate>(candidates.size());
		for(AbstractIntermediate candidate : candidates) {
			if(!(candidate instanceof CatchIntermediate)) {
				continue;
			}
			//otherwise, add it to the catchBlocks.
			catchBlocks.add((CatchIntermediate)candidate);
		}
		Collections.sort(catchBlocks, new IntermediateComparator());

		//loop through.
		for(int i=0, j= catchBlocks.size(); i<j; i++) {

			//this is the last catch block.
			if(i == (j-1)) {
				InstructionHandle handle = findHandleFromTry(line);
				catchBlocks.get(i).getBlockRange().setEnd(handle);
			}
			else {
				//look to the next catch block... find the instruction.. and go back 1 instruction.
				CatchIntermediate next = catchBlocks.get(i+1);
				InstructionHandle prev = next.getInstruction().getPrev();
				
				catchBlocks.get(i).getBlockRange().setEnd(prev);
			}
			
		}
	}

	public InstructionHandle findHandleFromTry(TryIntermediate i) {
		//from the try block, find the last position...
		InstructionHandle end = i.getBlockRange().getEnd().getNext();
		//now, get the next...
		
		AbstractIntermediate next = igc.findNextNode(end);
		
		if(!(next instanceof GoToIntermediate)) {
			throw new IllegalStateException("Expected to be GOTO.");
		}
		GoToIntermediate gotoIntermediate = (GoToIntermediate)next;
		//now, find the target.  that is the end of the catch block.
		InstructionHandle ih = gotoIntermediate.getTarget().getInstruction();
		
		return igc.findPreviousNode(ih.getPrev()).getInstruction();
	}
}
