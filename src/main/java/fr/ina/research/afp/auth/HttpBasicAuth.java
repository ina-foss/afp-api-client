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


package fr.ina.research.afp.auth;

import java.util.List;
import java.util.Map;

import com.squareup.okhttp.Credentials;

import fr.ina.research.afp.Pair;

public class HttpBasicAuth implements Authentication {
    private String username;
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

    @Override
    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
        if (username == null && password == null) {
            return;
        }
        headerParams.put("Authorization", Credentials.basic(
            username == null ? "" : username,
            password == null ? "" : password));
    }
}
