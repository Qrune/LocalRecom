package ExternalApis;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TicketMasterAPI {
	private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String DEFAULT_KEYWORD = ""; // no restriction
    private static final String API_KEY = "p0Y30NNlguN1zoBhAVogOLVQnzCI4thD&";
    private static final String RADIUS = "5";
    public JSONArray search(double lat, double lon, String keyword) {
    	if (keyword == null) {
    		keyword = DEFAULT_KEYWORD;
    	}
    	//remove space in the keyword
    	try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// Get Hash
    	String geoHash = GeoHash.encodeGeohash(lat, lon, 8);
    	
    	
    	//format the query string
    	String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=%s", API_KEY,geoHash,keyword,RADIUS);
    	
    	//send request
    	try {
			HttpURLConnection connection = (HttpURLConnection) new URL(URL + "?" + query).openConnection();
			//set request method
			connection.setRequestMethod("GET");
			
			//Get the response Code
			int responseCode = connection.getResponseCode();
			
			//Debug
			System.out.println("Sending 'GET' request to URL: " + URL);
			System.out.println("Response Code: " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
			
			
			
			JSONObject obj = new JSONObject(response.toString());
            if (!obj.isNull("_embedded")) {
                JSONObject embbeded = obj.getJSONObject("_embedded");
                return embbeded.getJSONArray("events");
            }
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return new JSONArray();
    }
    
    
    
    //Debug Function
    void queryAPI(double lat, double lon) {
    	JSONArray events = search(lat,lon,null);
    	try {
    		for (int i=0; i<events.length(); i++){
    			JSONObject event = events.getJSONObject(i);
    			System.out.println(event.toString(2));
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
   


}
