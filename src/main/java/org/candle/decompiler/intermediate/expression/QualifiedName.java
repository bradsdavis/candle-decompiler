package org.candle.decompiler.intermediate.expression;

public class QualifiedName extends Name {

	private Name qualifier;
	private String value;
	
	public QualifiedName(Name qualifier, String value) {
		super(value);
		
		this.qualifier = qualifier;
	}
	
	public Name getQualifier() {
		return qualifier;
	}
	
	public void setQualifier(Name qualifier) {
		this.qualifier = qualifier;
	}
	
	
	@Override
	public String toString() {
		return qualifier.toString()+"."+value;
	}
}
