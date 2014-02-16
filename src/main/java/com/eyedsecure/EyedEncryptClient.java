package com.eyedsecure;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;



public class EyedEncryptClient {

	protected EyedEncryptService service;
	
	private String hashCode;
	
	
	protected byte[] sharedKey;
	
		
	protected String urls[] = {
            "https://localhost:8443/eyedencrypt-server/API",
            "http://localhost:8080/eyedencrypt-server/API",
    };

    protected String userAgent;
    
    
    public EyedEncryptClient(String sharedKey) {
    	setSharedKey(sharedKey);
    	service = new EyedEncryptService();
    }

    
    public Response requestHash(String username, String password) throws RequestException, UnsupportedEncodingException {
    	if (sharedKey == null) throw new IllegalArgumentException("This type of request requires a shared key");

        String paramStr = getParamString(username, password);

        List<String> serverUrls = new ArrayList<String>();
        for (String url : getUrls()) {
            serverUrls.add(url.concat("/requestHash?").concat(paramStr));            
        }
        
        for (String url : serverUrls){
        	System.out.println(url);
        }
        
        Response response = service.fetch(serverUrls, userAgent, new ResponseParser());
        
        // Verify the signature
        StringBuilder keyValueStr = new StringBuilder();
        for (Map.Entry<String, String> entry : response.getKeyValueMap().entrySet()) {
            if ("s".equals(entry.getKey())) {
                continue;
            } else if ("i".equals(entry.getKey())) {

                //MessageDigest digest = MessageDigest.getInstance("MD5")
                //String imageSignature = new BigInteger(1,digest.digest(image)).toString(16);

                continue;
            }
            if (keyValueStr.length() > 0) {
                keyValueStr.append("&");
            }
            keyValueStr.append(entry.getKey()).append("=").append(entry.getValue());
        }
        try {
            String signature = Signature.calculate(keyValueStr.toString(), sharedKey).trim();
            if (response.getSig() != null && !response.getSig().equals(signature) &&
                    !response.getResponseCode().equals(ResponseCode.BAD_SIGNATURE)) {
                // don't throw a RequestFailure if the server responds with bad signature
                throw new RequestException("Signatures miss-match");
            }

        } catch (SignatureException e) {
            throw new RequestException("Failed to calculate the response signature.", e);
        }


        // All fields are not returned on an error
        // If there is an error response, don't need to check them.
        if (!ResponseCode.isErrorCode(response.getResponseCode())) {
            // Verify server time, for additional security you can verify the UTC timestamp is reasonable
            if (response.getServerTimeStamp() == null) {
                throw new RequestException("Missing server timestamp");
            }

            if (response.getSig() == null) {
            //    throw new RequestException("Missing signature");
            }

        }

        setHashCode(response.getHashCode());

        return response;
    }

    public ResponseCode encryptDoc(File file, String pin) throws Exception{
    	if (!file.exists()) {
    		return ResponseCode.FILE_MISSING;
    	}
    	
    	if (pin.isEmpty()) {
    		return ResponseCode.INVALID_PIN;
    	}
    	
    	if (getHashCode().isEmpty()) {
    		return ResponseCode.SERVER_ERROR;
    	}
    	
    	
    	return FileEncrypter.encrypt(file, getHashCode() + pin);
    }
    
    public ResponseCode decryptDoc(File file, String pin) throws Exception{
    	if (!file.exists()) {
    		return ResponseCode.FILE_MISSING;
    	}
    	
    	if (pin.isEmpty()) {
    		return ResponseCode.INVALID_PIN;
    	}
    	
    	if (getHashCode().isEmpty()) {
    		return ResponseCode.SERVER_ERROR;
    	}
    	
    	
    	return FileEncrypter.decrypt(file, getHashCode() + pin);
    	
    }    
    
    protected String getParamString(String username, String password) throws RequestException, UnsupportedEncodingException {
        Map<String, String> reqMap = new HashMap<String, String>();
        if(username!=null) reqMap.put("un", Base64Converter.encode(username));
        if(password!=null) reqMap.put("pw", Base64Converter.encode(password));
        
        StringBuilder paramStrBuilder = new StringBuilder();
        for (String key : reqMap.keySet()) {
            if (paramStrBuilder.length() != 0) {
                paramStrBuilder.append("&");
            }
            try {
                paramStrBuilder.append(key).append("=").append(URLEncoder.encode(reqMap.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RequestException("Failed to encode parameter.", e);
            }
        }

        String paramStr = paramStrBuilder.toString();

        String s;
        try {
            s = URLEncoder.encode(Signature.calculate(paramStr, sharedKey), "UTF-8");
        } catch (SignatureException e) {
            throw new RequestException("Failed signing of request", e);
        } catch (UnsupportedEncodingException e) {
            throw new RequestException("Failed to encode signature", e);
        }
        return paramStr.concat("&s=" + s);

    }

    
	public String[] getUrls() {
		return urls;
	}


	public void setUrls(String[] urls) {
		this.urls = urls;
	}


	public String getUserAgent() {
		return userAgent;
	}


	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	
	public void setSharedKey(String sharedKey) {
        this.sharedKey = Base64.decodeBase64(sharedKey.getBytes());
    }

    public String getSharedKey() {
        return new String(Base64.encodeBase64(this.sharedKey));
    }


	public String getHashCode() {
		return hashCode;
	}


	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	
}
