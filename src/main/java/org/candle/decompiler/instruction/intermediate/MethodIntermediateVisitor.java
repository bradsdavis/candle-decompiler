package org.candle.decompiler.instruction.intermediate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.IntermediateContext;
import org.candle.decompiler.intermediate.IntermediateVariable;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Arithmetic;
import org.candle.decompiler.intermediate.expression.ArithmeticType;
import org.candle.decompiler.intermediate.expression.ArrayLength;
import org.candle.decompiler.intermediate.expression.ArrayAccess;
import org.candle.decompiler.intermediate.expression.Assignment;
import org.candle.decompiler.intermediate.expression.Cast;
import org.candle.decompiler.intermediate.expression.ConstructorInvocation;
import org.candle.decompiler.intermediate.expression.Declaration;
import org.candle.decompiler.intermediate.expression.Expression;
import org.candle.decompiler.intermediate.expression.FieldReference;
import org.candle.decompiler.intermediate.expression.GeneratedVariable;
import org.candle.decompiler.intermediate.expression.Increment;
import org.candle.decompiler.intermediate.expression.InstanceOf;
import org.candle.decompiler.intermediate.expression.MethodInvocation;
import org.candle.decompiler.intermediate.expression.MultiConditional;
import org.candle.decompiler.intermediate.expression.NewArrayInstance;
import org.candle.decompiler.intermediate.expression.NewConstantArrayInstance;
import org.candle.decompiler.intermediate.expression.NewInstance;
import org.candle.decompiler.intermediate.expression.OperationType;
import org.candle.decompiler.intermediate.expression.Resolved;
import org.candle.decompiler.intermediate.expression.Return;
import org.candle.decompiler.intermediate.expression.SingleConditional;
import org.candle.decompiler.intermediate.expression.Switch;
import org.candle.decompiler.intermediate.expression.Ternary;
import org.candle.decompiler.intermediate.expression.Throw;
import org.candle.decompiler.intermediate.expression.TypedExpression;
import org.candle.decompiler.intermediate.expression.Variable;

public class MethodIntermediateVisitor implements Visitor {
	private static final Log LOG = LogFactory.getLog(MethodIntermediateVisitor.class);
	
	private final IntermediateContext context;
	
	public MethodIntermediateVisitor(IntermediateContext context) {
		this.context = context;
	}
	
	//push commands
	public void visitConstantPushInstruction(ConstantPushInstruction instruction) {
		//handle in subtype.
	}
	public void visitICONST(ICONST instruction) {
		LOG.debug("Loading: "+instruction.getValue().toString());
		Resolved cons = new Resolved(context.getCurrentInstruction(), Type.INT, instruction.getValue().toString());
		context.getExpressions().push(cons);
	}

	public void visitLCONST(LCONST instruction) {
		Resolved cons = new Resolved(context.getCurrentInstruction(), Type.LONG, instruction.getValue().toString());
		context.getExpressions().push(cons);
	}
	

	public void visitDCONST(DCONST instruction) {
		Resolved cons = new Resolved(context.getCurrentInstruction(), Type.DOUBLE, instruction.getValue().toString());
		context.getExpressions().push(cons);
	}
	
	public void visitBIPUSH(BIPUSH instruction) {
		Resolved resolved = new Resolved(context.getCurrentInstruction(), Type.BYTE, instruction.getValue().toString());
		context.getExpressions().push(resolved);
		LOG.debug("Pushing: "+resolved);
	}
	
	public void visitSIPUSH(SIPUSH instruction) {
		Resolved resolved = new Resolved(context.getCurrentInstruction(), Type.SHORT, instruction.getValue().toString());
		context.getExpressions().push(resolved);
		LOG.debug("Pushing: "+resolved);
	}

	//return instructions
	public void visitReturnInstruction(ReturnInstruction instruction) {
		//fall through to typed expressions.
	}
	
	protected void processReturn(Return ret) {
		StatementIntermediate complete = new StatementIntermediate(context.getCurrentInstruction(), ret);
		context.pushIntermediateToInstruction(complete);
	}
	
	public void visitFRETURN(FRETURN instruction) {
		Expression exp = context.getExpressions().pop();
		Return ret = new Return(context.getCurrentInstruction(), exp);
		
		processReturn(ret);
	}

	public void visitDRETURN(DRETURN instruction) {
		Expression exp = context.getExpressions().pop();
		Return ret = new Return(context.getCurrentInstruction(), exp);
		
		processReturn(ret);	
	}

	public void visitARETURN(ARETURN instruction) {
		Expression exp = context.getExpressions().pop();
		Return ret = new Return(context.getCurrentInstruction(), exp);
		
		processReturn(ret);
	}
	public void visitRETURN(RETURN instruction) {
		Return ret = new Return(context.getCurrentInstruction());
		processReturn(ret);		
	}
	public void visitLRETURN(LRETURN instruction) {
		Expression exp = context.getExpressions().pop();
		Return ret = new Return(context.getCurrentInstruction(), exp);
		
		processReturn(ret);		
	}
	public void visitIRETURN(IRETURN instruction) {
		Expression exp = context.getExpressions().pop();
		Return ret = new Return(context.getCurrentInstruction(), exp);
		
		processReturn(ret);
	}

	public void visitGOTO_W(GOTO_W instruction) {
		//fall to goto
	}
	
	public void visitGOTO(GOTO instruction) {
		// fall to goto
	}

	public void visitGotoInstruction(GotoInstruction instruction) {
		GoToIntermediate line = new GoToIntermediate(context.getCurrentInstruction());
		context.pushIntermediateToInstruction(line);
	}
	
	//Process load instructions
	public void visitALOAD(ALOAD instruction) {
		//fall to load instruction
	}

	public void visitILOAD(ILOAD instruction) {
		//fall to load instruction
	}

	public void visitDLOAD(DLOAD instruction) {
		//fall to load instruction
	}
	
	public void visitFLOAD(FLOAD instruction) {
		//fall to load instruction
	}

	public void visitLLOAD(LLOAD instruction) {
		//fall to load instruction
	}
	
