package org.candle.decompiler.intermediate.graph.range;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class CaseEndRangeIntermediateVisitor extends GraphIntermediateVisitor {

	public CaseEndRangeIntermediateVisitor(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitCaseIntermediate(CaseIntermediate line) {
		SwitchIntermediate si = (SwitchIntermediate)igc.getSinglePredecessor(line);

		List<CaseIntermediate> switchCases = igc.getCaseIntermediates(si);
		int position = line.getBlockRange().getStart().getPosition();
		CaseIntermediate next = findNextCase(switchCases, position);

		if(next != null) {
			line.getBlockRange().setEnd(next.getBlockRange().getStart().getPrev());
		}
		else {
			//we should get the end from the parent.
			line.getBlockRange().setEnd(si.getBlockRange().getEnd());
		}
	}
	
	protected CaseIntermediate findNextCase(List<CaseIntermediate> cases, int position) {
		
		for(AbstractIntermediate switchCase : cases) {
			CaseIntermediate sc = (CaseIntermediate)switchCase;
			
			if(sc.getBlockRange().getStart().getPosition() > position) {
				return (CaseIntermediate)switchCase;
			}
		}
		
		return null;
	}

}
