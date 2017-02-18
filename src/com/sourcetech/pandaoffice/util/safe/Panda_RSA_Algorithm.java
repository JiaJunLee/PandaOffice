package com.sourcetech.pandaoffice.util.safe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sourcetech.pandaoffice.dao.RSAInfo;
import com.sourcetech.pandaoffice.detail.Res;


public class Panda_RSA_Algorithm {

	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";

	private static final int RSA_KEY_SIZE = 64 * 32;
	private static final int RSA_MAX_SIZE = 122;

	private static final String KEY_ALGORITHM = "RSA";
	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	
	public static PublicKey restorePublicKey(byte[] keyBytes) {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] RSAEncodeByPublicKey(PublicKey key, String plainText) {
		int blockSize = plainText.length() / RSA_MAX_SIZE;
		blockSize = plainText.length() % RSA_MAX_SIZE == 0 ? blockSize : blockSize + 1;
		String[] textBlock = new String[blockSize];
		String[] encodes = new String[blockSize];
		for (int i = 0; i < blockSize; i++) {
			int endIndex;
			if (i == (blockSize - 1))
				endIndex = plainText.length();
			else
				endIndex = (i + 1) * RSA_MAX_SIZE;
			textBlock[i] = plainText.substring(i * RSA_MAX_SIZE, endIndex);
		}
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			for (int i = 0; i < encodes.length; i++) {
				encodes[i] = Base64.encode(cipher.doFinal(textBlock[i].getBytes()));
			}
			return encodes;
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String sign(byte[] data, PrivateKey privateKey){
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			return Base64.encode(signature.sign());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, byte[]> loadKeys(File file) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));

			Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
			keyMap.put(PUBLIC_KEY, hexStringToBytes(String.valueOf(properties.get(PUBLIC_KEY))));
			keyMap.put(PRIVATE_KEY, hexStringToBytes(String.valueOf(properties.get(PRIVATE_KEY))));

			return keyMap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String bytesToHexString(byte data[]) {
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0xff & data[i]);
			if (hex.length() == 1) {
				strBuffer.append('0');
			}
			strBuffer.append(hex);
		}
		return strBuffer.toString();
	}
	
	public static Map<String, byte[]> clientKeyMap = loadKeys(new File(Res.Config(Res.Client_Encrypt_Config)));
	
	public static RSAInfo buildRSAInfo(JsonObject jsonObject){
		try {
			RSAInfo rsaInfo = new RSAInfo();
			rsaInfo.setEncode(RSAEncodeByPublicKey(restorePublicKey(clientKeyMap.get(PUBLIC_KEY)), URLEncoder.encode(jsonObject.toString(),"UTF-8")));
			rsaInfo.setSign(sign(jsonObject.toString().getBytes("UTF-8"), restorePrivateKey(clientKeyMap.get(PRIVATE_KEY))));
			return rsaInfo;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}