	public void visitLoadInstruction(LoadInstruction instruction) {
		int index = instruction.getIndex();
		
		if(index >=0) {
			IntermediateVariable localVar = context.getVariableResolver().getLocalVariable(index, context.getCurrentInstruction().getPosition()); 

			if(localVar == null) {
				LOG.debug("Did not find local variable: "+index + " for position: "+context.getCurrentInstruction().getPosition());
			}
			Variable variable = null;
			if(localVar == null) {
				//probably need to create a variable for enhanced loops...
				Type type = instruction.getType(context.getMethodGen().getConstantPool());
				localVar = context.getVariableResolver().addLocalVariable(index, context.getCurrentInstruction(), type);
				
				variable = new GeneratedVariable(context.getCurrentInstruction(), localVar.getType(), localVar.getName());
			}
			else {
				variable = new Variable(context.getCurrentInstruction(), localVar.getType(), localVar.getName());
			}
			
			//Variable variable = new Variable(context.getCurrentInstruction(), localVar.getType(), localVar.getName());
			
			context.getExpressions().push(variable);
			LOG.debug("Loaded: "+variable);
		}
		else {
			LOG.warn("Did not load because index: "+index);
		}
	}

	public void visitACONST_NULL(ACONST_NULL instruction) {
		//load from constant pool.
		Resolved cons = new Resolved(context.getCurrentInstruction(), Type.NULL, "null");
		context.getExpressions().push(cons);
	}

	public void visitGETSTATIC(GETSTATIC instruction) {
		LOG.debug("Getting static..");
		MethodGen mg = context.getMethodGen();
		ConstantPoolGen cpg = mg.getConstantPool();
		
		
		
		String referencedClassName = instruction.getReferenceType(cpg).toString();
		String thisClassName = context.getJavaClass().getClassName();

		String resolved = null;
		if(StringUtils.equals(referencedClassName, thisClassName)) {
			resolved = instruction.getFieldName(cpg);
		}
		else {
			resolved = referencedClassName+"."+instruction.getFieldName(cpg);
		}
		
		Resolved cons = new Resolved(context.getCurrentInstruction(), instruction.getType(cpg), resolved);
		context.getExpressions().push(cons);
	}

	public void visitPUTSTATIC(PUTSTATIC instruction) {
		ConstantPoolGen cpg = context.getMethodGen().getConstantPool();
		String fieldName = instruction.getFieldName(cpg);
		Type fieldType = instruction.getFieldType(cpg);
		
		Expression right = context.getExpressions().pop();
		Variable variable = new Variable(context.getCurrentInstruction(), fieldType, fieldName);
		Assignment assignment = new Assignment(context.getCurrentInstruction(), variable, right);
		
		if(LOG.isDebugEnabled()) {
			for(Field field : context.getJavaClass().getFields()) {
				LOG.debug(field);
			}
		}
		
		StatementIntermediate complete = new StatementIntermediate(context.getCurrentInstruction(), assignment);
		
		context.pushIntermediateToInstruction(complete);
	}
	
	public void visitPUTFIELD(PUTFIELD instruction) {
		ConstantPoolGen cpg = context.getMethodGen().getConstantPool();
		
		String fieldName = instruction.getFieldName(cpg);
		
		Expression right = context.getExpressions().pop();
		Expression left = context.getExpressions().pop();
		
		FieldReference fieldRef = new FieldReference(context.getCurrentInstruction(), left, fieldName);
		Assignment assignment = new Assignment(context.getCurrentInstruction(), fieldRef, right);
		
		StatementIntermediate complete = new StatementIntermediate(context.getCurrentInstruction(), assignment);
		
		context.pushIntermediateToInstruction(complete);
	}
	
	
	public void visitGETFIELD(GETFIELD instruction) {
		LOG.debug("Getting field..");
		Expression target = context.getExpressions().pop();
		
		
		MethodGen mg = context.getMethodGen();
		ConstantPoolGen cpg = mg.getConstantPool();
		
		FieldReference ref = new FieldReference(context.getCurrentInstruction(), target, instruction.getFieldName(cpg));
		context.getExpressions().push(ref);
	}

	public void visitLDC2_W(LDC2_W instruction) {
		//load from constant pool.
		LOG.debug("Loading from constant pool"+instruction.getClass());
		
		MethodGen mg = context.getMethodGen();
		ConstantPoolGen cpg = mg.getConstantPool();
		
		StringBuilder resolvedValue = new StringBuilder();
		Type type = instruction.getType(cpg);
		if(Type.STRING == type) {
			resolvedValue.append("\"");
		}
		
		
		Object instructionValue = instruction.getValue(cpg);
		if(instructionValue instanceof ConstantClass) {
			String clzName = getClassName((ConstantClass)instructionValue, cpg.getConstantPool());
			resolvedValue.append(clzName);
		}
		else {
			resolvedValue.append(instructionValue.toString());
		}
		
		
		if(Type.STRING == type) {
			resolvedValue.append("\"");
		}
		
		Resolved resolved = new Resolved(context.getCurrentInstruction(), type, resolvedValue.toString());
		context.getExpressions().push(resolved);
	}
	
	public void visitLDC(LDC instruction) {
		//load from constant pool.
		LOG.debug("Loading from constant pool"+instruction.getClass());
		
		MethodGen mg = context.getMethodGen();
		ConstantPoolGen cpg = mg.getConstantPool();
		
		StringBuilder resolvedValue = new StringBuilder();
		Type type = instruction.getType(cpg);
		if(Type.STRING == type) {
			resolvedValue.append("\"");
		}
		
		
		Object instructionValue = instruction.getValue(cpg);
		if(instructionValue instanceof ConstantClass) {
			String clzName = getClassName((ConstantClass)instructionValue, cpg.getConstantPool());
			resolvedValue.append(clzName);
		}
		else {
			resolvedValue.append(instructionValue.toString());
		}
		
		
		if(Type.STRING == type) {
			resolvedValue.append("\"");
		}
		
		Resolved resolved = new Resolved(context.getCurrentInstruction(), type, resolvedValue.toString());
		context.getExpressions().push(resolved);
	}

