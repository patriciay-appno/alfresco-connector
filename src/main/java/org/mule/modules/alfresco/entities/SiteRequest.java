package org.mule.modules.alfresco.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SiteRequest {


	@JsonProperty("shortName")
	private String shortName;
	
	@JsonProperty("sitePreset")
	private String sitePreset;	
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("description")
	private String description;

	@JsonProperty("visibility")
	// PUBLIC, MODERATED, PRIVATE
	private String visibility;
	
	@JsonProperty("type")
	private String type;
	
	

	public SiteRequest() {
	}

	

	public SiteRequest withShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}	
	
	public SiteRequest withSitePreset(String sitePreset) {
		this.sitePreset = sitePreset;
		return this;
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
	
	public SiteRequest withType(String type) {
		this.type = type;
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

	@JsonProperty("shortName")
	public String getShortName() {
		return shortName;
	}

	@JsonProperty("shortName")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@JsonProperty("sitePreset")
	public String getSitePreset() {
		return sitePreset;
	}

	@JsonProperty("sitePreset")
	public void setSitePreset(String sitePreset) {
		this.sitePreset = sitePreset;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}


