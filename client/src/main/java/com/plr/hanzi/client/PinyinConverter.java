package com.plr.hanzi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PinyinConverter {

	static private Logger LOGGER = Logger.getLogger(PinyinConverter.class.getName());

	private JSONObject pinyinMap = null;

	static private PinyinConverter pinyinConverter = null;

	private PinyinConverter() {
		final String resource = "data/num2pinyin.json";

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, resource);

		requestBuilder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {

				int code = response.getStatusCode();
				if (code < 200 && code >= 400) {
					LOGGER.log(Level.SEVERE, resource + " code http : " + code);
					return;
				}

				JSONValue jValue = JSONParser.parseLenient(response.getText());

				JSONObject jObject = jValue.isObject();

				if (jObject != null) {
					pinyinMap = jObject;
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				LOGGER.log(Level.SEVERE, exception.getMessage());
			}
		});
		
		try {
			requestBuilder.send();
		} catch (RequestException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	public static String getConvert(String pinyinNum) {
		
		String result = pinyinNum;
		
		boolean isUpperCase = Character.isUpperCase(pinyinNum.charAt(0));
		
		pinyinNum = pinyinNum.toLowerCase();
		
		try {
			
			JSONObject map = getPinyinConverter().pinyinMap;
			
			JSONValue jValue = map.get(pinyinNum);
			
			JSONString jString = jValue.isString();
			
			if (isUpperCase) {
				result = jString.stringValue();
								
				return Character.toUpperCase(result.charAt(0)) + result.substring(1);
			}
			
			return jString.stringValue();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		
		return result;
	}
	
	static PinyinConverter getPinyinConverter() {
		if (pinyinConverter == null) {
			pinyinConverter = new PinyinConverter();
		}
		return pinyinConverter;
	}
}
