package com.sourcetech.pandaoffice.dao;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetech.pandaoffice.util.HTTPUtil;
import com.sourcetech.pandaoffice.util.safe.Panda_RSA_Algorithm;

public class FileDownTest {
	
	public static void main(String[] args) {
//		JsonObject requestInfo = new JsonObject();
//		requestInfo.addProperty("file_id", "b23ab85a-1faa-4353-a0d5-05316833aac1");
//		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
//		try {
//			TransferFile transferFile = (TransferFile) HTTPUtil.request(new URL("http://localhost:8080/PandaOfficeServer/down"), rsaInfo);
//			System.out.println(transferFile.getFile().length);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
		
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("user_id", "4");
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		try {
			JsonParser jsonParser = new JsonParser();
			String str = (String) HTTPUtil.request(new URL("http://localhost:8080/PandaOfficeServer/project"), rsaInfo);
			JsonObject obj = (JsonObject) jsonParser.parse(str);
			JsonArray array = obj.get("projects").getAsJsonArray();
			for(JsonElement e : array){
				System.out.println(e);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
