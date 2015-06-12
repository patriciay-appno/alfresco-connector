package org.mule.modules.alfresco.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonPropertyOrder({"name", "title", "description", "type"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NodeRequest {

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("description")
	private String description;

	@JsonProperty("type")
	private String type;

	public NodeRequest() {
	}

	public NodeRequest withName(String name) {
		this.name = name;
		return this;
	}

	public NodeRequest withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public NodeRequest withDescription(String description) {
		this.description = description;
		return this;
	}
	
	public NodeRequest withType(String type) {
		this.type = type;
		return this;
	}
	


	@JsonProperty("name")
	public String getName() {
		return name;
	}

	
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
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
