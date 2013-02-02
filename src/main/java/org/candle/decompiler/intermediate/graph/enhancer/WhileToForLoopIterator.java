package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.MethodInvocation;
import org.candle.decompiler.intermediate.expression.SingleConditional;
import org.candle.decompiler.intermediate.expression.Variable;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class WhileToForLoopIterator extends GraphIntermediateVisitor {
	private static final Log LOG = LogFactory.getLog(WhileToForLoopIterator.class);
	
	public WhileToForLoopIterator(DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph) {
		super(intermediateGraph);
	}

	@Override
	public void visitWhileLoopLine(WhileIntermediate line) {
		//check to see if the while loop is a method invocation of hasNext.
		
		if(line.getExpression() instanceof SingleConditional) {
			if(((SingleConditional)line.getExpression()).getLeft() instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation)((SingleConditional)line.getExpression()).getLeft();
				
				String iteratorName = null;
				if(mi.getTarget() instanceof Variable) {
					iteratorName = ((Variable)mi.getTarget()).getName();
				}
				else {
					return;
				}
				
				if(StringUtils.equals("hasNext", mi.getMethodName())) {
					
					//probably an iterator.
					List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
					
					//look at the predecessor lines;  validate there are only 2 predecessors.
					if(predecessors.size() == 2) {
						StatementIntermediate declaration = null;
						
						for(AbstractIntermediate predecessor : predecessors) {
							if(comparator.before(predecessor, line)) {
								//should be declaration.
								
								if(predecessor instanceof StatementIntermediate) {
									declaration = (StatementIntermediate)predecessor;
									break;
								}
							}
						}
						
						//check to see if the declaration is a temporary variable..
						if(declaration == null) {
							return;
						}
						
						
						//otherwise, let's see if the declaration is an iterator.
						if(declaration.getExpression() instanceof Declaration) {
							Declaration declarationExpression = (Declaration)declaration.getExpression();
							Variable v = (Variable)declarationExpression.getAssignment().getLeft();
							
							//check to see if the declaration is the same as the iterator's name.
							if(StringUtils.equals(iteratorName, v.getName())) {
								LOG.debug("Identified Likely Iterator: "+v.getName());
								
								//get the ".next()" statement, which should be the first child.
								AbstractIntermediate firstChild = line.getTrueTarget();
								
								//see if this is a statement, if the statement is an assignment... 
								//then check the right side to see if it is an invocation.. and the invocation has the method name "next"...
								if(firstChild instanceof StatementIntermediate) {
									StatementIntermediate nextStatement = (StatementIntermediate)firstChild;
									if(nextStatement.getExpression() instanceof Declaration) {
										//the statement is indeed a declaration.
										Declaration nextDeclaration = (Declaration)nextStatement.getExpression();
										
										if(nextDeclaration.getAssignment().getRight() instanceof MethodInvocation) {
											MethodInvocation nextMethodInvocation = (MethodInvocation)nextDeclaration.getAssignment().getRight();
											
											if(StringUtils.equals("next", nextMethodInvocation.getMethodName())) {
												//YES.
												
												//check to see if the next method is on the candidate iterator.
												if(nextMethodInvocation.getTarget() instanceof Variable) {
													Variable nextMethodTarget = (Variable)nextMethodInvocation.getTarget();
													
													
													if(StringUtils.equals(iteratorName, nextMethodTarget.getName())) {
														LOG.info("Definitely an enhanced for loop.");
														
														if(declarationExpression.getAssignment().getRight() instanceof MethodInvocation) {
															MethodInvocation iteratorInvocation = (MethodInvocation)declarationExpression.getAssignment().getRight();
															
															if(StringUtils.equals("iterator", iteratorInvocation.getMethodName())) {
																//now, we are pretty certain this is an enhanced for loop...  we can chop up the graph.
																
																EnhancedForIntermediate enhancedFor = new EnhancedForIntermediate(line.getInstruction(), nextDeclaration.getVariable(), iteratorInvocation.getTarget());
																this.intermediateGraph.addVertex(enhancedFor);
																
																redirectSuccessors(line, enhancedFor);
																redirectPredecessors(line, enhancedFor);
																redirectPredecessors(declaration, enhancedFor);
																redirectSuccessors(nextStatement, enhancedFor);
																
																//remove the declaration, next, and line 
																this.intermediateGraph.removeVertex(line);
																this.intermediateGraph.removeVertex(declaration);
																this.intermediateGraph.removeVertex(nextStatement);
																
															}
														}
														
														
														
														
														
														
													}
												}
												
											}
											
										}
										
									}
									
									
								}
								
								
								
							}
							
						}
						
						
				}
				
			}
			
		}
		
		}
		
	}
	
}
