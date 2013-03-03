package org.candle.decompiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.commons.lang.StringUtils;

import com.sun.org.apache.bcel.internal.classfile.Utility;

public class ClassBlock extends Block {

	private final JavaClass javaClass;
	private final Set<String> imports = new TreeSet<String>();
	private final Set<String> fields = new TreeSet<String>();
	
	private String packageName; 
	private String className;
	private String superClassName;
	
	public ClassBlock(JavaClass javaClass) {
		this.javaClass = javaClass;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getSuperClassName() {
		return superClassName;
	}
	
	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	 
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public Set<String> getImports() {
		return imports;
	}
	
	public Set<String> getFields() {
		return fields;
	}

	@Override
	public int getStartBlockPosition() {
		throw new IllegalStateException();
	}
	
	@Override
	public int getEndBlockPosition() {
		throw new IllegalStateException();
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		builder.append("package ").append(packageName);
		builder.append(Block.NL);
		builder.append(Block.NL);
		
		for(String imp : imports) {
			builder.append("import ").append(imp).append(";");
			builder.append(Block.NL);
		}
		
		builder.append(Block.NL);
		builder.append(Block.NL);
		
		builder.append(extractClassSignature());

		builder.append("{");
		
		for(String field : this.fields) {
			builder.append(NL).append(TAB).append(field).append(";");
		}
		builder.append(Block.NL);
		
		for(Block child : children) {
			builder.append(NL);
			child.write(builder);
		}
		builder.append(NL);
		builder.append("}");
		builder.append(NL);
	}
	
	protected String extractClassSignature() {
		String access = Utility.accessToString(javaClass.getAccessFlags(), true);
		access = access.equals("") ? "" : (access + " ");

		StringBuilder builder = new StringBuilder(access);
		
		String className = StringUtils.substringAfterLast(javaClass.getClassName(), ".");
		builder.append(Utility.classOrInterface(javaClass.getAccessFlags()) + " " + className);
		
		if(StringUtils.isNotBlank(this.getSuperClassName()))
		{
			builder.append(" extends ");
			builder.append(Utility.compactClassName(javaClass.getSuperclassName(), false));
		}
		builder.append(Block.NL);
		
		int numInterfaces = javaClass.getInterfaceNames().length;

		if (numInterfaces > 0) {
			builder.append("implements ");

			for (int i = 0; i < numInterfaces; i++) {
				builder.append(javaClass.getInterfaceNames()[i]);
				if (i < numInterfaces - 1)
					builder.append(", ");
			}

			builder.append(" ");
		}
		
		return builder.toString();
	}
	
	
	
	@Override
	public int countPathToRoot() {
		return 0;
	}

}
