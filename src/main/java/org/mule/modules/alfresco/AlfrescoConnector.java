/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.alfresco;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.MuleRuntimeException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.rest.HttpMethod;
import org.mule.api.annotations.rest.RestCall;
import org.mule.api.annotations.rest.RestExceptionOn;
import org.mule.api.annotations.rest.RestPostParam;
import org.mule.api.annotations.rest.RestQueryParam;
import org.mule.api.annotations.rest.RestUriParam;
import org.mule.modules.alfresco.entities.AuthorityRequest;
import org.mule.modules.alfresco.entities.NodeRequest;
import org.mule.modules.alfresco.entities.SiteRequest;
import org.mule.modules.alfresco.entities.UserRequest;
import org.mule.modules.alfresco.strategy.ConnectorConnectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriTemplate;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="alfresco", friendlyName="Alfresco")
public abstract class AlfrescoConnector {

	private static final String DEFAULT_ACTIVITI_SERVER_URL = "http://localhost:8080";
	
	private static final String ACCEPT_HEADER = "Accept";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	private ObjectMapper mapper;
   
    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;   
    
    @RestUriParam("serverUrl")
	@Configurable	
	@Default(value = DEFAULT_ACTIVITI_SERVER_URL)
	private String serverUrl;
    
    private static final Logger logger = LoggerFactory
			.getLogger(AlfrescoConnector.class);

   
    
    
	/**
	 * Get a list of the child authorities of a group. The list contains both people and groups. 
	 * 
	 * 
	 * 
	 * @param shortName
	 * @param authorityType   
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get child authorities for group")
	@Summary("Get a list of the child authorities of a group. The list contains both people and groups.")
	public  Object getChildAuthoritiesForGroup (
			@RestUriParam("shortName") String shortName, 
			@RestQueryParam("authorityType") String authorityType)
			throws IOException {
		
		try {			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/groups/{shortName}/children");
			URI uri = template.expand(serverUrl, shortName);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			HttpMethodParams params = new HttpMethodParams();
			params.setParameter("authorityType", authorityType);
			get.setParams(params);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
	}
	
	
	
	/**
	 * Remove an authority (USER or GROUP) from a group. A user will not be deleted by this method. You must have 'administrator' privileges to alter a group. 
	 * 
	 * 
	 * 
	 * @param shortGroupName
	 * @param fullAuthorityName   
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Remove an authority (USER or GROUP) from a group")
	@Summary("Remove an authority (USER or GROUP) from a group. A user will not be deleted by this method. You must have 'administrator' privileges to alter a group. ")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/groups/{shortGroupName}/children/{fullAuthorityName}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object removeAuthorityFromGroup (@RestUriParam("shortGroupName") String shortGroupName, @RestUriParam("fullAuthorityName") String fullAuthorityName)
			throws IOException;


	
	/**
	 * Add a group or user to a group
	 * 
	 * Adds a group or user to a group. The webscript will create a sub group if one does not already exist, with the fullAuthorityName.
	 * You must have "administrator" privileges to modify groups.
	 * If the authority is for a group and doe not exist then it is created.
	 * The webscript returns Status_Created if a new group is created, otherwise it returns Status_OK. If Status_Created returns the new sub group, otherwise returns the group. 
	 * 
	 * @param shortName
	 * @param fullAuthorityName   
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Add a group or user to a group")
	@Summary("Adds a group or user to a group. The webscript will create a sub group if one does not already exist, with the fullAuthorityName. If the authority is for a group and doe not exist then it is created.  ")
	public Object addAuthorityToGroup (
			@RestUriParam("shortName") String shortName, 
			@RestUriParam("fullAuthorityName") String fullAuthorityName)
			throws IOException {
		
		try {			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/groups/{shortName}/children/{fullAuthorityName}");
			URI uri = template.expand(serverUrl, shortName, fullAuthorityName);
			
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	
	/**
	 * Get details of a group
	 * 
	 * 
	 * 
	 * @param shortName	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Get details of a group")
	@Summary("Get details of a group")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/groups/{shortName}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object getGroupDetails (@RestUriParam("shortName") String shortName)
			throws IOException;
	
	
	/**
	 * Update the details of a group
	 * 
	 * Updates the details of a group.
	 * You must have "administrator" privileges to change the name of a group.
	 * The following properties may be updated:
	 * - displayName:  The display name 
	 * 
	 * @param shortName	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Update details of a group")
	@Summary("Update details of a group. displayName property will be updated")
	public Object updateGroupDetails (
			@RestUriParam("shortName") String shortName,
			@RestPostParam("displayName") String displayName)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/groups/{shortName}");
			URI uri = template.expand(serverUrl, shortName);
			
			PutMethod put = new PutMethod(uri.toString());
			
			put.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			AuthorityRequest processRequest = new AuthorityRequest();
			processRequest = processRequest
					.withDisplayName(displayName);
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(processRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        put.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(put);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(put.getResponseBodyAsString());
		   
		    return put.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	}
	
	
	/**
	 * Delete a group
	 * 
	 * 
	 * 
	 * @param shortName	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Delete a group")
	@Summary("Delete a group")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/groups/{shortName}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object deleteGroup (@RestUriParam("shortName") String shortName)
			throws IOException;
	
	
	/**
	 * Search for groups
	 * 
	 * The following optional parameters are available:-
	 * 
     * shortNameFilter - returns those groups with a partial match on shortName. You can use the pattern matching characters * to match zero or more characters or ? to match one character.
     * zone - returns only groups that are in the specified zone, otherwise it returns groups from all zones.
     * If the optional sortBy parameter is given, then the results may be sorted. Possible values are "authorityName" (default), "shortName" and "displayName"
	 * 
	 * 
	 * @param shortNameFilter
	 * @param zone	
	 * @param maxItems	
	 * @param skipCount	
	 * @param sortBy		
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Search for groups")
	@Summary("Searches for groups. Returns an Array of groups in JSON format. ")
	public Object searchGroups (
			@Optional @RestQueryParam("shortNameFilter") String shortNameFilter,
		    @Optional @RestQueryParam("zone") String zone,
			@Optional @RestQueryParam("maxItems") String maxItems, 
			@Optional @RestQueryParam("skipCount") String skipCount, 
			@Optional @RestQueryParam("sortBy") String sortBy)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/groups");
			URI uri = template.expand(serverUrl);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
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

			get.setQueryString(params.toArray(new NameValuePair[params.size()]));
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.info("Response status code: " + result);
		    logger.info("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
	}
	
	/**
	 * Gets a list of the parent authorities of a group. 
	 * 
	 *	The optional level attribute can be ALL, in which case all parents are returned. The optional maxItems parameter sets the maximum number of items to be returned. 
	 *	If no value is set then all items are returned. The optional skipCount parameter determines how many items to skip before returning the first result. 
	 *	If no skipCount value is set then no items are skipped.
	 * 	If the optional sortBy parameter is given, then the results may be sorted. Possible values are "authorityName" (default), "shortName" and "displayName" 
	 * 
	 * @param shortName	
	 * @param level	
	 * @param maxItems
	 * @param skipCount
	 * @param sortBy	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Get parent authorities for group")
	@Summary("Gets a list of the parent authorities of a group.")
	public Object getParentAuthoritiesForGroup (
			@RestUriParam("shortName") String shortName,
			@Optional @RestQueryParam("level") String level,  
			@Optional @RestQueryParam("maxItems") String maxItems,  
			@Optional @RestQueryParam("skipCount") String skipCount,
			@Optional @RestQueryParam("sortBy") String sortBy)
			throws IOException {
		
		try {			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/groups/{shortName}/parents");
			URI uri = template.expand(serverUrl, shortName);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			if (level != null) {				
				params.add(new NameValuePair("level", level));				
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

			get.setQueryString(params.toArray(new NameValuePair[params.size()]));
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.info("Response status code: " + result);
		    logger.info("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
		
	}
	
	
	/**
	 *  List all root groups. 
	 * 
	 *	If the optional zone parameter is set to 'true' then returns root groups from the specified zone. If not specified will return groups from all zones.
	 *	If the optional shortNameFilter parameter is set then returns those root groups with a partial match on shortName. The shortname filter can contain the wild card characters * and ? but these must be url encoded for this script. 
	 *	The optional maxItems parameter sets the maximum number of items to be returned. If no value is set then all items are returned. 
	 *	The optional skipCount parameter determines how many items to skip before returning the first result. If no skipCount value is set then no items are skipped. 
	 *	If the optional sortBy parameter is given, then the results may be sorted. Possible values are "authorityName" (default), "shortName" and "displayName"  
	 * 
	 * @param shortNameFilter
	 * @param zone
	 * @param maxItems
	 * @param skipCount
	 * @param sortBy	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "List all root groups")
	@Summary("List all root groups.")
	public  Object listRootGroups (
			@Optional @RestQueryParam("shortNameFilter") String shortNameFilter, 
			@Optional @RestQueryParam("zone") String zone,  
			@Optional @RestQueryParam("maxItems") String maxItems,  
			@Optional @RestQueryParam("skipCount") String skipCount, 
			@Optional @RestQueryParam("sortBy") String sortBy)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/rootgroups");
			URI uri = template.expand(serverUrl);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
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

			get.setQueryString(params.toArray(new NameValuePair[params.size()]));
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.info("Response status code: " + result);
		    logger.info("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
	}
	
	
	/**
	 * Delete a root group
	 * 
	 * 
	 * 
	 * @param shortName	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Delete a root group")
	@Summary("Delete a root group")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/rootgroups/{shortName}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object deleteRootGroup (@RestUriParam("shortName") String shortName)
			throws IOException;
	
	/**
	 * Add a root group
	 * 
	 * Returns STATUS_CREATED if a new group is created.
     * If the group already exists returns BAD_REQUEST. The following properties may be specified for the new root group:
     * - displayName 
	 * 
	 * @param shortName	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Add a root group")
	@Summary("Add a root group")
	public Object addRootGroup (
			@RestUriParam("shortName") String shortName,
			@RestPostParam("displayName") String displayName)
			throws IOException {
		
		
		try {
			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/rootgroups/{shortName}");
			URI uri = template.expand(serverUrl, shortName);
			
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			AuthorityRequest processRequest = new AuthorityRequest();
			processRequest = processRequest
					.withDisplayName(displayName);
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(processRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        post.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	
	/**
	 * Deletes the specified category. 
	 * 
	 * 
	 * 
	 * @param storeProtocol
	 * @param storeId
	 * @param nodeId	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Delete a category")
	@Summary("Delete a category")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/category/{storeProtocol}/{storeId}/{nodeId}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object deleteCategory (
			@RestUriParam("storeProtocol") @Default("workspace") String storeProtocol,
			@RestUriParam("storeId") @Default("SpacesStore") String storeId,
			@RestUriParam("nodeId") String nodeId)
			throws IOException;
	
	/**
	 * Create new category in specified category or as a root category. 
	 * 
	 * @param categoryName
	 * @param storeProtocol
	 * @param storeId
	 * @param nodeId	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Create new category")
	@Summary("Create new category")
	public Object createNewCategory (
			@Optional @RestUriParam("storeProtocol") String storeProtocol,
			@Optional @RestUriParam("storeId") String storeId,
			@Optional @RestUriParam("nodeId") String nodeId,
			@RestPostParam("categoryName") String categoryName)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/category");
			URI uri = template.expand(serverUrl);
			
			
			if(storeProtocol != null && storeId != null && nodeId != null) {
				
				UriTemplate template2 = new UriTemplate(
						"{serverUrl}/alfresco/service/api/category/{storeProtocol}/{storeId}/{nodeId}");
				uri = template2.expand(serverUrl, storeProtocol, storeId, nodeId);
			}
			
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			NodeRequest categoryRequest = new NodeRequest();
			categoryRequest = categoryRequest
					.withName(categoryName);
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(categoryRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        post.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	/**
	 * Update the details of the specified category
	 * 
	 * Property that can be updated:
	 * - name: name of category
	 * 
	 * @param storeProtocol
	 * @param storeId
	 * @param nodeId	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Update category")
	@Summary("Update category")
	public Object updateCategory (
			@RestUriParam("storeProtocol") @Default("workspace") String storeProtocol,
			@RestUriParam("storeId") @Default("SpacesStore") String storeId,
			@RestUriParam("nodeId") String nodeId,
			@RestPostParam("categoryName") String categoryName)
			throws IOException {
		
		
		try {			
			
			
			UriTemplate template = new UriTemplate(
					"{serverUrl}/alfresco/service/api/category/{storeProtocol}/{storeId}/{nodeId}");
			URI uri = template.expand(serverUrl, storeProtocol, storeId, nodeId);
			
			
			PutMethod put = new PutMethod(uri.toString());
						
			put.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			NodeRequest categoryRequest = new NodeRequest();
			categoryRequest = categoryRequest
					.withName(categoryName);
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(categoryRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        put.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(put);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(put.getResponseBodyAsString());
		   
		    return put.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	/**
	 *	Creates a Child Folder in an existing Folder.
	 *
	 * 	By default the new folder will be of type cm:folder, but subtypes of cm:folder may be specified instead.
	 * 	The new NodeRef will be returned if the folder can be created.
	 *	The minimum request is of the form:
	 *
	 *  { "name": "NewNodeName" }
	 *  
	 *  The full set of parameters accepted in the request is of the form:
	 *  
	 *  { 
	 *  	"name": "NewNodeName",
	 *   	"title": "New Node Title",
	 *  	"description": "A shiny new node",
	 *  	"type": "cm:folder"
	 *   }
	 *  
	 *  	 	
	 * @param storeProtocol
	 * @param storeId
	 * @param nodeId	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Create child folder in existing folder")
	@Summary("Create child folder in existing folder")
	public Object createChildFolder (
			@RestUriParam("storeProtocol") @Default("workspace") String storeProtocol,
			@RestUriParam("storeId") @Default("SpacesStore") String storeId,
			@RestUriParam("nodeId") String nodeId,
			@RestPostParam("nodeName") String nodeName,
			@Optional @RestPostParam("nodeTitle") String nodeTitle,
			@Optional @RestPostParam("nodeDescription") String nodeDescription,
			@Optional @RestPostParam("nodeType") String nodeType)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/node/folder/{store_type}/{store_id}/{id}");
			URI uri = template.expand(serverUrl, storeProtocol, storeId, nodeId);
			
			
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			NodeRequest nodeRequest = new NodeRequest();
			nodeRequest = nodeRequest.withName(nodeName);
			nodeRequest = nodeRequest.withTitle(nodeTitle);
			nodeRequest = nodeRequest.withDescription(nodeDescription);
			nodeRequest = nodeRequest.withType(nodeType);
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(nodeRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        post.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	/**
	 *	Creates a Child Folder in the specified Site Container
	 *
	 * 	By default the new folder will be of type cm:folder, but subtypes of cm:folder may be specified instead.
	 * 	The new NodeRef will be returned if the folder can be created.
	 *	The minimum request is of the form:
	 *
	 *  { "name": "NewNodeName" }
	 *  
	 *  The full set of parameters accepted in the request is of the form:
	 *  
	 *  { 
	 *  	"name": "NewNodeName",
	 *   	"title": "New Node Title",
	 *  	"description": "A shiny new node",
	 *  	"type": "cm:folder"
	 *   }
	 *  
	 *  	 
	 * @param site
	 * @param container
	 * @param path	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Create child folder in Site Container")
	@Summary("Create child folder in Site Container")
	public Object createSiteChildFolder (
			@RestUriParam("site") String site,
			@RestUriParam("container") @Default("documentLibrary") String container,
			@Optional @RestUriParam("path") String path,
			@RestPostParam("nodeName") String nodeName,
			@Optional @RestPostParam("nodeTitle") String nodeTitle,
			@Optional @RestPostParam("nodeDescription") String nodeDescription,
			@Optional @RestPostParam("nodeType") String nodeType)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/site/folder/{site}/{container}");
			URI uri = template.expand(serverUrl, site, container);
			
			
			if(path != null) {
				
				UriTemplate template2 = new UriTemplate(
						"{serverUrl}/alfresco/service/api/site/folder/{site}/{container}/{path}");
				uri = template2.expand(serverUrl, site, container, path);
			}
			
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			NodeRequest nodeRequest = new NodeRequest();
			nodeRequest = nodeRequest.withName(nodeName);
			nodeRequest = nodeRequest.withTitle(nodeTitle);
			nodeRequest = nodeRequest.withDescription(nodeDescription);
			nodeRequest = nodeRequest.withType(nodeType);
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(nodeRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        post.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	

	/**
	 *  Get a collection of people stored in the repository. This can optionally be filtered according to a given filter query string 
	 * 	
	 * 	  
	 * @param filter	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Get users")
	@Summary("Get a collection of people stored in the repository.")
	public  Object getUsers (
			@Optional @RestQueryParam("filter") String filter)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/people");
			URI uri = template.expand(serverUrl);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			if(filter != null) {
				HttpMethodParams params = new HttpMethodParams();
				params.setParameter("filter", filter);
				get.setParams(params);
			}
						
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.info("Response status code: " + result);
		    logger.info("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
	}
	
	/**
	 *	Adds a new person based on the details provided.
	 *
	 *	userName
	 *	    mandatory - the user name for the new user
	 *	firstName
	 *	    mandatory - the given Name
	 *	lastName
	 *	    mandatory - the family name
	 *	email
	 *	    mandatory - the email address 
	 *	password
	 *	    optional - the new user's password. If not specified then a value of "password" is used which should be changed as soon as possible.
	 *	disableAccount
	 *	    optional - If present and set to "true" the user is created but their account will be disabled.
	 *	quota
	 *	    optional - Sets the quota size for the new user, in bytes.
	 *	groups
	 *	    optional - Array of group names to assign the new user to.
	 *	title
	 *	    optional - the title for the new user.
	 *	organisation
	 *	    optional - the organisation the new user belongs to.
	 *	jobtitle
	 *	    optional - the job title of the new user. 
	 *  
	 *  	 
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Create user")
	@Summary("Create user")
	public Object createUser (
			@RestPostParam("userName") String userName,
			@RestPostParam("firstName") String firstName,
			@RestPostParam("lastName") String lastName,
			@RestPostParam("email") String email,
			@Optional @RestPostParam("password") @Default("passorwd") String password,
			@Optional @RestPostParam("disableAccount") Boolean disableAccount,
			@Optional @RestPostParam("quota") Integer quota,
			@Optional @RestPostParam("title") String title,
			@Optional @RestPostParam("organisation") String organisation,
			@Optional @RestPostParam("jobtitle") String jobtitle)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/people");
			URI uri = template.expand(serverUrl);
			
		
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			UserRequest userRequest = new UserRequest();
			userRequest = userRequest.withUserName(userName);
			userRequest = userRequest.withFirstName(firstName);
			userRequest = userRequest.withLastName(lastName);
			userRequest = userRequest.withEmail(email);
			userRequest = userRequest.withPassword(password);
			userRequest = userRequest.withDisableAccount(disableAccount);
			userRequest = userRequest.withTitle(title);
			userRequest = userRequest.withOrganisation(organisation);
			userRequest = userRequest.withJobtitle(jobtitle);
			userRequest = userRequest.withQuota(quota);
			
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(userRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
	        post.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	/**
	 * Delete a user
	 * 
	 * 
	 * 
	 * @param userName	 
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Delete user")
	@Summary("Delete user. ")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/people/{userName}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object deleteUser (@RestUriParam("userName") String userName)
			throws IOException;



	/**
	 *	Update user
	 *
	 *  	 
	 * @param userName	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Update user")
	@Summary("Update user")
	public Object updateUser (
			@RestPostParam("userName") String userName,
			@Optional @RestPostParam("firstName") String firstName,
			@Optional @RestPostParam("lastName") String lastName,
			@Optional @RestPostParam("email") String email,
			@Optional @RestPostParam("disableAccount") Boolean disableAccount,
			@Optional @RestPostParam("quota") Integer quota,
			@Optional @RestPostParam("title") String title,
			@Optional @RestPostParam("organisation") String organisation,
			@Optional @RestPostParam("jobtitle") String jobtitle,
			@Optional @RestPostParam("location") String location,
			@Optional @RestPostParam("telephone") String telephone,
			@Optional @RestPostParam("mobile") String mobile,
			@Optional @RestPostParam("companyaddress1") String companyaddress1,
			@Optional @RestPostParam("companyaddress2") String companyaddress2,
			@Optional @RestPostParam("companyaddress3") String companyaddress3,
			@Optional @RestPostParam("companypostcode") String companypostcode,
			@Optional @RestPostParam("companytelephone") String companytelephone,
			@Optional @RestPostParam("companyfax") String companyfax,
			@Optional @RestPostParam("companyemail") String companyemail,
			@Optional @RestPostParam("skype") String skype,
			@Optional @RestPostParam("instantmsg") String instantmsg,
			@Optional @RestPostParam("persondescription") String persondescription)
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/people/{userName}");
			URI uri = template.expand(serverUrl, userName);
			
		
			PutMethod put = new PutMethod(uri.toString());
			
			put.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			UserRequest userRequest = new UserRequest();			
			userRequest = userRequest.withFirstName(firstName);
			userRequest = userRequest.withLastName(lastName);
			userRequest = userRequest.withEmail(email);			
			userRequest = userRequest.withDisableAccount(disableAccount);
			userRequest = userRequest.withTitle(title);
			userRequest = userRequest.withOrganisation(organisation);
			userRequest = userRequest.withJobtitle(jobtitle);
			userRequest = userRequest.withQuota(quota);
			
			userRequest = userRequest.withLocation(location);
			userRequest = userRequest.withTelephone(telephone);
			userRequest = userRequest.withMobile(mobile);
			userRequest = userRequest.withCompanyaddress1(companyaddress1);
			userRequest = userRequest.withCompanyaddress2(companyaddress2);
			userRequest = userRequest.withCompanyaddress3(companyaddress3);
			userRequest = userRequest.withCompanypostcode(companypostcode);
			userRequest = userRequest.withCompanytelephone(companytelephone);
			userRequest = userRequest.withCompanyfax(companyfax);
			userRequest = userRequest.withCompanyemail(companyemail);
			userRequest = userRequest.withSkype(skype);
			userRequest = userRequest.withInstantmsg(instantmsg);
			userRequest = userRequest.withPersondescription(persondescription);
			
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(userRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
			put.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(put);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(put.getResponseBodyAsString());
		   
		    return put.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	/**
	 *  Get a collection of the sites of which a user is an explicit member.
	 * 	
	 * 	  
	 * @param filter	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Get user sites")
	@Summary("Get a collection of the sites of which a user is an explicit member.")
	public  Object getUserSites (
			@RestUriParam("userId") String userId,
			@Optional @RestQueryParam("pagesize") String pagesize,
			@Optional @RestQueryParam("position") String position)
			
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/people/{userId}/sites");
			URI uri = template.expand(serverUrl, userId);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			if (pagesize != null) {				
				params.add(new NameValuePair("size", pagesize));				
			}
			if (position != null) {				
				params.add(new NameValuePair("pos", position));				
			}			
		
			

			get.setQueryString(params.toArray(new NameValuePair[params.size()]));
						
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.info("Response status code: " + result);
		    logger.info("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
	}
	
	/**
	 * Delete a site. 
	 * 
	 * 
	 * 
	 * @param shortname
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Delete site")
	@Summary("Delete a site")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/sites/{shortname}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object deleteSite (@RestUriParam("shortname") String shortname)
			throws IOException;
	
	/**
	 * Get the details of a  site. Returns 200, STATUS_OK on success. 
	 * 
	 * 
	 * 
	 * @param shortname
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Get site details")
	@Summary("Get site details")
	@RestCall(uri = "{serverUrl}/alfresco/service/api/sites/{shortname}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Object getSite (@RestUriParam("shortname") String shortname)
			throws IOException;
	
	

	/**
	 *  Update the details of a Web site. The following properties may be updated.
	 *
	 *  title : the title of the web site
	 *  description: the description for the web site
	 *  visibility: the site visibility, one of (PUBLIC,MODERATED,PRIVATE) 
	 *
	 * @param shortname
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Update site details")
	@Summary("Update site details")
	public Object updateSite (
			@RestUriParam("shortname") String shortname,			
			@Optional @RestPostParam("title") String title,
			@Optional @RestPostParam("description") String description,
			@Optional @RestPostParam("visibility") String visibility)
			
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/sites/{shortname}");
			URI uri = template.expand(serverUrl, shortname);
			
		
			PutMethod put = new PutMethod(uri.toString());
			
			put.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			put.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			SiteRequest siteRequest = new SiteRequest();
			siteRequest = siteRequest.withDescription(description);
			siteRequest = siteRequest.withTitle(title);
			siteRequest = siteRequest.withVisibility(StringUtils.upperCase(visibility));
			
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(siteRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
			put.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(put);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(put.getResponseBodyAsString());
		   
		    return put.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	/**
	 *   Get a collection of the sites in the repository. The collection can be filtered by name and/or site preset.
	 * 	
	 * 	  
	 * @param nameFilter
	 * @param sitePresetFilter	
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	@Processor(friendlyName = "Get sites")
	@Summary("Get a collection of sites.")
	public  Object getSites (
			@Optional @RestUriParam("nameFilter") String nameFilter,
			@Optional @RestUriParam("sitePresetFilter") String sitePresetFilter,			
			@Optional @RestQueryParam("pagesize") String pagesize)
			
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/sites");
			URI uri = template.expand(serverUrl);
			
			GetMethod get = new GetMethod(uri.toString());
			
			get.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			if (nameFilter != null) {				
				params.add(new NameValuePair("nf", nameFilter));				
			}
			if (sitePresetFilter != null) {				
				params.add(new NameValuePair("spf", sitePresetFilter));				
			}
			if (pagesize != null) {				
				params.add(new NameValuePair("size", pagesize));				
			}	
		
			

			get.setQueryString(params.toArray(new NameValuePair[params.size()]));
						
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(get);
		    logger.info("Response status code: " + result);
		    logger.info("Response body: ");
		    logger.debug(get.getResponseBodyAsString());
		   
		    return get.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
		
	}
	
	/**
	 *  Creates a new Web site based on the site preset and details provided.
	 *  
     *  Please note: this method only creates a site at the repository level, it does not create a fully functional site. It should be considered for internal use only at the moment. Currently, creating a site programmatically needs to be done in the Share context, using the create-site module. Further information can be found at the address http://your_domain:8080/share/page/index/uri/modules/create-site.post within your Alfresco installation.
     *  The following properties may be set.
     *  
     *  shortName
     *      the shortName of the web site, mandatory, must be unique
     *  sitePreset
     *      the sitePreset
     *  title
     *      the title of the web site
     *  description
     *      the description for the web site
     *  visibility
     *      the site visibility, one of (PUBLIC,MODERATED,PRIVATE), defaults to PUBLIC
     *  type
     *      the type of site to create, optional 
	 *
	 * @param shortName
	 * @param sitePreset
	 * @param title
	 * @param description
	 * @param visibility
	 * @param type
	 * @return Object
	 * @throws IOException
	 *             exception thrown
	 */
	