	public void visitFCONST(FCONST instruction) {
		Resolved cons = new Resolved(context.getCurrentInstruction(), Type.FLOAT, instruction.getValue().toString());
		context.getExpressions().push(cons);
	}
	
	public void visitIINC(IINC instruction) {
		//increment variable.
		int index = instruction.getIndex();
		
		IntermediateVariable iv = context.getVariableResolver().getLocalVariable(index, context.getCurrentInstruction().getPosition());
		
		Variable variable = null;
		if(iv == null) {
			//generate IV.
			iv = context.getVariableResolver().addLocalVariable(index, context.getCurrentInstruction(), instruction.getType(context.getMethodGen().getConstantPool()));
			variable = new GeneratedVariable(context.getCurrentInstruction(), iv.getType(), iv.getName());
		}
		else {
			variable = new Variable(context.getCurrentInstruction(), iv.getType(), iv.getName());
		}
		
		//now, how much does it increment by?
		int incrementBy = instruction.getIncrement();
		
		StringBuilder incrementerBuilder = new StringBuilder(iv.getName());
		if(incrementBy == 1) {
			incrementerBuilder.append("++");
		}
		else if(incrementBy == -1) {
			incrementerBuilder.append("--");
		}
		else if(incrementBy < 1) {
			incrementerBuilder.append(" -= ").append((-1*incrementBy));
		}
		else {
			incrementerBuilder.append(" += ").append(incrementBy);
		}
		
		Expression exp = new Increment(context.getCurrentInstruction(), variable, Type.INT, incrementerBuilder.toString());
		context.pushIntermediateToInstruction(new StatementIntermediate(context.getCurrentInstruction(), exp));
	}

	
	@Override
	public void visitIfInstruction(IfInstruction obj) {
		//do nothing.  handle in subtypes
	}

	//Process all single-value conditionals.
	@Override
	public void visitIFLT(IFLT instruction) {
		Expression left = context.getExpressions().pop();
		//TODO: this should probably be resolved to it's left expression value for the resovled value.
		Expression right = new Resolved(context.getCurrentInstruction(), null, "0");
		processMultiConditionalStatement(OperationType.LESS, left, right);
	}
	
	@Override
	public void visitIFGT(IFGT instruction) {
		Expression right = new Resolved(context.getCurrentInstruction(), null, "0");
		Expression left = context.getExpressions().pop();
		processMultiConditionalStatement(OperationType.GREATER, left, right);
	}

	@Override
	public void visitIFGE(IFGE obj) {
		Expression right = new Resolved(context.getCurrentInstruction(), null, "0");
		Expression left = context.getExpressions().pop();
		processMultiConditionalStatement(OperationType.GREATER_EQUAL, left, right);
	}
	@Override
	public void visitIFLE(IFLE obj) {
		Expression right = new Resolved(context.getCurrentInstruction(), null, "0");
		Expression left = context.getExpressions().pop();
		processMultiConditionalStatement(OperationType.LESS_EQUAL, left, right);
	}
	
	@Override
	public void visitIFNE(IFNE instruction) {
		Expression left = context.getExpressions().pop();
		SingleConditional conditional = new SingleConditional(context.getCurrentInstruction(), left, false);
		BooleanBranchIntermediate line = new BooleanBranchIntermediate(context.getCurrentInstruction(), conditional);
		context.pushIntermediateToInstruction(line);
	}
	
	@Override
	public void visitIFEQ(IFEQ instruction) {
		Expression left = context.getExpressions().pop();
		SingleConditional conditional = new SingleConditional(context.getCurrentInstruction(), left);
		BooleanBranchIntermediate line = new BooleanBranchIntermediate(context.getCurrentInstruction(), conditional);
		context.pushIntermediateToInstruction(line);
	}
	
	@Override
	public void visitIFNULL(IFNULL instruction) {
		Expression left = context.getExpressions().pop();
		Expression right = new Resolved(context.getCurrentInstruction(), Type.NULL, "null");
		
		MultiConditional conditional = new MultiConditional(context.getCurrentInstruction(), left, right, OperationType.EQ);
		BooleanBranchIntermediate line = new BooleanBranchIntermediate(context.getCurrentInstruction(), conditional);
		context.pushIntermediateToInstruction(line);
	}

	@Override
	public void visitIFNONNULL(IFNONNULL instruction) {
		Expression left = context.getExpressions().pop();
		Expression right = new Resolved(context.getCurrentInstruction(), Type.NULL, "null");
		
		MultiConditional conditional = new MultiConditional(context.getCurrentInstruction(), left, right, OperationType.NE);
		BooleanBranchIntermediate line = new BooleanBranchIntermediate(context.getCurrentInstruction(), conditional);
		context.pushIntermediateToInstruction(line);
	}
	

	public void visitINSTANCEOF(INSTANCEOF instruction) {
		ConstantPoolGen cpg = context.getMethodGen().getConstantPool();
		String type = instruction.getLoadClassType(cpg).getClassName();

		//get the left, create the right
		Expression left = context.getExpressions().pop();
		Expression right = new Resolved(context.getCurrentInstruction(), Type.BOOLEAN, type);
		InstanceOf instanceOf = new InstanceOf(context.getCurrentInstruction(), left, right);
		
		context.getExpressions().push(instanceOf);
	}

	
	//All Multi Value Conditionals.
	public void visitIF_ICMPLT(IF_ICMPLT instruction) {
		processMultiConditionalStatement(OperationType.LESS);
	}
	public void visitIF_ICMPGT(IF_ICMPGT instruction) {
		processMultiConditionalStatement(OperationType.GREATER);
	}
	public void visitIF_ICMPGE(IF_ICMPGE instruction) {
		processMultiConditionalStatement(OperationType.GREATER_EQUAL);
	}
	public void visitIF_ICMPLE(IF_ICMPLE instruction) {
		processMultiConditionalStatement(OperationType.LESS_EQUAL);
	}
	public void visitIF_ICMPNE(IF_ICMPNE instruction) {
		processMultiConditionalStatement(OperationType.NE);
	}
	public void visitIF_ACMPNE(IF_ACMPNE instruction) {
		//this.context.setCurrentInstruction(instruction.negate());
		processMultiConditionalStatement(OperationType.NE);
	}
	public void visitIF_ICMPEQ(IF_ICMPEQ instruction) {
		processMultiConditionalStatement(OperationType.EQ);
	}
	public void visitIF_ACMPEQ(IF_ACMPEQ instruction) {
		processMultiConditionalStatement(OperationType.EQ);
	}
	public void processMultiConditionalStatement(OperationType operation) {
		//first, get the things to be operated on.
		Expression right = context.getExpressions().pop();
		Expression left = context.getExpressions().pop();
		
		processMultiConditionalStatement(operation, left, right);
	}
	
