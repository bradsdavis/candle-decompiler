package org.candle.decompiler.intermediate.graph.range;

import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CaseComparator;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class SwitchRangeVisitor extends GraphIntermediateVisitor {

	public SwitchRangeVisitor(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitSwitchIntermediate(SwitchIntermediate line) {
		boolean foundUpper = false;
		
		AbstractIntermediate lastNode = igc.findNextNode(findMaxCase(line).getTarget());
		TreeSet<AbstractIntermediate> elements = (TreeSet<AbstractIntermediate>)igc.getOrderedIntermediate().subSet(line, true, lastNode, false);
		
		int position = lastNode.getInstruction().getPosition();
		
		//look for goto statements...
		for(AbstractIntermediate element : elements) {
			if(element instanceof GoToIntermediate) {
				GoToIntermediate gti = (GoToIntermediate)element;
				
				if(gti.getTarget().getInstruction().getPosition() > position) {
					line.getBlockRange().setEnd(gti.getTarget().getInstruction().getPrev());
					foundUpper = true;
					break;
				}
			}
		}
		
		if(!foundUpper) {
			//find the last node... and then get the next.
			line.getBlockRange().setEnd(lastNode.getInstruction());
		}
		
	}
	
	protected Case findMaxCase(SwitchIntermediate line) {
		TreeSet<Case> cases = new TreeSet<Case>(new CaseComparator());
		if(line.getCases()!=null) {
			cases.addAll(line.getCases());
		}
		if(line.getDefaultCase() != null) {
			cases.add(line.getDefaultCase());
		}
		
		return cases.last();
	}

}
