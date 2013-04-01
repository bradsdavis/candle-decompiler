package org.candle.decompiler.intermediate.graph.enhancer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.edge.InstructionEdgeAttributeProvider;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateLabelProvider;
import org.candle.decompiler.intermediate.graph.IntermediateVertexAttributeProvider;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdgeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.IntegerNameProvider;

public class IntermediateGraphWriter extends GraphIntermediateVisitor {

	private static final Log LOG = LogFactory.getLog(IntermediateGraphWriter.class);
	private static final String GRAPH_LOCATION = "GRAPH_LOCATION";
	
	private final String name;
	private File directory;
	
	public IntermediateGraphWriter(IntermediateGraphContext igc, String name) {
		super(igc);
		this.name = name;

		String graphLocation = System.getProperty(GRAPH_LOCATION);
		if(StringUtils.isNotBlank(graphLocation)) {
			File graphLocationFile = new File(graphLocation);
			if(graphLocationFile.exists() && graphLocationFile.isDirectory()) {
				directory = graphLocationFile;
			}
		}
	}

	@Override
	public void process() {
		if(directory == null) {
			return;
		}
		
		
		File a = new File(directory.getAbsolutePath()+File.separator+name);
		LOG.debug("Instruction Graph: "+a.getAbsolutePath());
		Writer x;
		try {
			x = new FileWriter(a);
			DOTExporter<AbstractIntermediate, IntermediateEdge> dot = new DOTExporter<AbstractIntermediate, IntermediateEdge>(new IntegerNameProvider<AbstractIntermediate>(), new IntermediateLabelProvider(), new IntermediateEdgeProvider(), new IntermediateVertexAttributeProvider(), new InstructionEdgeAttributeProvider()); 
			dot.export(x, igc.getGraph());
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.debug("End Instruction Graph ======");
	}
		
}
