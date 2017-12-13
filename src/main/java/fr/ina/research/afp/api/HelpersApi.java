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


package fr.ina.research.afp.api;

import com.google.gson.reflect.TypeToken;

import fr.ina.research.afp.ApiCallback;
import fr.ina.research.afp.ApiClient;
import fr.ina.research.afp.ApiException;
import fr.ina.research.afp.ApiResponse;
import fr.ina.research.afp.Configuration;
import fr.ina.research.afp.Pair;
import fr.ina.research.afp.ProgressRequestBody;
import fr.ina.research.afp.ProgressResponseBody;
import fr.ina.research.afp.api.model.ErrorMapping;
import fr.ina.research.afp.api.model.Parameters;
import fr.ina.research.afp.api.model.ReponseMapping;
import fr.ina.research.afp.api.model.ReponseTopics;
import fr.ina.research.afp.api.model.Result;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpersApi {
    private ApiClient apiClient;

    public HelpersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public HelpersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for documentUsingGET1 */
    private com.squareup.okhttp.Call documentUsingGET1Call(String uno, String wt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'uno' is set
        if (uno == null) {
            throw new ApiException("Missing the required parameter 'uno' when calling documentUsingGET1(Async)");
        }
        
        // verify the required parameter 'wt' is set
        if (wt == null) {
            throw new ApiException("Missing the required parameter 'wt' when calling documentUsingGET1(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/v1/api/get/{uno}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "uno" + "\\}", apiClient.escapeString(uno.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (wt != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "wt", wt));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*_/_*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Get document
     * Return the document for the given UNO
     * @param uno uno (required)
     * @param wt The desired newsItem link format (required)
     * @return Result
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Result documentUsingGET1(String uno, String wt) throws ApiException {
        ApiResponse<Result> resp = documentUsingGET1WithHttpInfo(uno, wt);
        return resp.getData();
    }

    /**
     * Get document
     * Return the document for the given UNO
     * @param uno uno (required)
     * @param wt The desired newsItem link format (required)
     * @return ApiResponse&lt;Result&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Result> documentUsingGET1WithHttpInfo(String uno, String wt) throws ApiException {
        com.squareup.okhttp.Call call = documentUsingGET1Call(uno, wt, null, null);
        Type localVarReturnType = new TypeToken<Result>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get document (asynchronously)
     * Return the document for the given UNO
     * @param uno uno (required)
     * @param wt The desired newsItem link format (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call documentUsingGET1Async(String uno, String wt, final ApiCallback<Result> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = documentUsingGET1Call(uno, wt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Result>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for mappingUsingGET1 */
    private com.squareup.okhttp.Call mappingUsingGET1Call(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        

        // create path and map variables
        String localVarPath = "/v1/api/mapping".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*_/_*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * List field mapping
     * list all field name allowed in document
     * @return ReponseMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ReponseMapping mappingUsingGET1() throws ApiException {
        ApiResponse<ReponseMapping> resp = mappingUsingGET1WithHttpInfo();
        return resp.getData();
    }

    /**
     * List field mapping
     * list all field name allowed in document
     * @return ApiResponse&lt;ReponseMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ReponseMapping> mappingUsingGET1WithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = mappingUsingGET1Call(null, null);
        Type localVarReturnType = new TypeToken<ReponseMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * List field mapping (asynchronously)
     * list all field name allowed in document
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call mappingUsingGET1Async(final ApiCallback<ReponseMapping> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = mappingUsingGET1Call(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ReponseMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for topicsUsingGET1 */
    private com.squareup.okhttp.Call topicsUsingGET1Call(String facet, String lang, Integer minDocCount, Integer size, String tz, String gap, String from, String to, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'facet' is set
        if (facet == null) {
            throw new ApiException("Missing the required parameter 'facet' when calling topicsUsingGET1(Async)");
        }
        
        // verify the required parameter 'lang' is set
        if (lang == null) {
            throw new ApiException("Missing the required parameter 'lang' when calling topicsUsingGET1(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/v1/api/list/{facet}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "facet" + "\\}", apiClient.escapeString(facet.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (minDocCount != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "minDocCount", minDocCount));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "size", size));
        if (lang != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "lang", lang));
        if (tz != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "tz", tz));
        if (gap != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "gap", gap));
        if (from != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "from", from));
        if (to != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "to", to));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*_/_*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * List facet
     * Return all value for a facet
     * @param facet The facet to be listed (required)
     * @param lang Give the lang to search content (required)
     * @param minDocCount The min document number for the facet (optional, default to 1)
     * @param size The size parameter can be set to define how many term facets should be returned out of the overall terms list (optional)
     * @param tz Tell which timezone you are located. By default GMT. (optional, default to GMT)
     * @param gap For the facet date, tell the granularity. Default day. (optional, default to day)
     * @param from The start limit date for search in coda format. Default his according of client profile. (optional)
     * @param to The end limit date for search in coda format. Default his now. (optional, default to now)
     * @return ReponseTopics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ReponseTopics topicsUsingGET1(String facet, String lang, Integer minDocCount, Integer size, String tz, String gap, String from, String to) throws ApiException {
        ApiResponse<ReponseTopics> resp = topicsUsingGET1WithHttpInfo(facet, lang, minDocCount, size, tz, gap, from, to);
        return resp.getData();
    }

    /**
     * List facet
     * Return all value for a facet
     * @param facet The facet to be listed (required)
     * @param lang Give the lang to search content (required)
     * @param minDocCount The min document number for the facet (optional, default to 1)
     * @param size The size parameter can be set to define how many term facets should be returned out of the overall terms list (optional)
     * @param tz Tell which timezone you are located. By default GMT. (optional, default to GMT)
     * @param gap For the facet date, tell the granularity. Default day. (optional, default to day)
     * @param from The start limit date for search in coda format. Default his according of client profile. (optional)
     * @param to The end limit date for search in coda format. Default his now. (optional, default to now)
     * @return ApiResponse&lt;ReponseTopics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ReponseTopics> topicsUsingGET1WithHttpInfo(String facet, String lang, Integer minDocCount, Integer size, String tz, String gap, String from, String to) throws ApiException {
        com.squareup.okhttp.Call call = topicsUsingGET1Call(facet, lang, minDocCount, size, tz, gap, from, to, null, null);
        Type localVarReturnType = new TypeToken<ReponseTopics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * List facet (asynchronously)
     * Return all value for a facet
     * @param facet The facet to be listed (required)
     * @param lang Give the lang to search content (required)
     * @param minDocCount The min document number for the facet (optional, default to 1)
     * @param size The size parameter can be set to define how many term facets should be returned out of the overall terms list (optional)
     * @param tz Tell which timezone you are located. By default GMT. (optional, default to GMT)
     * @param gap For the facet date, tell the granularity. Default day. (optional, default to day)
     * @param from The start limit date for search in coda format. Default his according of client profile. (optional)
     * @param to The end limit date for search in coda format. Default his now. (optional, default to now)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call topicsUsingGET1Async(String facet, String lang, Integer minDocCount, Integer size, String tz, String gap, String from, String to, final ApiCallback<ReponseTopics> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = topicsUsingGET1Call(facet, lang, minDocCount, size, tz, gap, from, to, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ReponseTopics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for topicsUsingPOST */
    private com.squareup.okhttp.Call topicsUsingPOSTCall(String facet, Parameters parameters, Integer minDocCount, Integer size, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = parameters;
        
        // verify the required parameter 'facet' is set
        if (facet == null) {
            throw new ApiException("Missing the required parameter 'facet' when calling topicsUsingPOST(Async)");
        }
        
        // verify the required parameter 'parameters' is set
        if (parameters == null) {
            throw new ApiException("Missing the required parameter 'parameters' when calling topicsUsingPOST(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/v1/api/list/{facet}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "facet" + "\\}", apiClient.escapeString(facet.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (minDocCount != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "minDocCount", minDocCount));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "size", size));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/xml", "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * List facet
     * Return all value for a facet
     * @param facet The facet to be listed (required)
     * @param parameters parameters (required)
     * @param minDocCount The min document number for the facet (optional, default to 1)
     * @param size The size parameter can be set to define how many term facets should be returned out of the overall terms list (optional)
     * @return ReponseTopics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ReponseTopics topicsUsingPOST(String facet, Parameters parameters, Integer minDocCount, Integer size) throws ApiException {
        ApiResponse<ReponseTopics> resp = topicsUsingPOSTWithHttpInfo(facet, parameters, minDocCount, size);
        return resp.getData();
    }

    /**
     * List facet
     * Return all value for a facet
     * @param facet The facet to be listed (required)
     * @param parameters parameters (required)
     * @param minDocCount The min document number for the facet (optional, default to 1)
     * @param size The size parameter can be set to define how many term facets should be returned out of the overall terms list (optional)
     * @return ApiResponse&lt;ReponseTopics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ReponseTopics> topicsUsingPOSTWithHttpInfo(String facet, Parameters parameters, Integer minDocCount, Integer size) throws ApiException {
        com.squareup.okhttp.Call call = topicsUsingPOSTCall(facet, parameters, minDocCount, size, null, null);
        Type localVarReturnType = new TypeToken<ReponseTopics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * List facet (asynchronously)
     * Return all value for a facet
     * @param facet The facet to be listed (required)
     * @param parameters parameters (required)
     * @param minDocCount The min document number for the facet (optional, default to 1)
     * @param size The size parameter can be set to define how many term facets should be returned out of the overall terms list (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call topicsUsingPOSTAsync(String facet, Parameters parameters, Integer minDocCount, Integer size, final ApiCallback<ReponseTopics> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = topicsUsingPOSTCall(facet, parameters, minDocCount, size, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ReponseTopics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