	public void processMultiConditionalStatement(OperationType operation, Expression left, Expression right) {
		MultiConditional conditional = new MultiConditional(context.getCurrentInstruction(), left, right, operation);
		//context.getExpressions().push(conditional);
		BooleanBranchIntermediate line = new BooleanBranchIntermediate(this.context.getCurrentInstruction(), conditional);
		//check to see whether you need to negate.
		
		//if the conditional's target is greater than the conditional's next statement, don't negate.
		BranchHandle branchHandle = (BranchHandle) context.getCurrentInstruction();
		int next = branchHandle.getNext().getPosition();
		int target = branchHandle.getTarget().getPosition();
		
		
		//Important.  Make sure the expression "true" is pointed towards the lower branch.
		if(target > next) {
			line.getExpression().negate();
		}
		
		context.pushIntermediateToInstruction(line);
	}
	
	
	//New object instances.
	public void visitNEW(NEW instruction) {
		ObjectType type = instruction.getLoadClassType(this.context.getMethodGen().getConstantPool());
		Type t = instruction.getType(this.context.getMethodGen().getConstantPool());
		LOG.debug("New object: "+t +", "+type);
		NewInstance instance = new NewInstance(context.getCurrentInstruction(), type);
		context.getExpressions().push(instance);
	}
	
	public void visitDUP2(DUP2 instruction) {
		Expression one = context.getExpressions().pop();
		Expression two = context.getExpressions().pop();
		
		context.getExpressions().push(two);
		context.getExpressions().push(one);
	
		context.getExpressions().push(two);
		context.getExpressions().push(one);
	}

	public void visitDUP2_X1(DUP2_X1 instruction) {
		Expression one = context.getExpressions().pop();
		Expression two = context.getExpressions().pop();
		Expression three = context.getExpressions().pop();
		
		//push on 1, 2
		context.getExpressions().push(two);
		context.getExpressions().push(one);
		
		//push on 1, 2, 3
		context.getExpressions().push(three);
		context.getExpressions().push(two);
		context.getExpressions().push(one);

	}
	

	public void visitDUP2_X2(DUP2_X2 instruction) {
		Expression one = context.getExpressions().pop();
		Expression two = context.getExpressions().pop();
		Expression three = context.getExpressions().pop();
		Expression four = context.getExpressions().pop();
		
		//push on 1, 2
		context.getExpressions().push(two);
		context.getExpressions().push(one);
		
		//push on 1, 2, 3, 4
		context.getExpressions().push(four);
		context.getExpressions().push(three);
		context.getExpressions().push(two);
		context.getExpressions().push(one);
	}
	
	//Now provide the suplication visitors
	public void visitDUP(DUP instruction) {
		Expression exp = context.getExpressions().pop();
		
		try {
			Expression dup = (Expression)exp.clone();
			dup.setInstructionHandle(context.getCurrentInstruction());
			context.getExpressions().push(dup);
			context.getExpressions().push(exp);
			
		} catch (CloneNotSupportedException e) {
			LOG.error("Exception duplicating expression: "+exp.toString(), e);
			
		}
		
	}
	
	public void visitDUP_X2(DUP_X2 instruction) {
		Expression one = context.getExpressions().pop();
		Expression two = context.getExpressions().pop();
		Expression three = context.getExpressions().pop();
		
		context.getExpressions().push(one);
		
		context.getExpressions().push(three);
		context.getExpressions().push(two);
		context.getExpressions().push(one);
	}


	public void visitDUP_X1(DUP_X1 instruction) {
		Expression one = context.getExpressions().pop();
		Expression two = context.getExpressions().pop();
		
		context.getExpressions().push(one);
		context.getExpressions().push(two);
		
		context.getExpressions().push(one);
	}
	
	public void visitSWAP(SWAP instruction) {
		Expression one = context.getExpressions().pop();
		Expression two = context.getExpressions().pop();

		context.getExpressions().push(one);
		context.getExpressions().push(two);
	}


	//call the method name..
	public void visitInvokeInstruction(InvokeInstruction instruction) {
		//handle in subtype.
	}

