package org.apache.bcel.generic;


public class DuplicateHandle extends InstructionHandle {

	private static final long serialVersionUID = -6019766747175365797L;
	protected final InstructionHandle original;
	
	public DuplicateHandle(InstructionHandle ih) {
		super(ih.getInstruction());
		this.i_position = ih.getPosition();
		this.next = ih.getNext();
		this.prev = ih.getPrev();
		this.original = ih;
	}
}
