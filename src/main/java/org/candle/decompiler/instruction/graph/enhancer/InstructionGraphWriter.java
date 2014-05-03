package org.candle.decompiler.instruction.graph.enhancer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.edge.InstructionEdgeAttributeProvider;
import org.candle.decompiler.instruction.graph.vertex.InstructionLabelProvider;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdgeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.IntegerNameProvider;

public class InstructionGraphWriter extends InstructionGraphEnhancer {

	private static final Log LOG = LogFactory.getLog(InstructionGraphWriter.class);
	private static final String GRAPH_LOCATION = "GRAPH_LOCATION";

	private final String name;
	private File directory;
	

	public InstructionGraphWriter(InstructionGraphContext igc, String name) {
		super(igc);
		this.name = name;

		String graphLocation = "/Users/bradsdavis/Projects/candle/graphs"; //System.getProperty(GRAPH_LOCATION);
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
			DOTExporter<InstructionHandle, IntermediateEdge> f = new DOTExporter<InstructionHandle, IntermediateEdge>(new IntegerNameProvider<InstructionHandle>(), new InstructionLabelProvider(), new IntermediateEdgeProvider(), null, new InstructionEdgeAttributeProvider()); 
			f.export(x, igc.getGraph());
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.debug("End Instruction Graph ======");
	}

}

