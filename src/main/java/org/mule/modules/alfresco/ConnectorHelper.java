/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.alfresco;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Bouchaib Fattouh Appnovation Technologies
 */
public class ConnectorHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(ConnectorHelper.class);

	protected static String setBasicAuthorization(String username,
			String password) {
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		logger.debug("Basic authorization encoded credentials: {}",
				authStringEnc);
		return "Basic " + authStringEnc;
	}

	public static String toISO8601Date(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	protected static void validateCredentials(String username, String password) {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("The username may not be null");
		}
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("The password may not be null");
		}
	}

	

}
