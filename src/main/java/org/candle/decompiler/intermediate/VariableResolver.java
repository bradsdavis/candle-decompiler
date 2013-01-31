package org.candle.decompiler.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VariableResolver {
	private static final Log LOG = LogFactory.getLog(VariableResolver.class);
	
	private final Map<Integer, IntermediateVariable> localGeneratedVariables = new HashMap<Integer, IntermediateVariable>();
	private final MethodGen methodGen;

	public VariableResolver(MethodGen methodGen) {
		this.methodGen = methodGen;
	}
	
	public IntermediateVariable getLocalVariable(int index, int pc) {
		IntermediateVariable iv = null;
		LocalVariableGen lv = getLocalVariableTable(index, pc);
		if(lv != null) {
			iv = new IntermediateVariable(lv.getName(), lv.getType());
		}
		else {
			iv = localGeneratedVariables.get(index);
		}
		
		return iv;
	}
	
	public IntermediateVariable addLocalVariable(int index, String name, Type type) {
		IntermediateVariable iv = new IntermediateVariable(name, type);
		localGeneratedVariables.put(index, iv);
		
		return iv;
	}
	
	public IntermediateVariable addLocalVariable(int index, Type type) {
		String name = RandomStringUtils.randomAlphabetic(3) + "$";
		LOG.info("Adding variable: "+index + " type: "+type + " name: "+name);
		return addLocalVariable(index, name, type);
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
