package org.mule.modules.alfresco.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonPropertyOrder({ "displayName"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AuthorityRequest {

	@JsonProperty("displayName")
	private String displayName;

	

	public AuthorityRequest() {
	}

	public AuthorityRequest withDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	

	/**
	 * 
	 * @return The displayName
	 */
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 *            The displayName
	 */
	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
