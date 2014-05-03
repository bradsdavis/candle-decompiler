package org.candle.decompiler.intermediate.graph.enhancer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

/**
 * Removes the static class packages from a statement when there is an import statement for the static class.
 *   
 * @author bradsdavis
 *
 */
public class RemoveImportedStaticClassReferences extends GraphIntermediateVisitor {
	private static final Log LOG = LogFactory.getLog(RemoveImportedStaticClassReferences.class);
	
	public RemoveImportedStaticClassReferences(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitStatementIntermediate(StatementIntermediate line) {
		//find out if this is the last statement...
		LOG.info("Expression: "+line.getExpression());
	}

}
