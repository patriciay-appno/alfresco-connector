package org.mule.modules.alfresco.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonPropertyOrder({"title", "description", "visibility"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SiteRequest {


	@JsonProperty("title")
	private String title;
	
	@JsonProperty("description")
	private String description;

	@JsonProperty("visibility")
	// PUBLIC, MODERATED, PRIVATE
	private String visibility;

	public SiteRequest() {
	}

	

	public SiteRequest withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public SiteRequest withDescription(String description) {
		this.description = description;
		return this;
	}
	
	public SiteRequest withVisibility(String visibility) {
		this.visibility = visibility;
		return this;
	}
	

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("visibility")
	public String getVisibility() {
		return visibility;
	}

	@JsonProperty("visibility")
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}


