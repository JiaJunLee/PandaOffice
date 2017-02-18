package com.sourcetech.pandaoffice.dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.sourcetech.pandaoffice.util.FileUtil;
import com.sourcetech.pandaoffice.util.HTTPUtil;
import com.sourcetech.pandaoffice.util.safe.EncryptFactory;
import com.sourcetech.pandaoffice.util.safe.Panda_RSA_Algorithm;

public class FileUploadTest {

	public static void main(String[] args) {	
//		try {
			
			java.io.File file = new java.io.File("C:\\Users\\李佳骏\\Desktop\\测试用例.xlsx");
			System.out.println(file.getName());
//			byte[] fileByte = FileUtil.getContent(file);
//			
//			File fileInfo = new File();
//			
//			
//			fileInfo.setId(UUID.randomUUID().toString());
//			fileInfo.setMd5(EncryptFactory.toHexString(EncryptFactory.getInstance().toHashBytes(fileByte, EncryptFactory.MD5_ALGORITHM)));
//			fileInfo.setName("测试用例");
//			fileInfo.setSize((double)fileByte.length/1024/1024);
//			fileInfo.setType("xlsx");
//			
//			TransferFile transferFile = new TransferFile();
//			transferFile.setFile(fileByte);
//			transferFile.setInfo(fileInfo);
//			
//			JsonObject object = new JsonObject();
//			object.addProperty("user_id", 4);
//			object.addProperty("project_id", 1);
//			RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(object);
//			transferFile.setRsaInfo(rsaInfo);
//			
//			HTTPUtil.request(new URL("http://localhost:8080/PandaOfficeServer/upload"), transferFile);
//			
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//b23ab85a-1faa-4353-a0d5-05316833aac1
	}

}
