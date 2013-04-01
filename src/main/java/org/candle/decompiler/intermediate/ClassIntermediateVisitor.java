package org.candle.decompiler.intermediate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.ast.BlockVisitor;
import org.candle.decompiler.ast.ClassBlock;
import org.candle.decompiler.ast.ConstructorBlock;
import org.candle.decompiler.ast.MethodBlock;
import org.candle.decompiler.instruction.graph.InstructionGraphContext;
import org.candle.decompiler.instruction.graph.InstructionGraphFactory;
import org.candle.decompiler.instruction.graph.edge.InstructionEdgeAttributeProvider;
import org.candle.decompiler.instruction.graph.enhancer.BackEdgeEnhancer;
import org.candle.decompiler.instruction.graph.enhancer.ConditionEdgeEnhancer;
import org.candle.decompiler.instruction.graph.enhancer.ContinuousLoop;
import org.candle.decompiler.instruction.graph.enhancer.ExceptionEdgeEnhancer;
import org.candle.decompiler.instruction.graph.enhancer.InstructionGraphEnhancer;
import org.candle.decompiler.instruction.graph.enhancer.InstructionToIntermediateEnhancer;
import org.candle.decompiler.instruction.graph.enhancer.LoopHeader;
import org.candle.decompiler.instruction.graph.enhancer.NonIntermediateEliminator;
import org.candle.decompiler.instruction.graph.enhancer.SplitInstructionEnhancer;
import org.candle.decompiler.instruction.graph.vertex.InstructionLabelProvider;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateGraphTransformer;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdge;
import org.candle.decompiler.intermediate.graph.edge.IntermediateEdgeProvider;
import org.candle.decompiler.intermediate.graph.enhancer.ArrayForToEnhancedFor;
import org.candle.decompiler.intermediate.graph.enhancer.ConditionExternalToWhileLoop;
import org.candle.decompiler.intermediate.graph.enhancer.ConditionToWhileLoop;
import org.candle.decompiler.intermediate.graph.enhancer.ConstantArrayCompressor;
import org.candle.decompiler.intermediate.graph.enhancer.Else;
import org.candle.decompiler.intermediate.graph.enhancer.ElseIf;
import org.candle.decompiler.intermediate.graph.enhancer.If;
import org.candle.decompiler.intermediate.graph.enhancer.MergeConditionExpression;
import org.candle.decompiler.intermediate.graph.enhancer.MultiConditionalToSwitchIntermediate;
import org.candle.decompiler.intermediate.graph.enhancer.RemoveCaseToCaseEdge;
import org.candle.decompiler.intermediate.graph.enhancer.RemoveImpliedVoidReturn;
import org.candle.decompiler.intermediate.graph.enhancer.RetractDuplicateFinally;
import org.candle.decompiler.intermediate.graph.enhancer.RetractOrphanGoto;
import org.candle.decompiler.intermediate.graph.enhancer.SwitchGotoToBreak;
import org.candle.decompiler.intermediate.graph.enhancer.WhileToForLoopIncrement;
import org.candle.decompiler.intermediate.graph.enhancer.WhileToForLoopIterator;
import org.candle.decompiler.intermediate.graph.enhancer.IntermediateGraphWriter;
import org.candle.decompiler.intermediate.graph.range.CaseEndRangeIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.range.CatchUpperRangeVisitor;
import org.candle.decompiler.intermediate.graph.range.FinallyRangeVisitor;
import org.candle.decompiler.intermediate.graph.range.IfLowerRangeVisitor;
import org.candle.decompiler.intermediate.graph.range.SwitchRangeVisitor;
import org.candle.decompiler.intermediate.graph.range.WhileRangeVisitor;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.IntegerNameProvider;

import com.sun.org.apache.bcel.internal.classfile.Utility;

public class ClassIntermediateVisitor extends EmptyVisitor {

	private final JavaClass javaClass;
	private final ConstantPoolGen constantPool;
	private ClassBlock classBlock;
	
	public ClassIntermediateVisitor(JavaClass javaClass) {
		this.javaClass = javaClass;
		this.constantPool = new ConstantPoolGen(javaClass.getConstantPool());
		
		this.classBlock = new ClassBlock(javaClass);
	}
	
	public ClassBlock decompile() {
		this.javaClass.accept(this);
		return classBlock;
	}
	
	private static final Log LOG = LogFactory.getLog(ClassIntermediateVisitor.class);
	
	@Override
	public void visitConstantClass(ConstantClass obj) {
		ConstantPool pool = javaClass.getConstantPool();
		String classVal = obj.getConstantValue(pool).toString();
		classVal = StringUtils.replace(classVal, "/", ".");

		if(StringUtils.equals(classVal, this.classBlock.getClassName())) {
			//skip adding class name.
			return;
		}
		
		this.classBlock.getImports().add(classVal);
	}

	@Override
	public void visitField(Field obj) {
		classBlock.getFields().add(obj.toString());
	}

	@Override
	public void visitJavaClass(JavaClass obj) {
		this.classBlock.setClassName(javaClass.getClassName());
		this.classBlock.setPackageName(obj.getPackageName());
		this.classBlock.setSuperClassName(obj.getSuperclassName());
		
		//process the pool.
		Constant[] pool = obj.getConstantPool().getConstantPool();
		for(Constant c : pool) {
			if(c == null) continue;
			c.accept(this);
		} 
		
		Field[] fields = obj.getFields();
		for(int i=0, j=fields.length; i<j; i++) {
			fields[i].accept(this);
		}
		
		//run through all of the methods
		Method[] methods = obj.getMethods();
		for(int i=0, j=methods.length; i<j; i++) {
			methods[i].accept(this);
		}
	}