	/*@Processor(friendlyName = "Create site")
	@Summary("Create site")
	public Object createSite (
			@RestPostParam("shortName") String shortName,	
			@Optional @RestPostParam("sitePreset") @Default("site-dashboard") String sitePreset,
			@Optional @RestPostParam("title") String title,
			@Optional @RestPostParam("description") String description,
			@Optional @RestPostParam("visibility") @Default("PUBLIC") String visibility,
			@Optional @RestPostParam("type") String type)
			
			throws IOException {
		
		
		try {			
			
			UriTemplate template = new UriTemplate("{serverUrl}/alfresco/service/api/sites");
			URI uri = template.expand(serverUrl);
			
		
			PostMethod post = new PostMethod(uri.toString());
			
			post.addRequestHeader(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(CONTENT_TYPE_HEADER,  MediaType.APPLICATION_JSON.toString());
			post.addRequestHeader(AUTHORIZATION_HEADER, ConnectorHelper.setBasicAuthorization(connectionStrategy.getUsername(),
					connectionStrategy.getPassword()));
					
			SiteRequest siteRequest = new SiteRequest();
			
			siteRequest = siteRequest.withShortName(shortName);
			siteRequest = siteRequest.withSitePreset(sitePreset);
			siteRequest = siteRequest.withDescription(description);
			siteRequest = siteRequest.withTitle(title);
			siteRequest = siteRequest.withVisibility(StringUtils.upperCase(visibility));
			siteRequest = siteRequest.withType(type);
			
			
			mapper = new ObjectMapper();
			
			String jsonValue = mapper.writeValueAsString(siteRequest);
			
			
			StringRequestEntity requestEntity = new StringRequestEntity(jsonValue, "application/json", "UTF-8");
			post.setRequestEntity(requestEntity);
			
			HttpClient httpClient = new HttpClient();
		    int result = httpClient.executeMethod(post);
		    logger.debug("Response status code: " + result);
		    logger.debug("Response body: ");
		    logger.debug(post.getResponseBodyAsString());
		   
		    return post.getResponseBodyAsString();
			
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
			
		}
	
	}
	
	*/
	
	
    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

  


	public String getServerUrl() {
		return serverUrl;
	}


	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	
	
}