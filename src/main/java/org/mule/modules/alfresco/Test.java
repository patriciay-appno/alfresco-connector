package org.mule.modules.alfresco;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.mule.api.MuleRuntimeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriTemplate;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			String serverUrl = "http://localhost:8080";
			String shortNameFilter = "ESG";
			String maxItems = "2";
			String zone = null;
			String skipCount = null;
			String sortBy = null;
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/groups");
			URI uri = template.expand(serverUrl);

			GetMethod get = new GetMethod(uri.toString());

			get.addRequestHeader("Authorization",
					ConnectorHelper.setBasicAuthorization("admin", "admin"));

			//HttpMethodParams params = new HttpMethodParams();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
		   
		  
			
		

			if (shortNameFilter != null) {
				
				params.add(new NameValuePair("shortNameFilter", shortNameFilter));
				
			}
			if (zone != null) {
				params.add(new NameValuePair("zone", zone));
		
			}
			if (maxItems != null) {
				params.add(new NameValuePair("maxItems", maxItems));
				
			}
			if (skipCount != null) {
				params.add(new NameValuePair("skipCount", skipCount));
			}
			if (sortBy != null) {
				params.add(new NameValuePair("sortBy", sortBy));
			}

			//get.setParams(params);
			  get.setQueryString(params.toArray(new NameValuePair[params.size()]));
			
			HttpClient httpClient = new HttpClient();
			int result = httpClient.executeMethod(get);
			System.out.println("Response status code: " + result);
			System.out.println("Response body: ");
			System.out.println(get.getResponseBodyAsString());

		} catch (RestClientException | IOException e) {
			throw new MuleRuntimeException(e);

		}
	}

}
