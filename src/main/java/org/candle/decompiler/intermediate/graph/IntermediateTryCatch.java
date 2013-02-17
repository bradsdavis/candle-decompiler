package org.candle.decompiler.intermediate.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.candle.decompiler.ast.trycatch.TryBlock;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.context.NullIntermediate;

public class IntermediateTryCatch {

	private final MethodGen method;
	private final IntermediateGraphContext igc;
	
	public IntermediateTryCatch(MethodGen method, IntermediateGraphContext igc) {
		this.method = method;
		this.igc = igc;
	}
	
	
	public void process() {
		Map<BlockRange, List<CodeExceptionGen>> tryRangeGen = new HashMap<BlockRange, List<CodeExceptionGen>>();
		//create try statements...
		for(CodeExceptionGen ceg : method.getExceptionHandlers()) {
			InstructionHandle min = ceg.getStartPC();
			InstructionHandle max = ceg.getEndPC();
			
			BlockRange tryRange = new BlockRange();
			tryRange.setStart(min);
			tryRange.setEnd(max);
			
			System.out.println("Range: "+tryRange);
			if(!tryRangeGen.containsKey(tryRange)) {
				tryRangeGen.put(tryRange, new LinkedList<CodeExceptionGen>());
			}				
			tryRangeGen.get(tryRange).add(ceg);
		}
		
		for(BlockRange tryRange : tryRangeGen.keySet()) {
			//create try block... create each catch block... link the two together for graph sake.
			//look up block...
			InstructionHandle start = tryRangeGen.get(tryRange).get(0).getStartPC();
			TryIntermediate tryIntermediate = new TryIntermediate(start);
			tryIntermediate.getBlockRange().setStart(tryRange.getStart());
			tryIntermediate.getBlockRange().setEnd(tryRange.getEnd());
			
			
			igc.getIntermediateGraph().addVertex(tryIntermediate);
			
			//add line between try and node.
			AbstractIntermediate tryFirst = igc.findNextNode(start);
			igc.redirectPredecessors(tryFirst, tryIntermediate);
			igc.getIntermediateGraph().addEdge(tryIntermediate, tryFirst);
			
			
			//create catch statements...
			for(CodeExceptionGen ceg : tryRangeGen.get(tryRange)) {
				System.out.println("CEG: "+ceg);
				
				
				//convert the node to catch blocks...
				AbstractIntermediate catchDeclaration = igc.getOrderedIntermediate().ceiling(new NullIntermediate(ceg.getHandlerPC()));
				
				System.out.println("Catch Declaration:"+catchDeclaration);
				
				if(catchDeclaration instanceof StatementIntermediate) {
					StatementIntermediate declarationStatement = (StatementIntermediate)catchDeclaration;
					if(declarationStatement.getExpression() instanceof Declaration) {
						Declaration declaration = (Declaration)declarationStatement.getExpression();
						
						//now, we can convert this into a catch block.
						CatchIntermediate catchIntermediate = new CatchIntermediate(declarationStatement.getInstruction(), declaration.getVariable());
						igc.getIntermediateGraph().addVertex(catchIntermediate);
						
						//redirect statement to catch.
						igc.redirectPredecessors(declarationStatement, catchIntermediate);
						igc.redirectSuccessors(declarationStatement, catchIntermediate);
						
						//now, we just need to remove the statement.
						igc.getIntermediateGraph().removeVertex(declarationStatement);
						
						//populate the bounds..
						
						
						//add the link between try and catch.
						igc.getIntermediateGraph().addEdge(tryIntermediate, catchIntermediate);
					}
					
				}
			}
			
		}
	}
	
	
	
}
