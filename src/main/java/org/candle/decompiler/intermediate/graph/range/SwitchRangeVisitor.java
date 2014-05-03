package org.candle.decompiler.intermediate.graph.range;

import java.util.Set;
import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CaseComparator;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.edge.SwitchEdge;

public class SwitchRangeVisitor extends GraphIntermediateVisitor {

	public SwitchRangeVisitor(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitSwitchIntermediate(SwitchIntermediate line) {
		boolean foundUpper = false;
		
		SwitchEdge maxEdge = findMaxCase(line);
		AbstractIntermediate lastNode = maxEdge.getTargetIntermediate();
		TreeSet<AbstractIntermediate> elements = (TreeSet<AbstractIntermediate>)igc.getOrderedIntermediate().subSet(line, true, lastNode, false);
		
		int position = lastNode.getInstruction().getPosition();
		
		//look for goto statements...
		for(AbstractIntermediate element : elements) {
			if(element instanceof GoToIntermediate) {
				GoToIntermediate gti = (GoToIntermediate)element;
				
				if(igc.getTarget(gti).getInstruction().getPosition() > position) {
					line.getBlockRange().setEnd(igc.getTarget(gti).getInstruction().getPrev());
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
	
	protected SwitchEdge findMaxCase(SwitchIntermediate line) {
		TreeSet<Case> cases = new TreeSet<Case>(new CaseComparator());
		
		Set<SwitchEdge> scs = igc.getCases(line);
		
		for(SwitchEdge sc : scs) {
			cases.add(sc.getSwitchCase());
		}
		Case max = cases.last();
		
		for(SwitchEdge sc : scs) {
			if(max == sc.getSwitchCase()) {
				return sc;
			}
		}
		
		return null;
	}

}
