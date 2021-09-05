
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * The Class GET_REQUEST.
 * @author Devi
 */

public class Get_Request {
	
	@Test
	public void getSpacexLaunchDetails() throws Exception {
		//base URL
		RestAssured.baseURI="https://api.spacexdata.com/v4/launches/latest";
		
		//Request object
		RequestSpecification httpreuqest=RestAssured.given();
		
		//Response object
		Response response=httpreuqest.request(Method.GET);
		
		//Basic Validation		
		 basicValidation(response);
		 
		//Parse the response
		 String responsebody =response.getBody().asString();
		 JsonPath jsonPath = new JsonPath(responsebody);
		 JSONParser parser = new JSONParser();  
		 JSONObject json = (JSONObject) parser.parse(responsebody);  
		 printJsonObject(json);
		 
		//get value based on parameter
		 String Value =getvaluefromList(jsonPath, "cores","landpad");
		 System.out.println("Values from the list:"+Value);
				
	}
	//method used to do basic validation of response
	public void basicValidation(Response response) {
		long responsetime=response.getTime();
		System.out.println("Response Time in milli seconds :"+responsetime);
		
		String contentType =response.getContentType();
		System.out.println("contentType:"+contentType);
		
		int responseStatusCode=response.getStatusCode();
		System.out.println("Response Status code:"+responseStatusCode);
		Assert.assertEquals(responseStatusCode, 200);
		
		
		String responseStatusLine=response.getStatusLine();
		System.out.println("Response Status line:"+responseStatusLine);
		Assert.assertEquals(responseStatusLine, "HTTP/1.1 200 OK");
	
	}
	
	//Method used to print all keyes in response
	public static void printJsonObject(JSONObject jsonObj) {
	    for (Object key : jsonObj.keySet()) {
	        //based on you key types
	        String keyStr = (String)key;
	        Object keyvalue = jsonObj.get(keyStr);
	        //Print key and value
	        System.out.println("key: "+ keyStr + " value: " + keyvalue);
	        //for nested objects iteration if required
	        if (keyvalue instanceof JSONObject)
	            printJsonObject((JSONObject)keyvalue);
	    }
	    
	}
	 //gets from value from the list
	private static String getvaluefromList(JsonPath path, String list, String Value) {
        List<HashMap<String, Object>> data = path.getList(list);
        for (HashMap<String, Object> object1 : data) {
           return (String) object1.get(Value);         
        }
        throw new NoSuchElementException(String.format("value is not found for: ", list));
    }
}
		