	public void visitINVOKESPECIAL(INVOKESPECIAL instruction) {
		LOG.debug("Invoking special.");
		
		Type[] types = instruction.getArgumentTypes(context.getMethodGen().getConstantPool());

		final List<Expression> parameters = new ArrayList<Expression>(types.length);
		for(int i=0, j=types.length; i<j; i++) 
		{
			Expression param = context.getExpressions().pop();
			LOG.debug("Parameter: "+param);
			parameters.add(param);
		}
		
		//now, get the target that we are calling the method on.
		Expression target = context.getExpressions().pop();
		
		//collect the method name we are calling.
		String methodName = instruction.getMethodName(context.getMethodGen().getConstantPool());
		
		
		MethodInvocation methodInvocation = null;
		//create the expression..
		if(StringUtils.equals(Constants.CONSTRUCTOR_NAME, methodName)) {
			LOG.debug(target.getClass());
			//pop the duplicate..
			//TODO: Figure out why the dup / new causes issues.
			if(target instanceof NewInstance) {
				//get rid of this.
				context.getExpressions().pop();
			}
			methodInvocation = new ConstructorInvocation(context.getCurrentInstruction(), target, methodName, parameters);
		}
		else {
			methodInvocation = new MethodInvocation(context.getCurrentInstruction(), target, methodName, parameters);
		}
		
		LOG.debug("Pushing: "+methodInvocation);
		context.getExpressions().push(methodInvocation);
	}
	

	
	public void visitINVOKESTATIC(INVOKESTATIC instruction) {
		//collect all parameters from the stack.
		ConstantPoolGen cpg = context.getMethodGen().getConstantPool();
		Type[] types = instruction.getArgumentTypes(context.getMethodGen().getConstantPool());
		
		final List<Expression> parameters = new ArrayList<Expression>(types.length);
		for(int i=0, j=types.length; i<j; i++) 
		{
			Expression param = context.getExpressions().pop();
			LOG.debug("Parameter: "+param);
			
			parameters.add(param);
		}
		
		
		Resolved resolvedType = new Resolved(context.getCurrentInstruction(), Type.CLASS, instruction.getLoadClassType(cpg).getClassName());
		String methodName = instruction.getMethodName(cpg);
		
		//create the expression..
		MethodInvocation methodInvocation = new MethodInvocation(context.getCurrentInstruction(), resolvedType, methodName, parameters);
		
		Type returned = instruction.getReturnType(context.getMethodGen().getConstantPool());
		
		if(returned == BasicType.VOID) {
			LOG.debug("This is a void return type!!");
			StatementIntermediate completeLine = new StatementIntermediate(context.getCurrentInstruction(), methodInvocation);
			context.pushIntermediateToInstruction(completeLine);
		}
		else {
			context.getExpressions().push(methodInvocation);
		}
	}
	
	public void visitINVOKEVIRTUAL(INVOKEVIRTUAL instruction) {
		LOG.debug("Invoking virtual..");
		Type[] types = instruction.getArgumentTypes(context.getMethodGen().getConstantPool());
		processInvoke(instruction, types.length);
	}
	

	public void visitINVOKEINTERFACE(INVOKEINTERFACE instruction) {
		ConstantPoolGen cpg = context.getMethodGen().getConstantPool();
		//collect all parameters from the stack.
		Type[] types = instruction.getArgumentTypes(cpg);
		
		final List<Expression> parameters = new ArrayList<Expression>(types.length);
		for(int i=0, j=types.length; i<j; i++)  
		{
			Expression param = context.getExpressions().pop();
			LOG.debug("Parameter: "+param);
			parameters.add(param);
		}
		
		//collect the method name we are calling.
		String methodName = instruction.getMethodName(context.getMethodGen().getConstantPool());
		
		Expression left = context.getExpressions().pop();
		
		//create the expression..
		MethodInvocation methodInvocation = new MethodInvocation(context.getCurrentInstruction(), left, methodName, parameters);
		
		Type returned = instruction.getReturnType(context.getMethodGen().getConstantPool());
		
		if(returned == BasicType.VOID) {
			StatementIntermediate completeLine = new StatementIntermediate(context.getCurrentInstruction(), methodInvocation);
			context.pushIntermediateToInstruction(completeLine);
			LOG.debug("Pushed complete line: "+completeLine.toString());
		}
		else {
			context.getExpressions().push(methodInvocation);
			
			LOG.debug("Pushed expression: "+methodInvocation);
		}
	
	}
	
	public void processInvoke(InvokeInstruction instruction, int nparams) {
		//collect all parameters from the stack.

		final List<Expression> parameters = new ArrayList<Expression>(nparams);
		for(int i=0; i<nparams; i++) 
		{
			Expression param = context.getExpressions().pop();
			LOG.debug("Parameter: "+param);
			parameters.add(param);
		}
		
		//now, get the target that we are calling the method on.
		Expression target = context.getExpressions().pop();
		
		//collect the method name we are calling.
		String methodName = instruction.getMethodName(context.getMethodGen().getConstantPool());
		
		//create the expression..
		MethodInvocation methodInvocation = new MethodInvocation(context.getCurrentInstruction(), target, methodName, parameters);
		
		Type returned = instruction.getReturnType(context.getMethodGen().getConstantPool());
		
		if(returned == BasicType.VOID) {
			StatementIntermediate completeLine = new StatementIntermediate(context.getCurrentInstruction(), methodInvocation);
			context.pushIntermediateToInstruction(completeLine);
			LOG.debug("Pushed complete line: "+completeLine.toString());
		}
		else {
			context.getExpressions().push(methodInvocation);
			
			LOG.debug("Pushed expression: "+methodInvocation);
		}
	}
	
	
	//Storing instructions
	public void visitLSTORE(LSTORE instruction) {
		//fall to store instruction.
	}
	public void visitFSTORE(FSTORE instruction) {
		//fall to store instruction.
	}
	public void visitISTORE(ISTORE instruction) {
		//fall to store instruction.
	}
	public void visitASTORE(ASTORE instruction) {
		//fall to store instruction.
	}
	public void visitDSTORE(DSTORE instruction) {
		//fall to store instruction.
	}

