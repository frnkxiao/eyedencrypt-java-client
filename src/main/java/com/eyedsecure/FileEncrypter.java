package com.eyedsecure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class FileEncrypter {

	private static String algo;
	
	public static ResponseCode encrypt(File file, String key) throws Exception{
		int keyLength = getKeyLength(algo.split("/")[0]);
				
				
		//opening streams
        FileInputStream fis =new FileInputStream(file);
        file=new File(file.getAbsolutePath()+".enc");
        FileOutputStream fos =new FileOutputStream(file);
        
        //generating key
        
        byte k[] = padKey(key,keyLength);
        
        SecretKeySpec keyS = new SecretKeySpec(k,algo.split("/")[0]);  

        //creating and initializing cipher and cipher streams
        Cipher encrypt =  Cipher.getInstance(algo);  
        encrypt.init(Cipher.ENCRYPT_MODE, keyS);  
        CipherOutputStream cout=new CipherOutputStream(fos, encrypt);
        
        byte[] buf = new byte[1024];
        int read;
        while((read=fis.read(buf))!=-1)  //reading data
            cout.write(buf,0,read);  //writing encrypted data
        //closing streams
        fis.close();
        cout.flush();
        cout.close();
		
		return ResponseCode.SUCCESS;
	}
	
	public static ResponseCode decrypt(File file, String key) throws Exception{
			
		int keyLength = getKeyLength(algo.split("/")[0]);
		
		 //opening streams
        FileInputStream fis =new FileInputStream(file);
        file=new File(file.getAbsolutePath()+".dec");
        FileOutputStream fos =new FileOutputStream(file);               
        
      //generating key
        byte k[] = padKey(key,keyLength);
        
        SecretKeySpec keyS = new SecretKeySpec(k,algo.split("/")[0]);  
        
        //creating and initializing cipher and cipher streams
        Cipher decrypt =  Cipher.getInstance(algo);  
        decrypt.init(Cipher.DECRYPT_MODE, keyS);  
        
        CipherInputStream cin=new CipherInputStream(fis, decrypt);
             
        byte[] buf = new byte[1024];
        int read=0;
        while((read=cin.read(buf))!=-1)  //reading encrypted data
             fos.write(buf,0,read);  //writing decrypted data
        //closing streams
        cin.close();
        fos.flush();
        fos.close();
    
		return ResponseCode.SUCCESS;
	}
	
	public static void setAlgo(String algo) {
		FileEncrypter.algo = algo;
	}
	
	private static byte[] padKey(String key, int keyLength) throws UnsupportedEncodingException {

		if (key.length() < keyLength) {
			StringBuilder sb = new StringBuilder(key);
			int len = key.length();
			
			while (keyLength > len + key.length()) {
				sb.append(key);
				len += key.length();
			}
			
			sb.append(key.substring(0,keyLength - sb.length()));
			
			return sb.toString().getBytes("UTF-8");
		}
		
		return key.substring(0,keyLength).getBytes("UTF-8");
	}
	
	// determine the size of key based on the algorithm
	private static int getKeyLength(String str) {
		if (str.toUpperCase().equals("DES")){
			return 8;
		} else if (str.toUpperCase().equals("DESEDE")) {
			return 24;
		} else if (str.toUpperCase().equals("AES")) {
			return 16;
		}

		return 0;
	}


}
