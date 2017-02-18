package com.sourcetech.pandaoffice.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sourcetech.pandaoffice.dao.Member;

public class HTTPUtil {

	private static final String REQUEST_METHOD = "POST";
	private static final int DEFALUT_TIMEOUT = 10 * 1000;

	public static Object request(URL url, Object object) {
		HttpURLConnection connection = null;
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		Object resultObject = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod(REQUEST_METHOD);
			connection.setConnectTimeout(DEFALUT_TIMEOUT);
			connection.setReadTimeout(DEFALUT_TIMEOUT);
			objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
			objectOutputStream.writeObject(object);
			objectInputStream = new ObjectInputStream(connection.getInputStream());
			resultObject = objectInputStream.readObject();
			objectOutputStream.close();
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return resultObject;
	}

}
