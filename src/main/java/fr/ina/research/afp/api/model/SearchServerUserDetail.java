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


package fr.ina.research.afp.api.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;

import fr.ina.research.afp.api.model.Parameters;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;


/**
 * SearchServerUserDetail
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2022-06-23T14:46:12.287+02:00")
public class SearchServerUserDetail   {
  @SerializedName("activationKey")
  private String activationKey = null;

  @SerializedName("additionalProperties")
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @SerializedName("authorities")
  private List<String> authorities = new ArrayList<String>();

  @SerializedName("clientId")
  private List<String> clientId = new ArrayList<String>();

  @SerializedName("created")
  private DateTime created = null;

  @SerializedName("email")
  private String email = null;

  @SerializedName("enabled")
  private Boolean enabled = null;

  @SerializedName("expires")
  private DateTime expires = null;

  @SerializedName("filters")
  private Parameters filters = null;

  @SerializedName("password")
  private String password = null;

  @SerializedName("resetPasswordKey")
  private String resetPasswordKey = null;

  @SerializedName("updated")
  private DateTime updated = null;

  @SerializedName("username")
  private String username = null;

  public SearchServerUserDetail activationKey(String activationKey) {
    this.activationKey = activationKey;
    return this;
  }

   /**
   * Get activationKey
   * @return activationKey
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public SearchServerUserDetail additionalProperties(Map<String, Object> additionalProperties) {
    this.additionalProperties = additionalProperties;
    return this;
  }

  public SearchServerUserDetail putAdditionalPropertiesItem(String key, Object additionalPropertiesItem) {
    this.additionalProperties.put(key, additionalPropertiesItem);
    return this;
  }

   /**
   * Get additionalProperties
   * @return additionalProperties
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(Map<String, Object> additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

  public SearchServerUserDetail authorities(List<String> authorities) {
    this.authorities = authorities;
    return this;
  }

  public SearchServerUserDetail addAuthoritiesItem(String authoritiesItem) {
    this.authorities.add(authoritiesItem);
    return this;
  }

   /**
   * Get authorities
   * @return authorities
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<String> authorities) {
    this.authorities = authorities;
  }

  public SearchServerUserDetail clientId(List<String> clientId) {
    this.clientId = clientId;
    return this;
  }

  public SearchServerUserDetail addClientIdItem(String clientIdItem) {
    this.clientId.add(clientIdItem);
    return this;
  }

   /**
   * Get clientId
   * @return clientId
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getClientId() {
    return clientId;
  }

  public void setClientId(List<String> clientId) {
    this.clientId = clientId;
  }

  public SearchServerUserDetail created(DateTime created) {
    this.created = created;
    return this;
  }

   /**
   * Get created
   * @return created
  **/
  @ApiModelProperty(example = "null", value = "")
  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public SearchServerUserDetail email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SearchServerUserDetail enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Get enabled
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public SearchServerUserDetail expires(DateTime expires) {
    this.expires = expires;
    return this;
  }

   /**
   * Get expires
   * @return expires
  **/
  @ApiModelProperty(example = "null", value = "")
  public DateTime getExpires() {
    return expires;
  }

  public void setExpires(DateTime expires) {
    this.expires = expires;
  }

  public SearchServerUserDetail filters(Parameters filters) {
    this.filters = filters;
    return this;
  }

   /**
   * Get filters
   * @return filters
  **/
  @ApiModelProperty(example = "null", value = "")
  public Parameters getFilters() {
    return filters;
  }

  public void setFilters(Parameters filters) {
    this.filters = filters;
  }

  public SearchServerUserDetail password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public SearchServerUserDetail resetPasswordKey(String resetPasswordKey) {
    this.resetPasswordKey = resetPasswordKey;
    return this;
  }

   /**
   * Get resetPasswordKey
   * @return resetPasswordKey
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getResetPasswordKey() {
    return resetPasswordKey;
  }

  public void setResetPasswordKey(String resetPasswordKey) {
    this.resetPasswordKey = resetPasswordKey;
  }

  public SearchServerUserDetail updated(DateTime updated) {
    this.updated = updated;
    return this;
  }

   /**
   * Get updated
   * @return updated
  **/
  @ApiModelProperty(example = "null", value = "")
  public DateTime getUpdated() {
    return updated;
  }

  public void setUpdated(DateTime updated) {
    this.updated = updated;
  }

  public SearchServerUserDetail username(String username) {
    this.username = username;
    return this;
  }

   /**
   * Get username
   * @return username
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchServerUserDetail searchServerUserDetail = (SearchServerUserDetail) o;
    return Objects.equals(this.activationKey, searchServerUserDetail.activationKey) &&
        Objects.equals(this.additionalProperties, searchServerUserDetail.additionalProperties) &&
        Objects.equals(this.authorities, searchServerUserDetail.authorities) &&
        Objects.equals(this.clientId, searchServerUserDetail.clientId) &&
        Objects.equals(this.created, searchServerUserDetail.created) &&
        Objects.equals(this.email, searchServerUserDetail.email) &&
        Objects.equals(this.enabled, searchServerUserDetail.enabled) &&
        Objects.equals(this.expires, searchServerUserDetail.expires) &&
        Objects.equals(this.filters, searchServerUserDetail.filters) &&
        Objects.equals(this.password, searchServerUserDetail.password) &&
        Objects.equals(this.resetPasswordKey, searchServerUserDetail.resetPasswordKey) &&
        Objects.equals(this.updated, searchServerUserDetail.updated) &&
        Objects.equals(this.username, searchServerUserDetail.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activationKey, additionalProperties, authorities, clientId, created, email, enabled, expires, filters, password, resetPasswordKey, updated, username);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchServerUserDetail {\n");
    
    sb.append("    activationKey: ").append(toIndentedString(activationKey)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("    authorities: ").append(toIndentedString(authorities)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    expires: ").append(toIndentedString(expires)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    resetPasswordKey: ").append(toIndentedString(resetPasswordKey)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

