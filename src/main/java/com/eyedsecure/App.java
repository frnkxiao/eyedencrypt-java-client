package com.eyedsecure;

import java.io.File;

/**
 * App
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	EyedEncryptClient client = new EyedEncryptClient("SharedSecret");
    	
    	Response response = client.requestHash("test", "tes2t");    	
    	
    	
    	if (response.getResponseCode() != ResponseCode.SUCCESS) {
    		System.out.println(response.getResponseCode().toString());
    		return;
    	}
    	
    	String hashCode = Base64Converter.decode(client.getHashCode());
    	
    	System.out.println(hashCode);
    	
    	// Encryption or decryption algorithms: DES / DESede / AES
    	FileEncrypter.setAlgo("AES/ECB/PKCS5Padding");
    	
    	File file = new File("C:/test/test.txt");
    	ResponseCode rc = client.encryptDoc(file, "12345");
    	
    	System.out.println(rc.toString());
    	
    	file = new File("C:/test/test.txt.enc");
    	rc = client.decryptDoc(file, "12345");
    	
    	System.out.println(rc.toString());

    }
}