	public void processIntermediate(IntermediateGraphContext igc) {
		List<GraphIntermediateVisitor> enhancers = new LinkedList<GraphIntermediateVisitor>();
		
		enhancers.add(new MergeConditionExpression(igc));
		enhancers.add(new ConstantArrayCompressor(igc));
		
		enhancers.add(new ConditionToWhileLoop(igc));
		enhancers.add(new ConditionExternalToWhileLoop(igc));
		
		enhancers.add(new IntermediateGraphWriter(igc, "ibefore.dot"));
		
		enhancers.add(new FinallyRangeVisitor(igc));
		enhancers.add(new CatchUpperRangeVisitor(igc));
		
		
		enhancers.add(new RetractDuplicateFinally(igc));
		enhancers.add(new RetractOrphanGoto(igc));
		
		
		enhancers.add(new WhileToForLoopIncrement(igc));
		
		
		enhancers.add(new WhileToForLoopIterator(igc));
		
		
		enhancers.add(new ArrayForToEnhancedFor(igc));
		

		
		enhancers.add(new If(igc));
		enhancers.add(new ElseIf(igc));
		enhancers.add(new Else(igc));

		
		enhancers.add(new MultiConditionalToSwitchIntermediate(igc));
		enhancers.add(new SwitchRangeVisitor(igc));
		enhancers.add(new SwitchGotoToBreak(igc));
		enhancers.add(new CaseEndRangeIntermediateVisitor(igc));
		enhancers.add(new RemoveCaseToCaseEdge(igc));

		enhancers.add(new WhileRangeVisitor(igc));
		enhancers.add(new IfLowerRangeVisitor(igc));
		
		
		//enhancers.add(new RemoveImpliedVoidReturn(igc));
		
		enhancers.add(new IntermediateGraphWriter(igc, "iafter.dot"));
		
		
		for(GraphIntermediateVisitor giv : enhancers) {
			giv.process();
		}
		
		
	}

	@Override
	public void visitMethod(Method obj) {
		MethodGen methodGenerator = new MethodGen(obj, this.javaClass.getClassName(), this.constantPool);
		
		LOG.debug("Processing MethodInvocation: "+methodGenerator.toString());
		IntermediateContext intermediateContext = new IntermediateContext(this.javaClass, methodGenerator);
		
		InstructionList instructions = methodGenerator.getInstructionList();
		instructions.setPositions(true);
		
		InstructionGraphFactory igf = new InstructionGraphFactory(instructions, methodGenerator.getExceptionHandlers());
		InstructionGraphContext igc = igf.process();
		
		writeGraph("before.dot", igc);
		
		List<InstructionGraphEnhancer> iges = new ArrayList<InstructionGraphEnhancer>();
		iges.add(new SplitInstructionEnhancer(igc));
		iges.add(new ConditionEdgeEnhancer(igc));
		iges.add(new ExceptionEdgeEnhancer(igc, methodGenerator.getExceptionHandlers()));
		iges.add(new InstructionToIntermediateEnhancer(igc, intermediateContext));
		
		iges.add(new BackEdgeEnhancer(igc));
		iges.add(new LoopHeader(igc));
		iges.add(new ContinuousLoop(igc));
		iges.add(new NonIntermediateEliminator(igc));
		
		for(InstructionGraphEnhancer ige : iges)
		{
			ige.process();
		}
		
		writeGraph("after.dot", igc);
		

		IntermediateGraphTransformer igt = new IntermediateGraphTransformer(igc);
		IntermediateGraphContext interGraphContext = igt.getIntermediateGraphContext();
		processIntermediate(interGraphContext);
		
		MethodBlock method = extractMethodSignature(methodGenerator);
		BlockVisitor iv = new BlockVisitor(interGraphContext, method);
		iv.process();
		
		classBlock.addChild(method);
		method.setParent(classBlock);
	}


	private void writeGraph(String name, InstructionGraphContext igc) {
		LOG.debug("Instruction Graph ======");
		File a = new File("/Users/bradsdavis/Projects/workspace/clzTest/"+name);
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

	protected MethodBlock extractMethodSignature(MethodGen methodGen) {
		
		MethodBlock mb = null;
		boolean isConstructor = false;
		
		
        ConstantPoolGen cpg = methodGen.getConstantPool();
        LocalVariableTable lvt = methodGen.getLocalVariableTable(cpg);
        
		String access = Utility.accessToString(methodGen.getAccessFlags());
		String signature = Type.getMethodSignature(methodGen.getType(), methodGen.getArgumentTypes());
        
        String name = methodGen.getName();
        if(StringUtils.equals(name, Constants.CONSTRUCTOR_NAME)) {
        	name = StringUtils.substringAfterLast(methodGen.getClassName(), ".");
        	isConstructor = true;
        }
        
        if(StringUtils.equals(methodGen.getName(), Constants.STATIC_INITIALIZER_NAME)) {
        	signature = "static ";
        }
        else {
        	signature = org.apache.bcel.classfile.Utility.methodSignatureToString(signature, name, access, true, lvt);
        }
        
        StringBuilder builder = new StringBuilder(signature);
        if (methodGen.getExceptions().length > 0) {
            for (String excep : methodGen.getExceptions()) {
                builder.append(" throws ").append(excep);
            }
        }
        
        if(isConstructor){
        	mb = new ConstructorBlock(builder.toString(), methodGen.getInstructionList().getEnd().getPosition());
        }
        else {
            mb = new MethodBlock(builder.toString(), methodGen.getInstructionList().getEnd().getPosition());
        }
        
        return mb;
	}
}
