package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Increment;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class WhileToForLoopIncrement extends GraphIntermediateVisitor {

	public WhileToForLoopIncrement(IntermediateGraphContext igc) {
		super(igc, false);
	}

	@Override
	public void visitWhileIntermediate(WhileIntermediate line) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getGraph(), line);
		
		//look at the predecessor lines;  validate there are only 2 predecessors.
		if(predecessors.size() == 2) {
			StatementIntermediate declaration = null;
			StatementIntermediate iteration = null;
			
			for(AbstractIntermediate predecessor : predecessors) {
				if(comparator.before(predecessor, line)) {
					//should be declaration.
					
					if(predecessor instanceof StatementIntermediate) {
						declaration = (StatementIntermediate)predecessor;
						continue;
					}
					else {
						//the line before the while should be a statement if this is
						//a for loop.
						return;
					}
				}
				
				//if the 
				if(comparator.after(predecessor, line)) {
					
					if(predecessor instanceof StatementIntermediate) {
						iteration = (StatementIntermediate)predecessor;
					}
					else {
						return;
					}
				}
			}
			
			//at this point, both should be set.
			if(declaration != null && iteration != null) {
				//now, check the expression and validate these are correct via AST.
				
				if(declaration.getExpression() instanceof Declaration) {
					Declaration declarationExpression = (Declaration)declaration.getExpression();
					
					if(iteration.getExpression() instanceof Increment) {
						Increment incrementExpression = (Increment)iteration.getExpression();
						
						if(incrementExpression.getVariable().getType().equals(declarationExpression.getVariable().getType())) {
							
							//now check names.
							if(incrementExpression.getVariable().getName().equals(declarationExpression.getVariable().getName())) {
								//we can actually convert this to a for loop.
								ForIntermediate forIntermediate = new ForIntermediate(line, declarationExpression, incrementExpression);
								forIntermediate.setTrueBranch(line.getTrueBranch());
								forIntermediate.setFalseBranch(line.getFalseBranch());
								
								igc.getGraph().addVertex(forIntermediate);
								
								igc.redirectSuccessors(line, forIntermediate);
								igc.redirectPredecessors(iteration, forIntermediate);
								igc.redirectPredecessors(declaration, forIntermediate);
								
								
								//remove the while loop, increment, and declaration.
								igc.getGraph().removeVertex(line);
								igc.getGraph().removeVertex(declaration);
								igc.getGraph().removeVertex(iteration);
							}
							
							
						}
					}
					
					
				}
			}
			
		}
		
	}
	
}
