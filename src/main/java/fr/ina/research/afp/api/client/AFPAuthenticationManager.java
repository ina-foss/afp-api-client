package fr.ina.research.afp.api.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import fr.ina.research.afp.ApiClient;
import fr.ina.research.afp.ApiException;
import fr.ina.research.afp.Pair;

public class AFPAuthenticationManager {
	public class MyOAuthToken {
		private String access_token;
		private String token_type;
		private String refresh_token;
		private String expires_in;
		private String scope;
		private long expirationDate;

		protected void computeExpirationDate() {
			expirationDate = System.currentTimeMillis() + (Integer.parseInt(expires_in) * 1000);
		}

		public int expiresIn() {
			return (int) ((expirationDate - System.currentTimeMillis()) / 1000);
		}

		public String getAccessToken() {
			return access_token;
		}

		public String getRefreshToken() {
			return refresh_token;
		}

		public String getScope() {
			return scope;
		}

		public String getTokenType() {
			return token_type;
		}

		public boolean isExpired() {
			return isExpired(0);
		}

		public boolean isExpired(int offset) {
			return expiresIn() < offset;
		}

	}

	private final static Type myOAuthTokenType = new TypeToken<MyOAuthToken>() {
	}.getType();

	public final static String KEY_CLIENT = "AFP_API_client";
	public final static String KEY_SECRET = "AFP_API_secret";
	public final static String KEY_USERNAME = "AFP_API_user";
	public final static String KEY_PASSWORD = "AFP_API_pass";

	private final boolean anonymous;
	private final Map<String, String> authenticationProperties;
	private final Logger logger;

	private ApiClient apiClient;
	private MyOAuthToken currentToken;
	private int refreshBefore;

	public AFPAuthenticationManager(Map<String, String> authenticationProperties, Proxy proxy, Logger logger) {
		super();

		this.logger = logger;
		this.authenticationProperties = authenticationProperties;

		anonymous = (authenticationProperties == null);
		refreshBefore = 600;

		apiClient = new ApiClient();
		if (proxy != null) {
			apiClient.getHttpClient().setProxy(proxy);
		}
		apiClient.setVerifyingSsl(false);

		cleanToken();
	}

	public AFPAuthenticationManager(Proxy proxy, Logger logger) {
		this(null, proxy, logger);
	}

	public void cleanToken() {
		currentToken = null;
	}

	public String getAuthenticatedAs() {
		return isAnonymous() ? "anonymous" : authenticationProperties.get(KEY_CLIENT) + "/" + authenticationProperties.get(KEY_USERNAME);
	}

	public String getToken() throws IOException {
		return getToken(120);
	}

	public String getToken(int expirationOffset) throws IOException {
		int nbTry = 3;
		boolean successfull = false;
		IOException lastException = null;

		while (!successfull && (nbTry > 0)) {
			nbTry--;
			try {
				// Refresh current token
				if ((currentToken != null) && currentToken.isExpired(refreshBefore)) {
					requestNewToken("refresh_token");
					successfull = true;
				}

				// Current token is valid
				if (!successfull && (currentToken != null) && !currentToken.isExpired(expirationOffset)) {
					successfull = true;
				}

				// Anonymous login
				if (!successfull && anonymous) {
					requestNewToken("anonymous");
					successfull = true;
				}

				// Password login
				if (!successfull) {
					requestNewToken("password");
					successfull = true;
				}
			} catch (IOException e) {
				currentToken = null;
				lastException = e;
				logger.warn("in getToken()", e);
				try {
					Thread.sleep(200000);
				} catch (InterruptedException e1) {
					// ignore
				}
			}
		}

		if (successfull) {
			return currentToken.getAccessToken();
		}

		throw lastException;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	private void requestNewToken(String grantType) throws IOException {
		Map<String, String> localVarHeaderParams = new HashMap<>();
		localVarHeaderParams.put("Accept", "application/json");

		String method = "GET";

		String[] localVarAuthNames = new String[] {};

		List<Pair> localVarQueryParams = new ArrayList<>();
		Map<String, Object> formParams = new HashMap<>();

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "grant_type", grantType));

		if ("refresh_token".equals(grantType) || "password".equals(grantType)) {
			String client = authenticationProperties.get(KEY_CLIENT);
			String secret = authenticationProperties.get(KEY_SECRET);
			byte[] creds = (client + ":" + secret).getBytes();
			localVarHeaderParams.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(creds));
		}

		if ("refresh_token".equals(grantType)) {
			localVarQueryParams.addAll(apiClient.parameterToPairs("", "refresh_token", currentToken.getRefreshToken()));
		}

		if ("password".equals(grantType)) {
			localVarHeaderParams.put("Content-Type", "multipart/form-data");

			method = "POST";
			formParams.put("username", authenticationProperties.get(KEY_USERNAME));
			formParams.put("password", authenticationProperties.get(KEY_PASSWORD));
		}
		Response response = null;
		try {
			Request r = apiClient.buildRequest("/oauth/token", method, localVarQueryParams, null, localVarHeaderParams, formParams, localVarAuthNames, null);
			logger.debug(r.toString());
			Call call = apiClient.buildCall(r);
			response = call.execute();
			if (response.isSuccessful()) {
				MyOAuthToken newToken = apiClient.deserialize(response, myOAuthTokenType);
				newToken.computeExpirationDate();

				currentToken = newToken;
			} else {
				throw new IOException("Unable to get an authentication token (" + response.code() + ") : Msg(" + response.message() + ") Body("
						+ (response.body() != null ? response.body().string() : "") + ")");
			}
		} catch (ApiException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.body().close();
			}
		}

	}

	public void setRefreshBefore(int refreshBefore) {
		this.refreshBefore = refreshBefore;
	}

}
