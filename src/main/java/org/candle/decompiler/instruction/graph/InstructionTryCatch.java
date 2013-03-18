package org.candle.decompiler.instruction.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.InstructionRange;
import org.apache.bcel.generic.InstructionHandle;

public class InstructionTryCatch {

	private static final Log LOG = LogFactory.getLog(InstructionTryCatch.class);
	
	private final CodeExceptionGen[] exceptions;
	private final InstructionGraphContext igc;
	
	public InstructionTryCatch(InstructionGraphContext igc, CodeExceptionGen[] exceptions) {
		this.igc = igc;
		this.exceptions = exceptions;
	}
	
	public void process() {
		
		Map<InstructionRange, List<CodeExceptionGen>> tryRangeGen = new HashMap<InstructionRange, List<CodeExceptionGen>>();
		
		//create try statements...
		for(CodeExceptionGen ceg : exceptions) {
			InstructionHandle min = (ceg.getStartPC());
			InstructionHandle max = (ceg.getEndPC());
			
			InstructionRange tryRange = new InstructionRange();
			tryRange.setStart(min);
			tryRange.setEnd(max);
			
			//SKIP THE FINALLY
			if(ceg.getCatchType() == null) {
				continue;
			}
			
			if(!tryRangeGen.containsKey(tryRange)) {
				tryRangeGen.put(tryRange, new LinkedList<CodeExceptionGen>());
			}				
			tryRangeGen.get(tryRange).add(ceg);
		}
	}

	
	
	
}
