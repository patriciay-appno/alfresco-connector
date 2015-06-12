package org.mule.modules.alfresco.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserRequest {

	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("email")
	private String email;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("quota")
	private Integer quota;
	
	@JsonProperty("disableAccount")
	private Boolean disableAccount;
		
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("organisation")
	private String organisation;
	
	@JsonProperty("jobtitle")
	private String jobtitle;
	
	@JsonProperty("location")
	private String location;
	
	@JsonProperty("telephone")
	private String telephone;
	
	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("companyaddress1")
	private String companyaddress1;
	
	@JsonProperty("companyaddress2")
	private String companyaddress2;
	
	@JsonProperty("companyaddress3")
	private String companyaddress3;
	
	@JsonProperty("companypostcode")
	private String companypostcode;
	
	@JsonProperty("companytelephone")
	private String companytelephone;
	
	@JsonProperty("companyfax")
	private String companyfax;
	
	@JsonProperty("companyemail")
	private String companyemail;
	
	@JsonProperty("skype")
	private String skype;
	
	@JsonProperty("instantmsg")
	private String instantmsg;
	
	@JsonProperty("persondescription")
	private String persondescription;
	
	public UserRequest() {
	}

	public UserRequest withUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public UserRequest withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	public UserRequest withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public UserRequest withEmail(String email) {
		this.email = email;
		return this;
	}
	
	public UserRequest withPassword(String password) {
		this.password = password;
		return this;
	}
	
	public UserRequest withDisableAccount(Boolean disableAccount) {
		this.disableAccount = disableAccount;
		return this;
	}
	
	public UserRequest withQuota(Integer quota) {
		this.quota = quota;
		return this;
	}
	
	public UserRequest withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public UserRequest withOrganisation(String organisation) {
		this.organisation = organisation;
		return this;
	}
	
	public UserRequest withJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
		return this;
	}
	
	public UserRequest withTelephone(String telephone) {
		this.telephone = telephone;
		return this;
	}
	
	public UserRequest withMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}
	
	public UserRequest withCompanyaddress1(String companyaddress1) {
		this.companyaddress1 = companyaddress1;
		return this;
	}
	
	public UserRequest withCompanyaddress2(String companyaddress2) {
		this.companyaddress2 = companyaddress2;
		return this;
	}
	
	public UserRequest withCompanyaddress3(String companyaddress3) {
		this.companyaddress3 = companyaddress3;
		return this;
	}
	
	public UserRequest withCompanypostcode(String companypostcode) {
		this.companypostcode = companypostcode;
		return this;
	}
	
	public UserRequest withCompanytelephone(String companytelephone) {
		this.companytelephone = companytelephone;
		return this;
	}
	
	public UserRequest withCompanyfax(String companyfax) {
		this.companyfax = companyfax;
		return this;
	}
	
	public UserRequest withCompanyemail(String companyemail) {
		this.companyemail = companyemail;
		return this;
	}
	
	public UserRequest withSkype(String skype) {
		this.skype = skype;
		return this;
	}
	
	public UserRequest withInstantmsg(String instantmsg) {
		this.instantmsg = instantmsg;
		return this;
	}
	
	public UserRequest withPersondescription(String persondescription) {
		this.persondescription = persondescription;
		return this;
	}
	
	public UserRequest withLocation(String location) {
		this.location = location;
		return this;
	}

	@JsonProperty("userName")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("userName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty("quota")
	public Integer getQuota() {
		return quota;
	}

	@JsonProperty("quota")
	public void setQuota(Integer quota) {
		this.quota = quota;
	}

	@JsonProperty("disableAccount")
	public Boolean getDisableAccount() {
		return disableAccount;
	}

	@JsonProperty("disableAccount")
	public void setDisableAccount(Boolean disableAccount) {
		this.disableAccount = disableAccount;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("organisation")
	public String getOrganisation() {
		return organisation;
	}

	@JsonProperty("organisation")
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	@JsonProperty("jobtitle")
	public String getJobtitle() {
		return jobtitle;
	}

	@JsonProperty("jobtitle")
	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	@JsonProperty("telephone")
	public String getTelephone() {
		return telephone;
	}

	@JsonProperty("telephone")
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@JsonProperty("mobile")
	public String getMobile() {
		return mobile;
	}

	@JsonProperty("mobile")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonProperty("companyaddress1")
	public String getCompanyaddress1() {
		return companyaddress1;
	}

	@JsonProperty("companyaddress1")
	public void setCompanyaddress1(String companyaddress1) {
		this.companyaddress1 = companyaddress1;
	}

	@JsonProperty("companyaddress2")
	public String getCompanyaddress2() {
		return companyaddress2;
	}

	@JsonProperty("companyaddress2")
	public void setCompanyaddress2(String companyaddress2) {
		this.companyaddress2 = companyaddress2;
	}

	@JsonProperty("companyaddress3")
	public String getCompanyaddress3() {
		return companyaddress3;
	}

	@JsonProperty("companyaddress3")
	public void setCompanyaddress3(String companyaddress3) {
		this.companyaddress3 = companyaddress3;
	}

	@JsonProperty("companypostcode")
	public String getCompanypostcode() {
		return companypostcode;
	}

	@JsonProperty("companypostcode")
	public void setCompanypostcode(String companypostcode) {
		this.companypostcode = companypostcode;
	}

	@JsonProperty("companytelephone")
	public String getCompanytelephone() {
		return companytelephone;
	}

	@JsonProperty("companytelephone")
	public void setCompanytelephone(String companytelephone) {
		this.companytelephone = companytelephone;
	}

	@JsonProperty("companyfax")
	public String getCompanyfax() {
		return companyfax;
	}

	@JsonProperty("companyfax")
	public void setCompanyfax(String companyfax) {
		this.companyfax = companyfax;
	}

	@JsonProperty("companyemail")
	public String getCompanyemail() {
		return companyemail;
	}

	@JsonProperty("companyemail")
	public void setCompanyemail(String companyemail) {
		this.companyemail = companyemail;
	}

	@JsonProperty("skype")
	public String getSkype() {
		return skype;
	}

	@JsonProperty("skype")
	public void setSkype(String skype) {
		this.skype = skype;
	}

	@JsonProperty("instantmsg")
	public String getInstantmsg() {
		return instantmsg;
	}

	@JsonProperty("instantmsg")
	public void setInstantmsg(String instantmsg) {
		this.instantmsg = instantmsg;
	}

	@JsonProperty("persondescription")
	public String getPersondescription() {
		return persondescription;
	}

	@JsonProperty("persondescription")
	public void setPersondescription(String persondescription) {
		this.persondescription = persondescription;
	}
	
	
}
