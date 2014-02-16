package com.eyedsecure;

import java.util.Map;
public class Response {

    private String hashCode;                    // hash code
    private String sig;                         // Signature
    private String serverTimeStamp;             // Server timestamp in UTC
    private ResponseCode responseCode;          // Response

    private Map<String, String> keyValueMap;    // Map of response properties


	/**
	 * Signature of the response, with the same API key as the request.
	 * 
	 * @return response signature
	 */
    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    /**
     *
     * Server response to the request.
     * 
     * @return response status
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
    
    /**
     * Returns all parameters from the response as a Map
     * 
     * @return map of all values
     */
    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    public void setKeyValueMap(Map<String, String> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }

    public String getServerTimeStamp() {
        return serverTimeStamp;
    }

    public void setServerTimeStamp(String serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}


}
