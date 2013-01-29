package org.candle.decompiler.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.commons.lang.RandomStringUtils;

public class VariableResolver {

	private final Map<Integer, IntermediateVariable> localGeneratedVariables = new HashMap<Integer, IntermediateVariable>();
	private final MethodGen methodGen;

	public VariableResolver(MethodGen methodGen) {
		this.methodGen = methodGen;
	}
	
	public IntermediateVariable getLocalVariable(int index, int pc) {
		IntermediateVariable iv = null;
		LocalVariableGen lv = getLocalVariableTable(index, pc);
		if(lv != null) {
			String signature = Utility.signatureToString(lv.getType().getSignature(), false);
			iv = new IntermediateVariable(lv.getName(), signature);
		}
		else {
			iv = localGeneratedVariables.get(index);
		}
		
		return iv;
	}
	
	public IntermediateVariable addLocalVariable(int index, String name, String type) {
		IntermediateVariable iv = new IntermediateVariable(name, type);
		localGeneratedVariables.put(index, iv);
		
		return iv;
	}
	
	public IntermediateVariable addLocalVariable(int index, String type) {
		String name = RandomStringUtils.randomAlphabetic(3) + "$";
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