	public void visitStoreInstruction(StoreInstruction instruction) {
		Expression right = this.context.getExpressions().pop();
		Type type = instruction.getType(context.getMethodGen().getConstantPool());
		
		//first, check to see whether the variable currently has been declared.
		//this would be the case if we don't get null when looking up the local variable.
		int pc = context.getCurrentInstruction().getPosition();
		int lvtIndex = instruction.getIndex();
		
		IntermediateVariable iv = context.getVariableResolver().getLocalVariable(lvtIndex, pc);

		//if the variable is not null, it is declared.
		boolean declared = (iv != null);
		
		Variable variable = null;
		if(!declared) {
			//look it up from the next phase code.
			pc = this.context.getCurrentInstruction().getNext().getPosition();
			iv = context.getVariableResolver().getLocalVariable(lvtIndex, pc);

			if(iv == null) {
				//probably need to create a variable for enhanced loops...
				LOG.debug("Adding index: "+instruction.getIndex() + " as: "+type);
				
				//try and resolve the type for the variable from the right hand side.
				if(type == Type.OBJECT) {
					if(right instanceof TypedExpression) {
						type = ((TypedExpression) right).getType();
					}
				}
				
				//generate variable name...
				iv = context.getVariableResolver().addLocalVariable(instruction.getIndex(), context.getCurrentInstruction(), type);
				variable = new GeneratedVariable(context.getCurrentInstruction(), iv.getType(), iv.getName());
			}
		}
		
		//create the variable.
		if(variable == null) {
			variable = new Variable(context.getCurrentInstruction(), iv.getType(), iv.getName());
		}
		
		//create the assignment.
		Assignment assignment = new Assignment(context.getCurrentInstruction(), variable, right);
		
		Expression left = null;
		if(declared) {
			left = assignment;
		}
		else {
			//if it is currently not declared... create the declaration.
			left = new Declaration(context.getCurrentInstruction(), variable, assignment);
		}
		
		StatementIntermediate cl = new StatementIntermediate(context.getCurrentInstruction(), left);
		context.pushIntermediateToInstruction(cl);

		LOG.debug("Stored.");
	}
	
	

	//arithmetic operations
	public void visitArithmeticInstruction(ArithmeticInstruction instruction) {
		//fall through to instruction types
	}

