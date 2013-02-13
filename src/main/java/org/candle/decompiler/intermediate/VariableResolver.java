package org.candle.decompiler.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.expression.NewArrayInstance;

public class VariableResolver {
	private static final Log LOG = LogFactory.getLog(VariableResolver.class);
	
	private final Map<NewArrayInstance, IntermediateVariable> localGeneratedArrayVariables = new HashMap<NewArrayInstance, IntermediateVariable>();
	
	private final Map<VariableIndex, IntermediateVariable> localGeneratedVariables = new HashMap<VariableIndex, IntermediateVariable>();
	private final MethodGen methodGen;

	public VariableResolver(MethodGen methodGen) {
		this.methodGen = methodGen;
	}
	
	public void addLocalArrayVariable(NewArrayInstance nai) {
		String name = RandomStringUtils.randomAlphabetic(3) + "$";
		IntermediateVariable iv = new IntermediateVariable(name, null);
		localGeneratedArrayVariables.put(nai, iv);
	}
	
	public IntermediateVariable getLocalArrayVariable(NewArrayInstance nai) {
		if(localGeneratedArrayVariables.containsKey(nai)) {
			return localGeneratedArrayVariables.get(nai);
		}
		return null;
	}
	
	public IntermediateVariable getLocalVariable(int index, int pc) {
		IntermediateVariable iv = null;
		LocalVariableGen lv = getLocalVariableTable(index, pc);
		if(lv != null) {
			iv = new IntermediateVariable(lv.getName(), lv.getType());
		}
		else {
			for(VariableIndex vi : localGeneratedVariables.keySet()) {
				if(vi.withinBounds(index, pc)) {
					return localGeneratedVariables.get(vi);
				}
			}
		}
		
		return iv;
	}
	
	public IntermediateVariable addLocalVariable(int index, InstructionHandle ih, String name, Type type) {
		IntermediateVariable iv = new IntermediateVariable(name, type);
		VariableIndex varIndex = VariableIndex.Factory.createFromInstructionHandle(ih, index);
		LOG.info("Adding index: "+varIndex);
		localGeneratedVariables.put(varIndex, iv);
		
		return iv;
	}
	
	public IntermediateVariable addLocalVariable(int index, InstructionHandle ih, Type type) {
		String name = RandomStringUtils.randomAlphabetic(3) + "$";
		LOG.info("Adding variable: "+index + " type: "+type + " name: "+name);
		return addLocalVariable(index, ih, name, type);
	}
	
    protected LocalVariableGen getLocalVariableTable(int index, int pc) {
    	final LocalVariableGen[] local_variable_table = methodGen.getLocalVariables();
    	
    	 for (int i = 0, j = local_variable_table.length; i < j; i++) {
             if (local_variable_table[i].getIndex() == index) {
                 int start_pc = local_variable_table[i].getStart().getPosition();
                 int end_pc = local_variable_table[i].getEnd().getPosition();
                 if ((pc >= start_pc) && (pc <= end_pc)) {
                     return local_variable_table[i];
                 }
             }
         }
         return null;
    }
	
}
