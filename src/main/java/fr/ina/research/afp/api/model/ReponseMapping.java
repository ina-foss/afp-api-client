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


package fr.ina.research.afp.api.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;

import fr.ina.research.afp.api.model.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ReponseMapping
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-19T17:17:54.807+02:00")
public class ReponseMapping   {
  @SerializedName("mapping")
  private Map<String, Map<String, Object>> mapping = new HashMap<String, Map<String, Object>>();

  @SerializedName("numFound")
  private Long numFound = null;

  @SerializedName("status")
  private Status status = null;

  @SerializedName("took")
  private Long took = null;

  public ReponseMapping mapping(Map<String, Map<String, Object>> mapping) {
    this.mapping = mapping;
    return this;
  }

  public ReponseMapping putMappingItem(String key, Map<String, Object> mappingItem) {
    this.mapping.put(key, mappingItem);
    return this;
  }

   /**
   * Get mapping
   * @return mapping
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map<String, Map<String, Object>> getMapping() {
    return mapping;
  }

  public void setMapping(Map<String, Map<String, Object>> mapping) {
    this.mapping = mapping;
  }

  public ReponseMapping numFound(Long numFound) {
    this.numFound = numFound;
    return this;
  }

   /**
   * Get numFound
   * @return numFound
  **/
  @ApiModelProperty(example = "null", value = "")
  public Long getNumFound() {
    return numFound;
  }

  public void setNumFound(Long numFound) {
    this.numFound = numFound;
  }

  public ReponseMapping status(Status status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(example = "null", value = "")
  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public ReponseMapping took(Long took) {
    this.took = took;
    return this;
  }

   /**
   * Get took
   * @return took
  **/
  @ApiModelProperty(example = "null", value = "")
  public Long getTook() {
    return took;
  }

  public void setTook(Long took) {
    this.took = took;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReponseMapping reponseMapping = (ReponseMapping) o;
    return Objects.equals(this.mapping, reponseMapping.mapping) &&
        Objects.equals(this.numFound, reponseMapping.numFound) &&
        Objects.equals(this.status, reponseMapping.status) &&
        Objects.equals(this.took, reponseMapping.took);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mapping, numFound, status, took);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReponseMapping {\n");
    
    sb.append("    mapping: ").append(toIndentedString(mapping)).append("\n");
    sb.append("    numFound: ").append(toIndentedString(numFound)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    took: ").append(toIndentedString(took)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

