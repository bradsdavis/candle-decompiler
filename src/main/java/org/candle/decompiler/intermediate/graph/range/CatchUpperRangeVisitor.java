package org.candle.decompiler.intermediate.graph.range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.IntermediateComparator;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.expression.Return;
import org.candle.decompiler.intermediate.expression.Throw;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;
import org.jgrapht.traverse.BreadthFirstIterator;

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

	private static final Log LOG = LogFactory.getLog(CatchUpperRangeVisitor.class);
	
	public CatchUpperRangeVisitor(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitCatchIntermediate(CatchIntermediate line) {
		//first, check if the line has an end already..
		if(line.getBlockRange().getEnd()!=null) {
			return;
		}
		//processLastCatch(line);
		
		BreadthFirstIterator<AbstractIntermediate, IntermediateEdge> bfi = new BreadthFirstIterator<AbstractIntermediate, IntermediateEdge>(igc.getGraph(), line);
		
		AbstractIntermediate lastStatement = null;
		while(bfi.hasNext()) {
			AbstractIntermediate next = bfi.next();
			if(next instanceof GoToIntermediate) {
				//this would be a possible GOTO... find previous.
				LOG.debug("Catch GOGO: "+next+" goto:"+next.getInstruction().getPosition());
				lastStatement = igc.getSinglePredecessor(next);
				break;
			}
			if(next instanceof StatementIntermediate) {
				//determine what type of statement...
				if(((StatementIntermediate) next).getExpression() instanceof Throw) {
					lastStatement = next;
					break;
				}
				
				if(((StatementIntermediate) next).getExpression() instanceof Return) {
					lastStatement = next;
					break;
				}
			}
			
		}
		
		if(lastStatement != null) {
			line.getBlockRange().setEnd(lastStatement.getInstruction());
		}
	}
	
	public void processLastCatch(CatchIntermediate line) {
		TryIntermediate tryBlock = (TryIntermediate)igc.getSinglePredecessor(line);
		if(igc.getFinallyClause(tryBlock) != null) {
			return;
		}
		
		//now, we are going to look for the block jump.
		InstructionHandle ih = tryBlock.getBlockRange().getEnd();
		//see about a goto.
		GoToIntermediate gotoHandle = (GoToIntermediate)igc.findNextNode(ih.getNext());
		TreeSet<AbstractIntermediate> ordered = new TreeSet<AbstractIntermediate>(new IntermediateComparator());
		//no finally clause...
		ordered.addAll(igc.getCatchClauses(tryBlock));
		
		AbstractIntermediate target = gotoHandle.getTarget();
		//now, look backwards and find the non-GOTO statement.
		
		List<AbstractIntermediate> candidates = Graphs.predecessorListOf(igc.getGraph(), target);
		Set<AbstractIntermediate> elements = new HashSet<AbstractIntermediate>();
		
		for(AbstractIntermediate candidate : candidates) {
			if(!(candidate instanceof GoToIntermediate)) {
				elements.add(candidate);
			}
		}
		
		for(AbstractIntermediate element : elements) {
			LOG.debug("Element: "+element+" Position: "+element.getInstruction().getPosition());
		}
		
		if(elements.size() == 1) {
			if(ordered.last() instanceof CatchIntermediate) {
				CatchIntermediate ci = (CatchIntermediate)ordered.last();
				ci.getBlockRange().setEnd(elements.iterator().next().getInstruction());
			}
		}
	}
}
