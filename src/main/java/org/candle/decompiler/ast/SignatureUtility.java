package org.candle.decompiler.ast;

import org.apache.commons.lang.StringUtils;

import com.sun.org.apache.bcel.internal.classfile.Utility;

public class SignatureUtility {

	public static String signatureToString(String signature) {
		String util = Utility.signatureToString(signature);
		util = StringUtils.removeStart(util, "java.lang.");
		
		return util;
	}
}
