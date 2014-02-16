package com.eyedsecure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

/**
 * Parse response from server
 * 
 * User: Frank
 * Date: 2/3/14
 */
public class ResponseParser {

    public Response parse(InputStream inputStream) throws IOException {
    	if(inputStream == null) {
            throw new IllegalArgumentException("InputStream argument was null");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        // We use a TreeMap so we get consistent signature
        Map<String, String> responseMap = new TreeMap<String, String>();
        String line;
        while ((line = in.readLine()) != null) {
            int delimiterIndex=line.indexOf("=");
            if(delimiterIndex==-1) continue; // Malformed line
            String key=line.substring(0,delimiterIndex);
            String val=line.substring(delimiterIndex+1);
            responseMap.put(key, val);
        }
        in.close();

        return parse(new Response(), responseMap);

    }
    
    protected Response parse( Response response, Map<String, String> responseMap) {
               
    	response.setKeyValueMap(responseMap);
        for(String key: responseMap.keySet()) {
            String val = responseMap.get(key);

            if(key.equals("hc")) {
                response.setHashCode(val);
            } else if(key.equals("s")) {
                response.setSig(val);
            } else if ("t".equals(key)) {
                response.setServerTimeStamp(val);
            } else if (key.equals("c") && val.length()>0)  {
                response.setResponseCode(ResponseCode.valueOf(val));
            }
        }


        return response;

    }


}
