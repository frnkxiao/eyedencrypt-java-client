package com.eyedsecure;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

public class Base64Converter {

	static String decode(String str) throws UnsupportedEncodingException {
		
		String text = URLDecoder.decode(str, "UTF-8");
		
		byte[] decoded = Base64.decodeBase64(text.getBytes());		
		
		return new String(decoded,"UTF-8");
	}
	
	
	static String encode(String str) throws UnsupportedEncodingException {
		
		String text = URLEncoder.encode(str, "UTF-8");
		
		byte[] encoded = Base64.encodeBase64(text.getBytes());		
		
		return new String(encoded,"UTF-8");
	}
}
