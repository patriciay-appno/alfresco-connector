/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.alfresco.strategy;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.components.HttpBasicAuth;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.rest.BasicAuthPassword;
import org.mule.api.annotations.rest.BasicAuthUsername;


@HttpBasicAuth( configElementName = "config-type", headerName="Authorization", friendlyName = "HttpBasicAuth")
public class ConnectorConnectionStrategy{
	
	@Configurable
	@BasicAuthUsername
	private String username;

	@Configurable
	@BasicAuthPassword
	@Password
	private String password;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}