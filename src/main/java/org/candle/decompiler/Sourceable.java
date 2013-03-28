package org.candle.decompiler;

import java.io.IOException;
import java.io.Writer;

public interface Sourceable  {
	public void write(Writer writer) throws IOException;
}
