/**
 * AFP Content API
 * The first version of the AFP API is an exciting step forward towards making it easier for users to have open access to news articles from Agence France-Presse.  We created it so that you can surface the amazing content AFP share every second, in innovative ways.  Build something great!  Once you've been registered by your AFP marketing contact, it's easy to start requesting data from AFP API.  All endpoints are only accessible via https and are located at __`api.afpforum.com`__.  [External API scheme](https://api.afpforum.com/v1/docs?group=api)  [External API documentation in english](https://api.afpforum.com/docs/api-en.pdf)  [External API documentation in french](https://api.afpforum.com/docs/api-fr.pdf)  ---  #### AUTHENTICATION ####  To access the service you must get an access token from the authorize end point. You have three ways to get it. The authentification scheme is based on OAuth 2.0.  To access to all authentication end point you must pass in the basic authentification header the client-id and client-secret provided by your AFP marketing contact.  * __Anonymous mode__: this is the way to test the service by fetching news items from the last days: https://api.afpforum.com/oauth/token?grant_type=anonymous  * __Temporary access__: this allows you access to the full service for a short period. You need to provide an authorization code. This code is given by your AFP marketing contact. https://api.afpforum.com/oauth/token?grant_type=authorization_code&code=ACCESS-CODE  * __Permanent access__: If you are a regular customer with a user name and password, you grab an access token from  https://api.afpforum.com/oauth/token?username=USERNAME&password=PASSWORD&grant_type=password  The HTTP Method POST and GET are supported, POST Method prefered. You must provide in the HTTP  Header Basic Authentification, you client id and your client secret. Else you will not be able to use the API.   Sample authentification using curl: curl -su CLIENT_ID:CLIENT_SECRET -H \"Accept: application/json\" \"https://api.afpforum.com/oauth/token?username=USER&password=PASSWORD&grant_type=password   After authentication, you can grab the latest news by accessing the following URL with your access token (replace ACCESS-TOKEN with your own):   https://api.afpforum.com/v1/api/latest?access_token=ACCESS-TOKEN&lang=en&tz=GMT  You're best off using an access_token for the authenticated user for each endpoint, though many endpoints require it. In some cases an access_token will give you more access to information, and in all cases, it means that you are operating under a per-access_token limit.  ---  #### Limit Be nice.####  If you're sending too many requests too quickly, we'll send back a `503` error code (server unavailable). You are limited to 5000 requests per hour per `access_token` or `client_id` overall. Practically, this means you should (when possible) authenticate users so that limits are well outside the reach of a given user.  ---  #### RESPONSE  ####  Every response is contained by an envelope. That is, each response has a predictable set of keys with which you can expect to interact:   json {    \"response\": {     \"status\": {      \"code\": 0,      \"reason\": \"Success\"     },     ...    }  }  ---  #### STATUS ####  The status key is used to communicate information about the response to the developer. If everything goes fine, you'll only ever see a code key with value 0. However, sometimes things go wrong, and in that case you might see a response like:    json {   \"error\": {    \"error_type\": \"SearchServerException\",    \"code\": 401,    \"message\": \"Access token expired\"   }  }   ---  #### DATE ARGUMENT ####  The date format is compliant to YYYY-MM-DD'T'HH:mm:ssZ format, like 2016-10-24T22:00:00Z, it supports also Date Math expression format  Some examples are:  * `now-1h`: The current time minus one hour, with ms resolution. * `now-1h+1m`: The current time minus one hour plus one minute, with ms resolution. * `now-1h/d`: The current time minus one hour, rounded down to the nearest day. * `now-1M`: The current time minus one month, with ms resolution. * `2015-01-01||+1M/d`: 2015-01-01 plus one month, rounded down to the nearest day.  ---  #### PARAMETERS ####  The parameter object is complex, some examples below  !!! %%%Simple parameter searching items in multimedia production  json {   \"tz\": \"Europe/Paris\",   \"dateRange\": {    \"from\": \"now-1M\",    \"to\": \"now\"   },   \"sortField\": \"published\",   \"sortOrder\": \"desc\",   \"dateGap\": \"day\",   \"lang\": \"fr\",   \"wantCluster\": true,   \"maxRows\": 50,   \"fields\": [   ],   \"wantedFacets\": {    \"product\": {     \"minDocCount\": 1,     \"size\": 5    },    \"iptc\": {     \"minDocCount\": 1,     \"size\": 100    },    \"country\": {     \"minDocCount\": 1,     \"size\": 100    },    \"city\": {     \"minDocCount\": 1,     \"size\": 100    },    \"slug\": {     \"minDocCount\": 1,     \"size\": 3    },    \"urgency\": {     \"minDocCount\": 1,     \"size\": 100    }   },   \"query\": {    \"name\": \"product\",    \"and\": \"multimedia\"   }  } ¡¡¡  !!! %%%Combined parameter searching items in news and multimedia production concerning the France and Germany country  json {   \"tz\": \"Europe/Paris\",   \"dateRange\": {    \"from\": \"now-1M\",    \"to\": \"now\"   },   \"sortField\": \"published\",   \"sortOrder\": \"desc\",   \"dateGap\": \"day\",   \"lang\": \"fr\",   \"wantCluster\": true,   \"maxRows\": 50,   \"fields\": [   ],   \"wantedFacets\": {    \"product\": {     \"minDocCount\": 1,     \"size\": 5    },    \"iptc\": {     \"minDocCount\": 1,     \"size\": 100    },    \"country\": {     \"minDocCount\": 1,     \"size\": 100    },    \"city\": {     \"minDocCount\": 1,     \"size\": 100    },    \"slug\": {     \"minDocCount\": 1,     \"size\": 3    },    \"urgency\": {     \"minDocCount\": 1,     \"size\": 100    }   },   \"query\": {    \"and\": [     {      \"name\": \"product\",      \"in\": [       \"multimedia\",       \"news\"      ]     },     {      \"name\": \"country\",      \"or\": [       \"FRA\",       \"DEU\"      ]     }    ]   }  } ¡¡¡   !!! %%%Complex parameter searching items in multimedia production concerning the France country and the talking about rugby without \"TOP 14\" national league   {   ...      \"query\": {    \"and\": [     {      \"name\": \"product\",      \"in\": [       \"multimedia\"      ]     },     {      \"name\": \"country\",      \"in\": [       \"FRA\"      ]     },     {      \"and\": [       {        \"name\": \"news\",        \"contains\": [         \"rugby\"        ]       },       {        \"exclude\": [         {          \"name\": \"news\",          \"contains\": [           \"TOP 14\"          ]         }        ]       }      ]     }    ]   }  } ¡¡¡ ---
 *
 * OpenAPI spec version: v0.3.0
 * Contact: api-support@afp.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.ina.research.afp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HttpMethod;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;

import fr.ina.research.afp.auth.ApiKeyAuth;
import fr.ina.research.afp.auth.Authentication;
import fr.ina.research.afp.auth.HttpBasicAuth;
import fr.ina.research.afp.auth.OAuth;
import okio.BufferedSink;
import okio.Okio;

public class ApiClient {
	public static final double JAVA_VERSION;
	public static final boolean IS_ANDROID;
	public static final int ANDROID_SDK_VERSION;

	static {
		JAVA_VERSION = Double.parseDouble(System.getProperty("java.specification.version"));
		boolean isAndroid;
		try {
			Class.forName("android.app.Activity");
			isAndroid = true;
		} catch (ClassNotFoundException e) {
			isAndroid = false;
		}
		IS_ANDROID = isAndroid;
		int sdkVersion = 0;
		if (IS_ANDROID) {
			try {
				sdkVersion = Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt(null);
			} catch (Exception e) {
				try {
					sdkVersion = Integer.parseInt((String) Class.forName("android.os.Build$VERSION").getField("SDK").get(null));
				} catch (Exception e2) {
				}
			}
		}
		ANDROID_SDK_VERSION = sdkVersion;
	}

	/**
	 * The datetime format to be used when <code>lenientDatetimeFormat</code> is
	 * enabled.
	 */
	public static final String LENIENT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	private String basePath = "https://api.afpforum.com/";
	private boolean lenientOnJson = false;
	private boolean debugging = false;
	private Map<String, String> defaultHeaderMap = new HashMap<>();
	private String tempFolderPath = null;

	private Map<String, Authentication> authentications;

	private DateFormat dateFormat;
	private DateFormat datetimeFormat;
	private boolean lenientDatetimeFormat;
	private int dateLength;

	private InputStream sslCaCert;
	private boolean verifyingSsl;

	private OkHttpClient httpClient;
	private JSON json;

	private HttpLoggingInterceptor loggingInterceptor;

	/*
	 * Constructor for ApiClient
	 */
	public ApiClient() {
		httpClient = new OkHttpClient();

		verifyingSsl = true;

		json = new JSON(this);

		/*
		 * Use RFC3339 format for date and datetime. See
		 * http://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14
		 */
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// Always use UTC as the default time zone when dealing with date
		// (without time).
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		initDatetimeFormat();

		// Be lenient on datetime formats when parsing datetime from string.
		// See <code>parseDatetime</code>.
		lenientDatetimeFormat = true;

		// Set default User-Agent.
		setUserAgent("INA; OTMedia; afp-api-client v0.3.0");

		// Setup authentications (key: authentication name, value:
		// authentication).
		authentications = new HashMap<>();
		authentications.put("oauth2", new OAuth());
		// Prevent the authentications from being modified.
		authentications = Collections.unmodifiableMap(authentications);
	}

	/**
	 * Add a default header.
	 *
	 * @param key
	 *            The header's key
	 * @param value
	 *            The header's value
	 * @return ApiClient
	 */
	public ApiClient addDefaultHeader(String key, String value) {
		defaultHeaderMap.put(key, value);
		return this;
	}

	/**
	 * Apply SSL related settings to httpClient according to the current values
	 * of verifyingSsl and sslCaCert.
	 */
	private void applySslSettings() {
		try {
			KeyManager[] keyManagers = null;
			TrustManager[] trustManagers = null;
			HostnameVerifier hostnameVerifier = null;
			if (!verifyingSsl) {
				TrustManager trustAll = new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				};
				SSLContext sslContext = SSLContext.getInstance("TLS");
				trustManagers = new TrustManager[] { trustAll };
				hostnameVerifier = new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
			} else if (sslCaCert != null) {
				char[] password = null; // Any password will work.
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
				Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(sslCaCert);
				if (certificates.isEmpty()) {
					throw new IllegalArgumentException("expected non-empty set of trusted certificates");
				}
				KeyStore caKeyStore = newEmptyKeyStore(password);
				int index = 0;
				for (Certificate certificate : certificates) {
					String certificateAlias = "ca" + Integer.toString(index++);
					caKeyStore.setCertificateEntry(certificateAlias, certificate);
				}
				TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				trustManagerFactory.init(caKeyStore);
				trustManagers = trustManagerFactory.getTrustManagers();
			}

			if ((keyManagers != null) || (trustManagers != null)) {
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(keyManagers, trustManagers, new SecureRandom());
				httpClient.setSslSocketFactory(sslContext.getSocketFactory());
			} else {
				httpClient.setSslSocketFactory(null);
			}
			httpClient.setHostnameVerifier(hostnameVerifier);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public Call buildCall(Request r) throws ApiException {
		return httpClient.newCall(r);
	}

	/**
	 * Build HTTP call with the given options.
	 *
	 * @param path
	 *            The sub-path of the HTTP URL
	 * @param method
	 *            The request method, one of "GET", "HEAD", "OPTIONS", "POST",
	 *            "PUT", "PATCH" and "DELETE"
	 * @param queryParams
	 *            The query parameters
	 * @param body
	 *            The request body object
	 * @param headerParams
	 *            The header parameters
	 * @param formParams
	 *            The form parameters
	 * @param authNames
	 *            The authentications to apply
	 * @param progressRequestListener
	 *            Progress request listener
	 * @return The HTTP call
	 * @throws ApiException
	 *             If fail to serialize the request body object
	 */
	public Call buildCall(String path, String method, List<Pair> queryParams, Object body, Map<String, String> headerParams, Map<String, Object> formParams, String[] authNames,
			ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Request r = buildRequest(path, method, queryParams, body, headerParams, formParams, authNames, progressRequestListener);

		return buildCall(r);
	}

	public Request buildRequest(String path, String method, List<Pair> queryParams, Object body, Map<String, String> headerParams, Map<String, Object> formParams,
			String[] authNames, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		updateParamsForAuth(authNames, queryParams, headerParams);

		final String url = buildUrl(path, queryParams);
		final Request.Builder reqBuilder = new Request.Builder().url(url);
		processHeaderParams(headerParams, reqBuilder);

		String contentType = headerParams.get("Content-Type");
		// ensuring a default content type
		if (contentType == null) {
			contentType = "application/json";
		}

		RequestBody reqBody;
		if (!HttpMethod.permitsRequestBody(method)) {
			reqBody = null;
		} else if ("application/x-www-form-urlencoded".equals(contentType)) {
			reqBody = buildRequestBodyFormEncoding(formParams);
		} else if ("multipart/form-data".equals(contentType)) {
			reqBody = buildRequestBodyMultipart(formParams);
		} else if (body == null) {
			if ("DELETE".equals(method)) {
				// allow calling DELETE without sending a request body
				reqBody = null;
			} else {
				// use an empty request body (for POST, PUT and PATCH)
				reqBody = RequestBody.create(MediaType.parse(contentType), "");
			}
		} else {
			reqBody = serialize(body, contentType);
		}

		Request request = null;

		if ((progressRequestListener != null) && (reqBody != null)) {
			ProgressRequestBody progressRequestBody = new ProgressRequestBody(reqBody, progressRequestListener);
			request = reqBuilder.method(method, progressRequestBody).build();
		} else {
			request = reqBuilder.method(method, reqBody).build();
		}

		return request;
	}

	/**
	 * Build a form-encoding request body with the given form parameters.
	 *
	 * @param formParams
	 *            Form parameters in the form of Map
	 * @return RequestBody
	 */
	public RequestBody buildRequestBodyFormEncoding(Map<String, Object> formParams) {
		FormEncodingBuilder formBuilder = new FormEncodingBuilder();
		for (Entry<String, Object> param : formParams.entrySet()) {
			formBuilder.add(param.getKey(), parameterToString(param.getValue()));
		}
		return formBuilder.build();
	}

	/**
	 * Build a multipart (file uploading) request body with the given form
	 * parameters, which could contain text fields and file fields.
	 *
	 * @param formParams
	 *            Form parameters in the form of Map
	 * @return RequestBody
	 */
	public RequestBody buildRequestBodyMultipart(Map<String, Object> formParams) {
		MultipartBuilder mpBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
		for (Entry<String, Object> param : formParams.entrySet()) {
			if (param.getValue() instanceof File) {
				File file = (File) param.getValue();
				Headers partHeaders = Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\"; filename=\"" + file.getName() + "\"");
				MediaType mediaType = MediaType.parse(guessContentTypeFromFile(file));
				mpBuilder.addPart(partHeaders, RequestBody.create(mediaType, file));
			} else {
				Headers partHeaders = Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\"");
				mpBuilder.addPart(partHeaders, RequestBody.create(null, parameterToString(param.getValue())));
			}
		}
		return mpBuilder.build();
	}

	/**
	 * Build full URL by concatenating base path, the given sub path and query
	 * parameters.
	 *
	 * @param path
	 *            The sub path
	 * @param queryParams
	 *            The query parameters
	 * @return The full URL
	 */
	public String buildUrl(String path, List<Pair> queryParams) {
		final StringBuilder url = new StringBuilder();
		url.append(basePath).append(path);

		if ((queryParams != null) && !queryParams.isEmpty()) {
			// support (constant) query string in `path`, e.g. "/posts?draft=1"
			String prefix = path.contains("?") ? "&" : "?";
			for (Pair param : queryParams) {
				if (param.getValue() != null) {
					if (prefix != null) {
						url.append(prefix);
						prefix = null;
					} else {
						url.append("&");
					}
					String value = parameterToString(param.getValue());
					url.append(escapeString(param.getName())).append("=").append(escapeString(value));
				}
			}
		}

		return url.toString();
	}

	/**
	 * Deserialize response body to Java object, according to the return type
	 * and the Content-Type response header.
	 *
	 * @param <T>
	 *            Type
	 * @param response
	 *            HTTP response
	 * @param returnType
	 *            The type of the Java object
	 * @return The deserialized Java object
	 * @throws ApiException
	 *             If fail to deserialize response body, i.e. cannot read
	 *             response body or the Content-Type of the response is not
	 *             supported.
	 */
	@SuppressWarnings("unchecked")
	public <T> T deserialize(Response response, Type returnType) throws ApiException {
		if ((response == null) || (returnType == null)) {
			return null;
		}

		if ("byte[]".equals(returnType.toString())) {
			// Handle binary response (byte array).
			try {
				return (T) response.body().bytes();
			} catch (IOException e) {
				throw new ApiException(e);
			}
		} else if (returnType.equals(File.class)) {
			// Handle file downloading.
			return (T) downloadFileFromResponse(response);
		}

		String respBody;
		try {
			if (response.body() != null) {
				respBody = response.body().string();
			} else {
				respBody = null;
			}
		} catch (IOException e) {
			throw new ApiException(e);
		}

		if ((respBody == null) || "".equals(respBody)) {
			return null;
		}

		String contentType = response.headers().get("Content-Type");
		if (contentType == null) {
			// ensuring a default content type
			contentType = "application/json";
		}
		if (isJsonMime(contentType)) {
			return json.deserialize(respBody, returnType);
		} else if (returnType.equals(String.class)) {
			// Expecting string, return the raw response body.
			return (T) respBody;
		} else {
			throw new ApiException("Content type \"" + contentType + "\" is not supported for type: " + returnType, response.code(), response.headers().toMultimap(), respBody);
		}
	}

	/**
	 * Download file from the given response.
	 *
	 * @param response
	 *            An instance of the Response object
	 * @throws ApiException
	 *             If fail to read file content from response and write to disk
	 * @return Downloaded file
	 */
	public File downloadFileFromResponse(Response response) throws ApiException {
		try {
			File file = prepareDownloadFile(response);
			BufferedSink sink = Okio.buffer(Okio.sink(file));
			sink.writeAll(response.body().source());
			sink.close();
			return file;
		} catch (IOException e) {
			throw new ApiException(e);
		}
	}

	/**
	 * Escape the given string to be used as URL query value.
	 *
	 * @param str
	 *            String to be escaped
	 * @return Escaped string
	 */
	public String escapeString(String str) {
		try {
			return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * {@link #execute(Call, Type)}
	 *
	 * @param <T>
	 *            Type
	 * @param call
	 *            An instance of the Call object
	 * @throws ApiException
	 *             If fail to execute the call
	 * @return ApiResponse&lt;T&gt;
	 */
	public <T> ApiResponse<T> execute(Call call) throws ApiException {
		return execute(call, null);
	}

	/**
	 * Execute HTTP call and deserialize the HTTP response body into the given
	 * return type.
	 *
	 * @param returnType
	 *            The return type used to deserialize HTTP response body
	 * @param <T>
	 *            The return type corresponding to (same with) returnType
	 * @param call
	 *            Call
	 * @return ApiResponse object containing response status, headers and data,
	 *         which is a Java object deserialized from response body and would
	 *         be null when returnType is null.
	 * @throws ApiException
	 *             If fail to execute the call
	 */
	public <T> ApiResponse<T> execute(Call call, Type returnType) throws ApiException {
		try {
			Response response = call.execute();
			T data = handleResponse(response, returnType);
			return new ApiResponse<>(response.code(), response.headers().toMultimap(), data);
		} catch (IOException e) {
			throw new ApiException(e);
		}
	}

	/**
	 * {@link #executeAsync(Call, Type, ApiCallback)}
	 *
	 * @param <T>
	 *            Type
	 * @param call
	 *            An instance of the Call object
	 * @param callback
	 *            ApiCallback&lt;T&gt;
	 */
	public <T> void executeAsync(Call call, ApiCallback<T> callback) {
		executeAsync(call, null, callback);
	}

	/**
	 * Execute HTTP call asynchronously.
	 *
	 * @see #execute(Call, Type)
	 * @param <T>
	 *            Type
	 * @param call
	 *            The callback to be executed when the API call finishes
	 * @param returnType
	 *            Return type
	 * @param callback
	 *            ApiCallback
	 */
	@SuppressWarnings("unchecked")
	public <T> void executeAsync(Call call, final Type returnType, final ApiCallback<T> callback) {
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				callback.onFailure(new ApiException(e), 0, null);
			}

			@Override
			public void onResponse(Response response) throws IOException {
				T result;
				try {
					result = (T) handleResponse(response, returnType);
				} catch (ApiException e) {
					callback.onFailure(e, response.code(), response.headers().toMultimap());
					return;
				}
				callback.onSuccess(result, response.code(), response.headers().toMultimap());
			}
		});
	}

	/**
	 * Format the given Date object into string (Date format).
	 *
	 * @param date
	 *            Date object
	 * @return Formatted date in string representation
	 */
	public String formatDate(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * Format the given Date object into string (Datetime format).
	 *
	 * @param date
	 *            Date object
	 * @return Formatted datetime in string representation
	 */
	public String formatDatetime(Date date) {
		return datetimeFormat.format(date);
	}

	/**
	 * Get authentication for the given name.
	 *
	 * @param authName
	 *            The authentication name
	 * @return The authentication, null if not found
	 */
	public Authentication getAuthentication(String authName) {
		return authentications.get(authName);
	}

	/**
	 * Get authentications (key: authentication name, value: authentication).
	 *
	 * @return Map of authentication objects
	 */
	public Map<String, Authentication> getAuthentications() {
		return authentications;
	}

	/**
	 * Get base path
	 *
	 * @return Baes path
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * Get connection timeout (in milliseconds).
	 *
	 * @return Timeout in milliseconds
	 */
	public int getConnectTimeout() {
		return httpClient.getConnectTimeout();
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public DateFormat getDatetimeFormat() {
		return datetimeFormat;
	}

	/**
	 * Get HTTP client
	 *
	 * @return An instance of OkHttpClient
	 */
	public OkHttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * Get JSON
	 *
	 * @return JSON object
	 */
	public JSON getJSON() {
		return json;
	}

	public int getReadTimeout() {
		return httpClient.getReadTimeout();
	}

	/**
	 * Get SSL CA cert.
	 *
	 * @return Input stream to the SSL CA cert
	 */
	public InputStream getSslCaCert() {
		return sslCaCert;
	}

	/**
	 * The path of temporary folder used to store downloaded files from
	 * endpoints with file response. The default value is <code>null</code>,
	 * i.e. using the system's default tempopary folder.
	 *
	 * @see <a href=
	 *      "https://docs.oracle.com/javase/7/docs/api/java/io/File.html#createTempFile">createTempFile</a>
	 * @return Temporary folder path
	 */
	public String getTempFolderPath() {
		return tempFolderPath;
	}

	public int getWriteTimeout() {
		return httpClient.getWriteTimeout();
	}

	/**
	 * Guess Content-Type header from the given file (defaults to
	 * "application/octet-stream").
	 *
	 * @param file
	 *            The given file
	 * @return The guessed Content-Type
	 */
	public String guessContentTypeFromFile(File file) {
		String contentType = URLConnection.guessContentTypeFromName(file.getName());
		if (contentType == null) {
			return "application/octet-stream";
		} else {
			return contentType;
		}
	}

	/**
	 * Handle the given response, return the deserialized object when the
	 * response is successful.
	 *
	 * @param <T>
	 *            Type
	 * @param response
	 *            Response
	 * @param returnType
	 *            Return type
	 * @throws ApiException
	 *             If the response has a unsuccessful status code or fail to
	 *             deserialize the response body
	 * @return Type
	 */
	public <T> T handleResponse(Response response, Type returnType) throws ApiException {
		if (response.isSuccessful()) {
			if ((returnType == null) || (response.code() == 204)) {
				// returning null if the returnType is not defined,
				// or the status code is 204 (No Content)
				return null;
			} else {
				return deserialize(response, returnType);
			}
		} else {
			String respBody = null;
			if (response.body() != null) {
				try {
					respBody = response.body().string();
				} catch (IOException e) {
					throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
				}
			}
			throw new ApiException(response.message(), response.code(), response.headers().toMultimap(), respBody);
		}
	}

	/**
	 * Initialize datetime format according to the current environment, e.g.
	 * Java 1.7 and Android.
	 */
	private void initDatetimeFormat() {
		String formatWithTimeZone = null;
		if (IS_ANDROID) {
			if (ANDROID_SDK_VERSION >= 18) {
				// The time zone format "ZZZZZ" is available since Android 4.3
				// (SDK version 18)
				formatWithTimeZone = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
			}
		} else if (JAVA_VERSION >= 1.7) {
			// The time zone format "XXX" is available since Java 1.7
			formatWithTimeZone = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
		}
		if (formatWithTimeZone != null) {
			datetimeFormat = new SimpleDateFormat(formatWithTimeZone);
			// NOTE: Use the system's default time zone (mainly for datetime
			// formatting).
		} else {
			// Use a common format that works across all systems.
			datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			// Always use the UTC time zone as we are using a constant trailing
			// "Z" here.
			datetimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
	}

	/**
	 * Check that whether debugging is enabled for this API client.
	 *
	 * @return True if debugging is enabled, false otherwise.
	 */
	public boolean isDebugging() {
		return debugging;
	}

	/**
	 * Check if the given MIME is a JSON MIME. JSON MIME examples:
	 * application/json application/json; charset=UTF8 APPLICATION/JSON
	 *
	 * @param mime
	 *            MIME (Multipurpose Internet Mail Extensions)
	 * @return True if the given MIME is JSON, false otherwise.
	 */
	public boolean isJsonMime(String mime) {
		return (mime != null) && mime.matches("(?i)application\\/json(;.*)?");
	}

	/**
	 * Whether to allow various ISO 8601 datetime formats when parsing a
	 * datetime string.
	 *
	 * @see #parseDatetime(String)
	 * @return True if lenientDatetimeFormat flag is set to true
	 */
	public boolean isLenientDatetimeFormat() {
		return lenientDatetimeFormat;
	}

	/**
	 * @see <a href=
	 *      "https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/stream/JsonReader.html#setLenient(boolean)">setLenient</a>
	 *
	 * @return True if lenientOnJson is enabled, false otherwise.
	 */
	public boolean isLenientOnJson() {
		return lenientOnJson;
	}

	/**
	 * True if isVerifyingSsl flag is on
	 *
	 * @return True if isVerifySsl flag is on
	 */
	public boolean isVerifyingSsl() {
		return verifyingSsl;
	}

	private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, password);
			return keyStore;
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * Format to {@code Pair} objects.
	 *
	 * @param collectionFormat
	 *            collection format (e.g. csv, tsv)
	 * @param name
	 *            Name
	 * @param value
	 *            Value
	 * @return A list of Pair objects
	 */
	public List<Pair> parameterToPairs(String collectionFormat, String name, Object value) {
		List<Pair> params = new ArrayList<>();

		// preconditions
		if ((name == null) || name.isEmpty() || (value == null)) {
			return params;
		}

		Collection valueCollection = null;
		if (value instanceof Collection) {
			valueCollection = (Collection) value;
		} else {
			params.add(new Pair(name, parameterToString(value)));
			return params;
		}

		if (valueCollection.isEmpty()) {
			return params;
		}

		// get the collection format
		collectionFormat = ((collectionFormat == null) || collectionFormat.isEmpty() ? "csv" : collectionFormat); // default:
																													// csv

		// create the params based on the collection format
		if (collectionFormat.equals("multi")) {
			for (Object item : valueCollection) {
				params.add(new Pair(name, parameterToString(item)));
			}

			return params;
		}

		String delimiter = ",";

		if (collectionFormat.equals("csv")) {
			delimiter = ",";
		} else if (collectionFormat.equals("ssv")) {
			delimiter = " ";
		} else if (collectionFormat.equals("tsv")) {
			delimiter = "\t";
		} else if (collectionFormat.equals("pipes")) {
			delimiter = "|";
		}

		StringBuilder sb = new StringBuilder();
		for (Object item : valueCollection) {
			sb.append(delimiter);
			sb.append(parameterToString(item));
		}

		params.add(new Pair(name, sb.substring(1)));

		return params;
	}

	/**
	 * Format the given parameter object into string.
	 *
	 * @param param
	 *            Parameter
	 * @return String representation of the parameter
	 */
	public String parameterToString(Object param) {
		if (param == null) {
			return "";
		} else if (param instanceof Date) {
			return formatDatetime((Date) param);
		} else if (param instanceof Collection) {
			StringBuilder b = new StringBuilder();
			for (Object o : (Collection) param) {
				if (b.length() > 0) {
					b.append(",");
				}
				b.append(String.valueOf(o));
			}
			return b.toString();
		} else {
			return String.valueOf(param);
		}
	}

	/**
	 * Parse the given date string into Date object. The default
	 * <code>dateFormat</code> supports these ISO 8601 date formats: 2015-08-16
	 * 2015-8-16
	 *
	 * @param str
	 *            String to be parsed
	 * @return Date
	 */
	public Date parseDate(String str) {
		if (str == null) {
			return null;
		}
		try {
			return dateFormat.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Parse date or date time in string format into Date object.
	 *
	 * @param str Date time string to be parsed
	 *
	 * @return Date representation of the string
	 */
	public Date parseDateOrDatetime(String str) {
		if (str == null) {
			return null;
		} else if (str.length() <= dateLength) {
			return parseDate(str);
		} else {
			return parseDatetime(str);
		}
	}

	/**
	 * Parse the given datetime string into Date object. When
	 * lenientDatetimeFormat is enabled, the following ISO 8601 datetime formats
	 * are supported: 2015-08-16T08:20:05Z 2015-8-16T8:20:05Z
	 * 2015-08-16T08:20:05+00:00 2015-08-16T08:20:05+0000
	 * 2015-08-16T08:20:05.376Z 2015-08-16T08:20:05.376+00:00
	 * 2015-08-16T08:20:05.376+00 Note: The 3-digit milli-seconds is optional.
	 * Time zone is required and can be in one of these formats: Z (same with
	 * +0000) +08:00 (same with +0800) -02 (same with -0200) -0200
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
	 * @param str
	 *            Date time string to be parsed
	 * @return Date representation of the string
	 */
	public Date parseDatetime(String str) {
		if (str == null) {
			return null;
		}

		DateFormat format;
		if (lenientDatetimeFormat) {
			/*
			 * When lenientDatetimeFormat is enabled, normalize the date string
			 * into <code>LENIENT_DATETIME_FORMAT</code> to support various
			 * formats defined by ISO 8601.
			 */
			// normalize time zone
			// trailing "Z": 2015-08-16T08:20:05Z => 2015-08-16T08:20:05+0000
			str = str.replaceAll("[zZ]\\z", "+0000");
			// remove colon in time zone: 2015-08-16T08:20:05+00:00 =>
			// 2015-08-16T08:20:05+0000
			str = str.replaceAll("([+-]\\d{2}):(\\d{2})\\z", "$1$2");
			// expand time zone: 2015-08-16T08:20:05+00 =>
			// 2015-08-16T08:20:05+0000
			str = str.replaceAll("([+-]\\d{2})\\z", "$100");
			// add milliseconds when missing
			// 2015-08-16T08:20:05+0000 => 2015-08-16T08:20:05.000+0000
			str = str.replaceAll("(:\\d{1,2})([+-]\\d{4})\\z", "$1.000$2");
			format = new SimpleDateFormat(LENIENT_DATETIME_FORMAT);
		} else {
			format = datetimeFormat;
		}

		try {
			return format.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prepare file for download
	 *
	 * @param response
	 *            An instance of the Response object
	 * @throws IOException
	 *             If fail to prepare file for download
	 * @return Prepared file for the download
	 */
	public File prepareDownloadFile(Response response) throws IOException {
		String filename = null;
		String contentDisposition = response.header("Content-Disposition");
		if ((contentDisposition != null) && !"".equals(contentDisposition)) {
			// Get filename from the Content-Disposition header.
			Pattern pattern = Pattern.compile("filename=['\"]?([^'\"\\s]+)['\"]?");
			Matcher matcher = pattern.matcher(contentDisposition);
			if (matcher.find()) {
				filename = sanitizeFilename(matcher.group(1));
			}
		}

		String prefix = null;
		String suffix = null;
		if (filename == null) {
			prefix = "download-";
			suffix = "";
		} else {
			int pos = filename.lastIndexOf(".");
			if (pos == -1) {
				prefix = filename + "-";
			} else {
				prefix = filename.substring(0, pos) + "-";
				suffix = filename.substring(pos);
			}
			// File.createTempFile requires the prefix to be at least three
			// characters long
			if (prefix.length() < 3) {
				prefix = "download-";
			}
		}

		if (tempFolderPath == null) {
			return File.createTempFile(prefix, suffix);
		} else {
			return File.createTempFile(prefix, suffix, new File(tempFolderPath));
		}
	}

	/**
	 * Set header parameters to the request builder, including default headers.
	 *
	 * @param headerParams
	 *            Header parameters in the ofrm of Map
	 * @param reqBuilder
	 *            Reqeust.Builder
	 */
	public void processHeaderParams(Map<String, String> headerParams, Request.Builder reqBuilder) {
		for (Entry<String, String> param : headerParams.entrySet()) {
			reqBuilder.header(param.getKey(), parameterToString(param.getValue()));
		}
		for (Entry<String, String> header : defaultHeaderMap.entrySet()) {
			if (!headerParams.containsKey(header.getKey())) {
				reqBuilder.header(header.getKey(), parameterToString(header.getValue()));
			}
		}
	}

	/**
	 * Sanitize filename by removing path. e.g. ../../sun.gif becomes sun.gif
	 *
	 * @param filename
	 *            The filename to be sanitized
	 * @return The sanitized filename
	 */
	public String sanitizeFilename(String filename) {
		return filename.replaceAll(".*[/\\\\]", "");
	}

	/**
	 * Select the Accept header's value from the given accepts array: if JSON
	 * exists in the given array, use it; otherwise use all of them (joining
	 * into a string)
	 *
	 * @param accepts
	 *            The accepts array to select from
	 * @return The Accept header to use. If the given array is empty, null will
	 *         be returned (not to set the Accept header explicitly).
	 */
	public String selectHeaderAccept(String[] accepts) {
		if (accepts.length == 0) {
			return null;
		}
		for (String accept : accepts) {
			if (isJsonMime(accept)) {
				return accept;
			}
		}
		return StringUtil.join(accepts, ",");
	}

	/**
	 * Select the Content-Type header's value from the given array: if JSON
	 * exists in the given array, use it; otherwise use the first one of the
	 * array.
	 *
	 * @param contentTypes
	 *            The Content-Type array to select from
	 * @return The Content-Type header to use. If the given array is empty, JSON
	 *         will be used.
	 */
	public String selectHeaderContentType(String[] contentTypes) {
		if (contentTypes.length == 0) {
			return "application/json";
		}
		for (String contentType : contentTypes) {
			if (isJsonMime(contentType)) {
				return contentType;
			}
		}
		return contentTypes[0];
	}

	/**
	 * Serialize the given Java object into request body according to the
	 * object's class and the request Content-Type.
	 *
	 * @param obj
	 *            The Java object
	 * @param contentType
	 *            The request Content-Type
	 * @return The serialized request body
	 * @throws ApiException
	 *             If fail to serialize the given object
	 */
	public RequestBody serialize(Object obj, String contentType) throws ApiException {
		if (obj instanceof byte[]) {
			// Binary (byte array) body parameter support.
			return RequestBody.create(MediaType.parse(contentType), (byte[]) obj);
		} else if (obj instanceof File) {
			// File body parameter support.
			return RequestBody.create(MediaType.parse(contentType), (File) obj);
		} else if (isJsonMime(contentType)) {
			String content;
			if (obj != null) {
				content = json.serialize(obj);
			} else {
				content = null;
			}
			return RequestBody.create(MediaType.parse(contentType), content);
		} else {
			throw new ApiException("Content type \"" + contentType + "\" is not supported");
		}
	}

	/**
	 * Helper method to set access token for the first OAuth2 authentication.
	 *
	 * @param accessToken
	 *            Access token
	 */
	public void setAccessToken(String accessToken) {
		for (Authentication auth : authentications.values()) {
			if (auth instanceof OAuth) {
				((OAuth) auth).setAccessToken(accessToken);
				return;
			}
		}
		throw new RuntimeException("No OAuth2 authentication configured!");
	}

	/**
	 * Helper method to set API key value for the first API key authentication.
	 *
	 * @param apiKey
	 *            API key
	 */
	public void setApiKey(String apiKey) {
		for (Authentication auth : authentications.values()) {
			if (auth instanceof ApiKeyAuth) {
				((ApiKeyAuth) auth).setApiKey(apiKey);
				return;
			}
		}
		throw new RuntimeException("No API key authentication configured!");
	}

	/**
	 * Helper method to set API key prefix for the first API key authentication.
	 *
	 * @param apiKeyPrefix
	 *            API key prefix
	 */
	public void setApiKeyPrefix(String apiKeyPrefix) {
		for (Authentication auth : authentications.values()) {
			if (auth instanceof ApiKeyAuth) {
				((ApiKeyAuth) auth).setApiKeyPrefix(apiKeyPrefix);
				return;
			}
		}
		throw new RuntimeException("No API key authentication configured!");
	}

	/**
	 * Set base path
	 *
	 * @param basePath
	 *            Base path of the URL (e.g https://api.afpforum.com/
	 * @return An instance of OkHttpClient
	 */
	public ApiClient setBasePath(String basePath) {
		this.basePath = basePath;
		return this;
	}

	/**
	 * Sets the connect timeout (in milliseconds). A value of 0 means no
	 * timeout, otherwise values must be between 1 and
	 *
	 * @param connectionTimeout
	 *            connection timeout in milliseconds
	 * @return Api client
	 */
	public ApiClient setConnectTimeout(int connectionTimeout) {
		httpClient.setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS);
		return this;
	}

	public ApiClient setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
		dateLength = this.dateFormat.format(new Date()).length();
		return this;
	}

	public ApiClient setDatetimeFormat(DateFormat datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
		return this;
	}

	/**
	 * Enable/disable debugging for this API client.
	 *
	 * @param debugging
	 *            To enable (true) or disable (false) debugging
	 * @return ApiClient
	 */
	public ApiClient setDebugging(boolean debugging) {
		if (debugging != this.debugging) {
			if (debugging) {
				loggingInterceptor = new HttpLoggingInterceptor();
				loggingInterceptor.setLevel(Level.BODY);
				httpClient.interceptors().add(loggingInterceptor);
			} else {
				httpClient.interceptors().remove(loggingInterceptor);
				loggingInterceptor = null;
			}
		}
		this.debugging = debugging;
		return this;
	}

	/**
	 * Set HTTP client
	 *
	 * @param httpClient
	 *            An instance of OkHttpClient
	 * @return Api Client
	 */
	public ApiClient setHttpClient(OkHttpClient httpClient) {
		this.httpClient = httpClient;
		return this;
	}

	/**
	 * Set JSON
	 *
	 * @param json
	 *            JSON object
	 * @return Api client
	 */
	public ApiClient setJSON(JSON json) {
		this.json = json;
		return this;
	}

	public ApiClient setLenientDatetimeFormat(boolean lenientDatetimeFormat) {
		this.lenientDatetimeFormat = lenientDatetimeFormat;
		return this;
	}

	/**
	 * Set LenientOnJson
	 *
	 * @param lenient
	 *            True to enable lenientOnJson
	 * @return ApiClient
	 */
	public ApiClient setLenientOnJson(boolean lenient) {
		lenientOnJson = lenient;
		return this;
	}

	/**
	 * Helper method to set password for the first HTTP basic authentication.
	 *
	 * @param password
	 *            Password
	 */
	public void setPassword(String password) {
		for (Authentication auth : authentications.values()) {
			if (auth instanceof HttpBasicAuth) {
				((HttpBasicAuth) auth).setPassword(password);
				return;
			}
		}
		throw new RuntimeException("No HTTP basic authentication configured!");
	}

	public ApiClient setReadTimeout(long timeout) {
		httpClient.setReadTimeout(timeout, TimeUnit.MILLISECONDS);
		return this;
	}

	/**
	 * Configure the CA certificate to be trusted when making https requests.
	 * Use null to reset to default.
	 *
	 * @param sslCaCert
	 *            input stream for SSL CA cert
	 * @return ApiClient
	 */
	public ApiClient setSslCaCert(InputStream sslCaCert) {
		this.sslCaCert = sslCaCert;
		applySslSettings();
		return this;
	}

	/**
	 * Set the tempoaray folder path (for downloading files)
	 *
	 * @param tempFolderPath
	 *            Temporary folder path
	 * @return ApiClient
	 */
	public ApiClient setTempFolderPath(String tempFolderPath) {
		this.tempFolderPath = tempFolderPath;
		return this;
	}

	/**
	 * Set the User-Agent header's value (by adding to the default header map).
	 *
	 * @param userAgent
	 *            HTTP request's user agent
	 * @return ApiClient
	 */
	public ApiClient setUserAgent(String userAgent) {
		addDefaultHeader("User-Agent", userAgent);
		return this;
	}

	/**
	 * Helper method to set username for the first HTTP basic authentication.
	 *
	 * @param username
	 *            Username
	 */
	public void setUsername(String username) {
		for (Authentication auth : authentications.values()) {
			if (auth instanceof HttpBasicAuth) {
				((HttpBasicAuth) auth).setUsername(username);
				return;
			}
		}
		throw new RuntimeException("No HTTP basic authentication configured!");
	}

	/**
	 * Configure whether to verify certificate and hostname when making https
	 * requests. Default to true. NOTE: Do NOT set to false in production code,
	 * otherwise you would face multiple types of cryptographic attacks.
	 *
	 * @param verifyingSsl
	 *            True to verify TLS/SSL connection
	 * @return ApiClient
	 */
	public ApiClient setVerifyingSsl(boolean verifyingSsl) {
		this.verifyingSsl = verifyingSsl;
		applySslSettings();
		return this;
	}

	public ApiClient setWriteTimeout(long timeout) {
		httpClient.setWriteTimeout(timeout, TimeUnit.MILLISECONDS);
		return this;
	}

	/**
	 * Update query and header parameters based on authentication settings.
	 *
	 * @param authNames
	 *            The authentications to apply
	 * @param queryParams
	 *            List of query parameters
	 * @param headerParams
	 *            Map of header parameters
	 */
	public void updateParamsForAuth(String[] authNames, List<Pair> queryParams, Map<String, String> headerParams) {
		for (String authName : authNames) {
			Authentication auth = authentications.get(authName);
			if (auth == null) {
				throw new RuntimeException("Authentication undefined: " + authName);
			}
			auth.applyToParams(queryParams, headerParams);
		}
	}
}
