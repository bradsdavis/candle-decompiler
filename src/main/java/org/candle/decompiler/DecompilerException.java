package org.candle.decompiler;

public class DecompilerException extends Exception {

	private static final long serialVersionUID = 2982690021822415127L;

	public DecompilerException(String message) {
		super(message);
	}
	
	public DecompilerException(String message, Throwable cause) {
        super(message, cause);
    }
}
