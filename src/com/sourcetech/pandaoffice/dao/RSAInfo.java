package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;

public class RSAInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4030767488185271422L;

	private String[] encode;
	private String sign;

	public String[] getEncode() {
		return encode;
	}

	public void setEncode(String[] encode) {
		this.encode = encode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
