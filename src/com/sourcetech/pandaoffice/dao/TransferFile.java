package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;

public class TransferFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3697634560295405223L;
	
	private File info;
	private byte[] file;
	private RSAInfo rsaInfo;
	
	public File getInfo() {
		return info;
	}
	public void setInfo(File info) {
		this.info = info;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public RSAInfo getRsaInfo() {
		return rsaInfo;
	}
	public void setRsaInfo(RSAInfo rsaInfo) {
		this.rsaInfo = rsaInfo;
	}
	
	

}
