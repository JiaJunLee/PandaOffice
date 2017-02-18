package com.sourcetech.pandaoffice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetech.pandaoffice.detail.Area;

public class NetworkInfoUtil {
	
	private static InetAddress ia;

	static {
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		try {
//			System.out.println(getLocalMac());
//			System.out.println(getRealIp());
//			System.out.println(getIpArea(getRealIp()).getProvince());
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public static NetworkInfo getNetworkInfo() {
//		NetworkInfo info = new NetworkInfo();
//		try {
//			info.setMac(getLocalMac());
//			info.setIp(getRealIp());
//			info.setCity(getIpArea(info.getIp()));
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return info;
//	}

	public static String getLocalMac() throws SocketException {
		// 获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
	
	public static String requestAsString(String httpUrl) throws IOException {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		URL url = new URL(httpUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		InputStream is = connection.getInputStream();
		reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String strRead = null;
		while ((strRead = reader.readLine()) != null) {
			sbf.append(strRead);
			sbf.append("\r\n");
		}
		reader.close();
		result = sbf.toString();
		return result;
	}

	public static String getRealIp() throws IOException {
		String webContent = requestAsString("http://www.ip138.com/ua.asp");
		int start = webContent.indexOf("[") + 1;
		int end = webContent.indexOf("]");
		return webContent.substring(start, end);
	}
	
	public static Area getIpArea(String ip){
		Area area = new Area();
		try {
			String context = requestAsString("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + ip);
			String json = context.substring(context.indexOf("{"), context.indexOf("}")+1);
			JsonParser parser = new JsonParser();
			JsonObject result = parser.parse(json).getAsJsonObject();
			area.setCountry(result.get("country").getAsString());
			area.setProvince(result.get("province").getAsString());
			area.setCity(result.get("city").getAsString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return area;
	}

}