	protected void processArithmeticTwoStackOperations(ArithmeticType type) {
		Expression right = context.getExpressions().pop();
		Expression left = context.getExpressions().pop();
		
		Expression addExp = new Arithmetic(context.getCurrentInstruction(), left, right, type);
		context.getExpressions().push(addExp);
	}
	

	
	//add operations
	public void visitLADD(LADD instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.ADD);
	}

	public void visitDADD(DADD instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.ADD);
	}
	public void visitIADD(IADD instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.ADD);
	}
	public void visitFADD(FADD instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.ADD);
	}
	
	
	//subtract operations
	public void visitLSUB(LSUB instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SUBTRACT);
	}
	public void visitFSUB(FSUB instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SUBTRACT);
	}
	public void visitDSUB(DSUB instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SUBTRACT);
	}
	public void visitISUB(ISUB instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SUBTRACT);
	}
	
	//multiply operations
	public void visitIMUL(IMUL instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.MULTIPLY);
	}
	public void visitDMUL(DMUL instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.MULTIPLY);
	}
	public void visitFMUL(FMUL instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.MULTIPLY);
	}
	public void visitLMUL(LMUL instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.MULTIPLY);
	}
	
	//divide operations
	public void visitDDIV(DDIV instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.DIVIDE);
	}
	public void visitFDIV(FDIV instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.DIVIDE);
	}
	public void visitLDIV(LDIV instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.DIVIDE);
	}
	public void visitIDIV(IDIV instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.DIVIDE);
	}

	//remainder operations
	public void visitDREM(DREM instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.REMAINDER);
	}
	public void visitFREM(FREM instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.REMAINDER);
	}
	public void visitIREM(IREM instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.REMAINDER);
	}
	public void visitLREM(LREM instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.REMAINDER);
	}
	
	//negate operations
	protected void processNegation(Type type) {
		//negation is the same as multiplying by negative 1; more readable.
		//push a negative 1 to the stack.
		Expression negativeOne = new Resolved(context.getCurrentInstruction(), type, "-1");
		context.getExpressions().push(negativeOne);
		
		processArithmeticTwoStackOperations(ArithmeticType.MULTIPLY);
	}
	public void visitDNEG(DNEG instruction) {
		processNegation(Type.DOUBLE);
	}
	public void visitFNEG(FNEG instruction) {
		processNegation(Type.FLOAT);
	}
	public void visitLNEG(LNEG instruction) {
		processNegation(Type.LONG);
	}
	public void visitINEG(INEG instruction) {
		processNegation(Type.INT);
	}

	//and operations
	public void visitIAND(IAND instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.BITWISE_AND);
	}
	public void visitLAND(LAND instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.BITWISE_AND);
	}

	//shift operations
	
	//shift right unsigned operations
	public void visitIUSHR(IUSHR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SHIFT_RIGHT_UNSIGNED);
	}
	public void visitLUSHR(LUSHR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SHIFT_RIGHT_UNSIGNED);
	}

	//shift right signed
	public void visitISHR(ISHR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SHIFT_RIGHT_SIGNED);
	}
	public void visitLSHR(LSHR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SHIFT_RIGHT_SIGNED);
	}

	//shift left
	public void visitLSHL(LSHL instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SHIFT_LEFT);
	}
	public void visitISHL(ISHL instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.SHIFT_LEFT);
	}
	
	//xor operations
	public void visitLXOR(LXOR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.BITWISE_XOR);
	}
	public void visitIXOR(IXOR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.BITWISE_XOR);
	}

	//or operations
	public void visitIOR(IOR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.BITWISE_OR);
	}
	public void visitLOR(LOR instruction) {
		processArithmeticTwoStackOperations(ArithmeticType.BITWISE_OR);
	}

	
	//array store instruction
	public void visitLASTORE(LASTORE instruction) {
		//long
		processArrayStore();
	}
	public void visitDASTORE(DASTORE instruction) {
		//double
		processArrayStore();
	}
	public void visitBASTORE(BASTORE instruction) {
		//byte
		processArrayStore();
	}
	public void visitCASTORE(CASTORE instruction) {
		//character
		processArrayStore();
	}
	public void visitSASTORE(SASTORE instruction) {
		//short
		processArrayStore();
	}
	public void visitIASTORE(IASTORE instruction) {
		//int
		processArrayStore();
	}
	public void visitFASTORE(FASTORE instruction) {
		//float
		processArrayStore();
	}
	public void visitAASTORE(AASTORE instruction) {
		//object
		processArrayStore();
	}
	public void visitArrayInstruction(ArrayInstruction instruction) {
		// TODO Auto-generated method stub
	}
	
	protected void processArrayStore() {
		Expression value = context.getExpressions().pop();
		Expression arrayPosition = context.getExpressions().pop();
		Expression arrayReference = context.getExpressions().pop();
		
		ArrayAccess arrayPositionReference = new ArrayAccess(context.getCurrentInstruction(), arrayReference, arrayPosition);
		Assignment assignment = new Assignment(context.getCurrentInstruction(), arrayPositionReference, value);
		
		StatementIntermediate si = new StatementIntermediate(context.getCurrentInstruction(), assignment);
		
		//add it to the intermediate lines.
		context.pushIntermediateToInstruction(si);
	}

	//array length instruction
	public void visitARRAYLENGTH(ARRAYLENGTH instruction) {
		Expression target = context.getExpressions().pop();
		ArrayLength arrayLength = new ArrayLength(context.getCurrentInstruction(), target);
		
		context.getExpressions().push(arrayLength);
	}

	//create array.
	public void visitAllocationInstruction(AllocationInstruction instruction) {
		//handle in subtype
	}
	
	/**
	 * Decompiles "new primitive array" operations. 
	 */
	public void visitNEWARRAY(NEWARRAY instruction) {
		//first, check to see if the next instruction is a DUP.  If so,
		//this is probably a constant array value.
		Expression count = context.getExpressions().pop();
		NewArrayInstance nai = null;
		
		if(context.getCurrentInstruction().getNext().getInstruction() instanceof DUP) {
			nai = new NewConstantArrayInstance(context.getCurrentInstruction(), instruction.getType(), count);
		}
		else {
			nai = new NewArrayInstance(context.getCurrentInstruction(), instruction.getType(), count);
		}
		
		context.getExpressions().push(nai);	
		
	}
	
	/**
	 * Decompiles "new object array" operations. 
	 */
	public void visitANEWARRAY(ANEWARRAY instruction) {
		Type type = instruction.getType(context.getMethodGen().getConstantPool());
		Expression count = context.getExpressions().pop();

		NewArrayInstance nai = null;
		
		if(context.getCurrentInstruction().getNext().getInstruction() instanceof DUP) {
			nai = new NewConstantArrayInstance(context.getCurrentInstruction(), type, count);
		}
		else {
			nai = new NewArrayInstance(context.getCurrentInstruction(), type, count);
		}
		
		context.getExpressions().push(nai);
	}
	
	/***
	 * Decompiles "new multi-dimentional array" operations.
	 */
	public void visitMULTIANEWARRAY(MULTIANEWARRAY instruction) {
		Type type = instruction.getType(context.getMethodGen().getConstantPool());
		
		LinkedList<Expression> counts = new LinkedList<Expression>();
		int provided = instruction.getDimensions();
		
		
		
		for(int i=0; i<provided; i++) {
			counts.addFirst(context.getExpressions().pop());
		}
		
		NewArrayInstance nai = new NewArrayInstance(context.getCurrentInstruction(), type, counts);
		context.getExpressions().push(nai);
	}
	
	//array load operations
	protected void processArrayLoad() {
		Expression arrayPosition = context.getExpressions().pop();
		Expression arrayObject = context.getExpressions().pop();
		
		//now, we just need to create the array reference.
		
		ArrayAccess apr = new ArrayAccess(context.getCurrentInstruction(), arrayObject, arrayPosition);
		context.getExpressions().push(apr);
	}
	
	public void visitSALOAD(SALOAD instruction) {
		processArrayLoad();
	}
	public void visitLALOAD(LALOAD instruction) {
		processArrayLoad();
	}
	public void visitIALOAD(IALOAD instruction) {
		processArrayLoad();
	}
	public void visitDALOAD(DALOAD instruction) {
		processArrayLoad();
	}
	public void visitCALOAD(CALOAD instruction) {
		processArrayLoad();
	}
	public void visitBALOAD(BALOAD instruction) {
		processArrayLoad();
	}
	public void visitAALOAD(AALOAD instruction) {
		processArrayLoad();
	}
	public void visitFALOAD(FALOAD instruction) {
		processArrayLoad();
	}

	public void visitNOP(NOP instruction) {
		LOG.debug("No operation expression: "+instruction);
	}

	public void visitPopInstruction(PopInstruction instruction) {
		// TODO Auto-generated method stub
	}

	public void visitPOP(POP instruction) {
		LOG.debug("Pop.");
		context.getExpressions().pop();
	}
	public void visitPOP2(POP2 instruction) {
		LOG.debug("Double Pop.");
		context.getExpressions().pop();
		context.getExpressions().pop();
	}

	public void visitI2F(I2F instruction) {
		// fall through to conversion
	}
	public void visitI2D(I2D instruction) {
		// fall through to conversion
	}
	public void visitL2I(L2I instruction) {
		// fall through to conversion
	}
	public void visitF2L(F2L instruction) {
		// fall through to conversion
	}
	public void visitL2F(L2F instruction) {
		// fall through to conversion
	}
	public void visitF2D(F2D instruction) {
		// fall through to conversion
	}
	public void visitI2L(I2L instruction) {
		// fall through to conversion
	}
	public void visitI2S(I2S instruction) {
		// fall through to conversion
	}
	public void visitD2L(D2L instruction) {
		// fall through to conversion
	}
	public void visitL2D(L2D instruction) {
		// fall through to conversion
	}
	public void visitRET(RET instruction) {
		// fall through to conversion
	}
	public void visitD2I(D2I instruction) {
		// fall through to conversion
	}
	public void visitD2F(D2F instruction) {
		//fall through to conversion
	}
	public void visitF2I(F2I instruction) {
		// // fall through to conversion
	}
	public void visitI2B(I2B instruction) {
		// fall through to conversion
	}
	public void visitI2C(I2C instruction) {
		// fall through to conversion
	}

	public void visitConversionInstruction(ConversionInstruction instruction) {
		ConstantPoolGen cpg = context.getMethodGen().getConstantPool();
		
		Expression right = context.getExpressions().pop();
		Type type = instruction.getType(cpg);
		
		//now see what type it is.
		LOG.debug("To Type: "+type);
		
		Resolved resolve = new Resolved(context.getCurrentInstruction(), type, type.toString());
		
		Cast cast = new Cast(context.getCurrentInstruction(), resolve, right);
		context.getExpressions().push(cast);
	}
	


	public void visitBranchInstruction(BranchInstruction instruction) {
		//handle in subtypes.
	}

	public void visitLoadClass(LoadClass instruction) {
		//handled in subtypes.
	}

	public void visitFieldInstruction(FieldInstruction instruction) {
		//handle in subtypes.
		
	}

	public void visitTypedInstruction(TypedInstruction instruction) {
		//handle in subtypes.
		
	}

	public void visitSelect(Select instruction) {
		//handle in subtypes.
	}


	public void visitUnconditionalBranch(UnconditionalBranch instruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitPushInstruction(PushInstruction instruction) {
		//handle in subtype.
	}

	public void visitCPInstruction(CPInstruction instruction) {
		//handle in subtype.
	}

	
	public void visitATHROW(ATHROW instruction) {
		Expression expression = context.getExpressions().pop();
		Throw throwException = new Throw(context.getCurrentInstruction(), expression);
		
		StatementIntermediate complete = new StatementIntermediate(context.getCurrentInstruction(), throwException);
		context.pushIntermediateToInstruction(complete);
	}


	
	public void visitVariableLengthInstruction(VariableLengthInstruction instruction) {
		//handle in subtype
	}

	public void visitStackProducer(StackProducer instruction) {
		//handle in subtype.
		
	}

	public void visitStackConsumer(StackConsumer instruction) {
		//handle in subtype.
		
	}

	public void visitCHECKCAST(CHECKCAST instruction) {
		//don't need to handle for decompile.
	}

	public void visitFieldOrMethod(FieldOrMethod instruction) {
		//handle in subtype
	}

	public void visitExceptionThrower(ExceptionThrower instruction) {
		//handle in subtype
	}
	
	public void visitStackInstruction(StackInstruction instruction) {
		//handle in subtype
	}

	public void visitLocalVariableInstruction(LocalVariableInstruction instruction) {
		//handle in subtype
	}

	
	
	//synchronized block
	public void visitMONITOREXIT(MONITOREXIT instruction) {
		// TODO Auto-generated method stub
		
	}
	public void visitMONITORENTER(MONITORENTER instruction) {
		// TODO Auto-generated method stub
		
	}

	public void visitJsrInstruction(JsrInstruction instruction) {
		// TODO Auto-generated method stub
	}
	public void visitJSR(JSR instruction) {
		// TODO Auto-generated method stub
	}
	public void visitJSR_W(JSR_W instruction) {
		// TODO Auto-generated method stub
	}
	

	protected void processComparator() {
		Expression left = context.getExpressions().pop();
		Expression right = context.getExpressions().pop();

		MultiConditional conditional = new MultiConditional(context.getCurrentInstruction(), left, right, OperationType.EQ);
		BooleanBranchIntermediate line = new BooleanBranchIntermediate(this.context.getCurrentInstruction(), conditional);
		context.pushIntermediateToInstruction(line);
	}
	
	public void visitDCMPG(DCMPG instruction) {
		processComparator();
	}
	public void visitDCMPL(DCMPL instruction) {
		processComparator();
	}
	public void visitFCMPG(FCMPG instruction) {
		processComparator();
	}
	public void visitFCMPL(FCMPL instruction) {
		processComparator();
	}
	public void visitLCMP(LCMP instruction) {
		Expression left = context.getExpressions().pop();
		Expression right = context.getExpressions().pop();
		
		MultiConditional eq = new MultiConditional(context.getCurrentInstruction(), left, right, OperationType.EQ);
		MultiConditional logic = new MultiConditional(context.getCurrentInstruction(), left, right, OperationType.GREATER);
		
		Resolved r0 = new Resolved(context.getCurrentInstruction(), Type.INT, "0");
		Resolved rN = new Resolved(context.getCurrentInstruction(), Type.INT, "-1");
		Resolved rP = new Resolved(context.getCurrentInstruction(), Type.INT, "1");
		
		Ternary tern2 = new Ternary(context.getCurrentInstruction(), ObjectType.INT, logic, rP, rN);
		Ternary tern1 = new Ternary(context.getCurrentInstruction(), Type.INT, eq, r0, tern2);
		
		context.getExpressions().push(tern1);
	}



	public void visitLOOKUPSWITCH(LOOKUPSWITCH instruction) {
		handleSwitch();
	}

	public void visitTABLESWITCH(TABLESWITCH instruction) {
		handleSwitch();
	}

	public void handleSwitch() {
		Expression switchVal = context.getExpressions().pop();
		Switch switchExpression = new Switch(context.getCurrentInstruction(), switchVal);

		MultiBranchIntermediate mbi = new MultiBranchIntermediate(context.getCurrentInstruction(), switchExpression);
		context.pushIntermediateToInstruction(mbi);
	}
	
	public void visitIMPDEP1(IMPDEP1 instruction) {
		//Not used: https://www.vmth.ucdavis.edu/incoming/Jasmin/ref-impdep1.html
	}


	public void visitIMPDEP2(IMPDEP2 instruction) {
		//Not used: https://www.vmth.ucdavis.edu/incoming/Jasmin/ref-impdep1.html
	}

	public void visitBREAKPOINT(BREAKPOINT instruction) {
		//Not used: https://www.vmth.ucdavis.edu/incoming/Jasmin/ref--5.html
	}

	public void printExpressions() {
		if(LOG.isDebugEnabled()) {
			List<Expression> currentStack = new ArrayList<Expression>(context.getExpressions());
			LOG.debug("Current Expression Stack: ");
			for(Expression e : currentStack) {
				LOG.debug("Expression Stack: "+e);
			}
		}
	}

	protected String getClassName(ConstantClass constClass, ConstantPool cp) {
		String classVal = constClass.getConstantValue(cp).toString();
		StringBuilder builder = new StringBuilder(StringUtils.replace(classVal, "/", "."));
		builder.append(".class");
		
		return builder.toString();
	}

}
