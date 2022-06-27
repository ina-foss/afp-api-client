/**
 * AFP Core API
 * The first version of the AFP API is an exciting step forward towards making it easier for users to have open access to news articles from Agence France-Presse.  We created it so that you can surface the amazing content AFP share every second, in innovative ways.  Build something great!  Once you've been registered by your AFP marketing contact, it's easy to start requesting data from AFP API.  All endpoints are only accessible via https and are located at `https://afp-apicore-prod.afp.com/v1`.  [External API scheme](https://afp-apicore-prod.afp.com/v1/docs?group=api)  ---  #### AUTHENTICATION ####  To access the service you must get an access token from the authorised end point. You have three ways to get it. The authentication scheme is based on OAuth 2.0.  To access all authentication end points, you must fill in the basic authentication header with the client ID and client secret provided by your AFP marketing contact.  * __Anonymous mode__: this is the way to test the service by fetching news items from the day before: https://afp-apicore-prod.afp.com/oauth/token?grant_type=anonymous  * __Temporary access__: this allows you access to the full service for a short period. You need to provide an authorisation code. This code is given to you by your AFP marketing contact. https://afp-apicore-prod.afp.com/oauth/token?grant_type=authorization_code&code=ACCESS-CODE  * __Permanent access__: If you are a regular customer with a user name and password, you grab an access token from  https://afp-apicore-prod.afp.com/oauth/token?username=USERNAME&password=PASSWORD&grant_type=password  The HTTP Method POST and GET are supported, POST Method preferred. You must fill in the HTTP  Header Basic Authentication with your client ID and your client secret, else you will not be able to use the API.   Sample authentication using curl: curl -su CLIENT_ID:CLIENT_SECRET -H \"Accept: application/json\" \"https://afp-apicore-prod.afp.com/oauth/token?username=USER&password=PASSWORD&grant_type=password   After authentication, you can grab the latest news by accessing the following URL with your access token (replace ACCESS-TOKEN with your own):   https://afp-apicore-prod.afp.com/v1/api/latest?access_token=ACCESS-TOKEN&lang=en&tz=GMT  You're best off using an access_token for the authenticated user for each endpoint, though many endpoints require it. In some cases an access_token will give you more access to information, and in all cases, it means that you are operating under a per-access_token limit.  ---  #### Limit Be nice. ####  If you're sending too many requests too quickly, we'll send back a `503` error code (server unavailable). You are limited to 5000 requests per hour per `access_token` or `client_id` overall. Practically, this means you should (when possible) authenticate users so that limits are well outside the reach of a given user.  ---  #### RESPONSE  ####  Every response is contained by an envelope. That is, each response has a predictable set of keys with which you can expect to interact:   json {    \"response\": {     \"status\": {      \"code\": 0,      \"reason\": \"Success\"     },     ...    }  }  ---  #### STATUS ####  The status key is used to communicate information about the response to the developer. If everything goes fine, you'll only ever see a code key with value 0. However, sometimes things go wrong, and in that case you might see a response like:    json {   \"error\": {    \"error_type\": \"SearchServerException\",    \"code\": 401,    \"message\": \"Access token expired\"   }  }   ---  #### DATE ARGUMENT ####  The date format is compliant to YYYY-MM-DD'T'HH:mm:ssZ format, like 2016-10-24T22:00:00Z, it supports also Date Math expression format  Some examples are:  * `now-1h`: The current time minus one hour, with ms resolution. * `now-1h+1m`: The current time minus one hour plus one minute, with ms resolution. * `now-1h/d`: The current time minus one hour, rounded down to the nearest day. * `now-1M`: The current time minus one month, with ms resolution. * `2015-01-01||+1M/d`: 2015-01-01 plus one month, rounded down to the nearest day.  ---  #### PARAMETERS ####  The parameter object is complex, some examples below  !!! %%%Simple parameter searching items in multimedia production  json {   \"tz\": \"Europe/Paris\",   \"dateRange\": {    \"from\": \"now-1M\",    \"to\": \"now\"   },   \"sortField\": \"published\",   \"sortOrder\": \"desc\",   \"dateGap\": \"day\",   \"lang\": \"fr\",   \"wantCluster\": true,   \"maxRows\": 50,   \"fields\": [   ],   \"wantedFacets\": {    \"product\": {     \"minDocCount\": 1,     \"size\": 5    },    \"iptc\": {     \"minDocCount\": 1,     \"size\": 100    },    \"country\": {     \"minDocCount\": 1,     \"size\": 100    },    \"city\": {     \"minDocCount\": 1,     \"size\": 100    },    \"slug\": {     \"minDocCount\": 1,     \"size\": 3    },    \"urgency\": {     \"minDocCount\": 1,     \"size\": 100    }   },   \"query\": {    \"name\": \"product\",    \"and\": \"multimedia\"   }  } ¡¡¡  !!! %%%Combined parameter searching items in news and multimedia production concerning the France and Germany country  json {   \"tz\": \"Europe/Paris\",   \"dateRange\": {    \"from\": \"now-1M\",    \"to\": \"now\"   },   \"sortField\": \"published\",   \"sortOrder\": \"desc\",   \"dateGap\": \"day\",   \"lang\": \"fr\",   \"wantCluster\": true,   \"maxRows\": 50,   \"fields\": [   ],   \"wantedFacets\": {    \"product\": {     \"minDocCount\": 1,     \"size\": 5    },    \"iptc\": {     \"minDocCount\": 1,     \"size\": 100    },    \"country\": {     \"minDocCount\": 1,     \"size\": 100    },    \"city\": {     \"minDocCount\": 1,     \"size\": 100    },    \"slug\": {     \"minDocCount\": 1,     \"size\": 3    },    \"urgency\": {     \"minDocCount\": 1,     \"size\": 100    }   },   \"query\": {    \"and\": [     {      \"name\": \"product\",      \"in\": [       \"multimedia\",       \"news\"      ]     },     {      \"name\": \"country\",      \"or\": [       \"FRA\",       \"DEU\"      ]     }    ]   }  } ¡¡¡   !!! %%%Complex parameter searching items in multimedia production concerning the France country and the talking about rugby without \"TOP 14\" national league   json {   ...      \"query\": {    \"and\": [     {      \"name\": \"product\",      \"in\": [       \"multimedia\"      ]     },     {      \"name\": \"country\",      \"in\": [       \"FRA\"      ]     },     {      \"and\": [       {        \"name\": \"news\",        \"contains\": [         \"rugby\"        ]       },       {        \"exclude\": [         {          \"name\": \"news\",          \"contains\": [           \"TOP 14\"          ]         }        ]       }      ]     }    ]   }  } ¡¡¡ --- 
 *
 * OpenAPI spec version: 1.1.26
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import fr.ina.research.afp.ApiCallback;
import fr.ina.research.afp.ApiClient;
import fr.ina.research.afp.ApiException;
import fr.ina.research.afp.ApiResponse;
import fr.ina.research.afp.Configuration;
import fr.ina.research.afp.Pair;
import fr.ina.research.afp.ProgressRequestBody;
import fr.ina.research.afp.ProgressResponseBody;
import fr.ina.research.afp.api.model.Parameters;
import fr.ina.research.afp.api.model.Result;

public class MoreLikeThisApi {
    private ApiClient apiClient;

    public MoreLikeThisApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MoreLikeThisApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for moreLikeThisUsingGET1 */
    private com.squareup.okhttp.Call moreLikeThisUsingGET1Call(String uno, String wt, List<String> lang, Integer size, String tz, Boolean c, String gap, List<String> f, List<String> fl, String sort, String from, String to, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'uno' is set
        if (uno == null) {
            throw new ApiException("Missing the required parameter 'uno' when calling moreLikeThisUsingGET1(Async)");
        }
        
        // verify the required parameter 'wt' is set
        if (wt == null) {
            throw new ApiException("Missing the required parameter 'wt' when calling moreLikeThisUsingGET1(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/v1/api/mlt".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (uno != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "uno", uno));
        if (lang != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "lang", lang));
        if (size != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "size", size));
        if (tz != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "tz", tz));
        if (c != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "c", c));
        if (gap != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "gap", gap));
        if (f != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "f", f));
        if (fl != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "fl", fl));
        if (sort != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort", sort));
        if (from != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "from", from));
        if (to != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "to", to));
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
     * More Like This
     * Return all news items looking the news item passed as argument
     * @param uno The UNO for document reference (required)
     * @param wt The desired newsItem link format (required)
     * @param lang Give the lang to search content (optional)
     * @param size The number of responses (optional, default to 50)
     * @param tz Tell which timezone you are located. By default GMT. (optional, default to GMT)
     * @param c Tell if word clustering must calculated, by default no word cluster (optional, default to false)
     * @param gap For the facet date, tell the granularity. Default day. (optional, default to day)
     * @param f Tell which facets must be returned like as \&quot;facetName\&quot;, by default no facet will be returned. Repeatable (optional)
     * @param fl Tell which fields must be returned each news item record, by default all fields will be returned. Repeatable (optional)
     * @param sort Tell on which field the result must be sorted, ascending or descending, by default it\\&#39;s \&quot;published desc\&quot;. All records could be sorted. (optional, default to published desc)
     * @param from The start limit date for search in coda format. Default his according of client profile. (optional)
     * @param to The end limit date for search in coda format. Default his now. (optional, default to now)
     * @return Result
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Result moreLikeThisUsingGET1(String uno, String wt, List<String> lang, Integer size, String tz, Boolean c, String gap, List<String> f, List<String> fl, String sort, String from, String to) throws ApiException {
        ApiResponse<Result> resp = moreLikeThisUsingGET1WithHttpInfo(uno, wt, lang, size, tz, c, gap, f, fl, sort, from, to);
        return resp.getData();
    }

    /**
     * More Like This
     * Return all news items looking the news item passed as argument
     * @param uno The UNO for document reference (required)
     * @param wt The desired newsItem link format (required)
     * @param lang Give the lang to search content (optional)
     * @param size The number of responses (optional, default to 50)
     * @param tz Tell which timezone you are located. By default GMT. (optional, default to GMT)
     * @param c Tell if word clustering must calculated, by default no word cluster (optional, default to false)
     * @param gap For the facet date, tell the granularity. Default day. (optional, default to day)
     * @param f Tell which facets must be returned like as \&quot;facetName\&quot;, by default no facet will be returned. Repeatable (optional)
     * @param fl Tell which fields must be returned each news item record, by default all fields will be returned. Repeatable (optional)
     * @param sort Tell on which field the result must be sorted, ascending or descending, by default it\\&#39;s \&quot;published desc\&quot;. All records could be sorted. (optional, default to published desc)
     * @param from The start limit date for search in coda format. Default his according of client profile. (optional)
     * @param to The end limit date for search in coda format. Default his now. (optional, default to now)
     * @return ApiResponse&lt;Result&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Result> moreLikeThisUsingGET1WithHttpInfo(String uno, String wt, List<String> lang, Integer size, String tz, Boolean c, String gap, List<String> f, List<String> fl, String sort, String from, String to) throws ApiException {
        com.squareup.okhttp.Call call = moreLikeThisUsingGET1Call(uno, wt, lang, size, tz, c, gap, f, fl, sort, from, to, null, null);
        Type localVarReturnType = new TypeToken<Result>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * More Like This (asynchronously)
     * Return all news items looking the news item passed as argument
     * @param uno The UNO for document reference (required)
     * @param wt The desired newsItem link format (required)
     * @param lang Give the lang to search content (optional)
     * @param size The number of responses (optional, default to 50)
     * @param tz Tell which timezone you are located. By default GMT. (optional, default to GMT)
     * @param c Tell if word clustering must calculated, by default no word cluster (optional, default to false)
     * @param gap For the facet date, tell the granularity. Default day. (optional, default to day)
     * @param f Tell which facets must be returned like as \&quot;facetName\&quot;, by default no facet will be returned. Repeatable (optional)
     * @param fl Tell which fields must be returned each news item record, by default all fields will be returned. Repeatable (optional)
     * @param sort Tell on which field the result must be sorted, ascending or descending, by default it\\&#39;s \&quot;published desc\&quot;. All records could be sorted. (optional, default to published desc)
     * @param from The start limit date for search in coda format. Default his according of client profile. (optional)
     * @param to The end limit date for search in coda format. Default his now. (optional, default to now)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call moreLikeThisUsingGET1Async(String uno, String wt, List<String> lang, Integer size, String tz, Boolean c, String gap, List<String> f, List<String> fl, String sort, String from, String to, final ApiCallback<Result> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = moreLikeThisUsingGET1Call(uno, wt, lang, size, tz, c, gap, f, fl, sort, from, to, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Result>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for moreLikeThisUsingPOST */
    private com.squareup.okhttp.Call moreLikeThisUsingPOSTCall(Parameters parameters, String wt, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = parameters;
        
        // verify the required parameter 'parameters' is set
        if (parameters == null) {
            throw new ApiException("Missing the required parameter 'parameters' when calling moreLikeThisUsingPOST(Async)");
        }
        
        // verify the required parameter 'wt' is set
        if (wt == null) {
            throw new ApiException("Missing the required parameter 'wt' when calling moreLikeThisUsingPOST(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/v1/api/mlt".replaceAll("\\{format\\}","json");

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
     * More Like This
     * Return all news items looking the news item passed as argument
     * @param parameters parameters (required)
     * @param wt The desired newsItem link format (required)
     * @return Result
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Result moreLikeThisUsingPOST(Parameters parameters, String wt) throws ApiException {
        ApiResponse<Result> resp = moreLikeThisUsingPOSTWithHttpInfo(parameters, wt);
        return resp.getData();
    }

    /**
     * More Like This
     * Return all news items looking the news item passed as argument
     * @param parameters parameters (required)
     * @param wt The desired newsItem link format (required)
     * @return ApiResponse&lt;Result&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Result> moreLikeThisUsingPOSTWithHttpInfo(Parameters parameters, String wt) throws ApiException {
        com.squareup.okhttp.Call call = moreLikeThisUsingPOSTCall(parameters, wt, null, null);
        Type localVarReturnType = new TypeToken<Result>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * More Like This (asynchronously)
     * Return all news items looking the news item passed as argument
     * @param parameters parameters (required)
     * @param wt The desired newsItem link format (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call moreLikeThisUsingPOSTAsync(Parameters parameters, String wt, final ApiCallback<Result> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = moreLikeThisUsingPOSTCall(parameters, wt, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Result>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
