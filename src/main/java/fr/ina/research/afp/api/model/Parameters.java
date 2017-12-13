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

import fr.ina.research.afp.api.model.Aggregat;
import fr.ina.research.afp.api.model.DateRange;
import fr.ina.research.afp.api.model.Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The global query parameter, like &#x60;&#x60;&#x60;json {  \&quot;tz\&quot;: \&quot;Europe/Paris\&quot;,  \&quot;dateRange\&quot;: {   \&quot;from\&quot;: \&quot;now-1M\&quot;,   \&quot;to\&quot;: \&quot;now\&quot;  },  \&quot;sortField\&quot;: \&quot;published\&quot;,  \&quot;sortOrder\&quot;: \&quot;desc\&quot;,  \&quot;dateGap\&quot;: \&quot;day\&quot;,  \&quot;lang\&quot;: \&quot;fr\&quot;,  \&quot;wantCluster\&quot;: true,  \&quot;maxRows\&quot;: 50,  \&quot;fields\&quot;: [  ],  \&quot;wantedFacets\&quot;: {   \&quot;product\&quot;: {    \&quot;minSize\&quot;: 1,    \&quot;maxSize\&quot;: 5   },   \&quot;iptc\&quot;: {    \&quot;minSize\&quot;: 1,    \&quot;maxSize\&quot;: 100   },   \&quot;country\&quot;: {    \&quot;minSize\&quot;: 1,    \&quot;maxSize\&quot;: 100   },   \&quot;city\&quot;: {    \&quot;minSize\&quot;: 1,    \&quot;maxSize\&quot;: 100   },   \&quot;slug\&quot;: {    \&quot;minSize\&quot;: 1,    \&quot;maxSize\&quot;: 3   },   \&quot;urgency\&quot;: {    \&quot;minSize\&quot;: 1,    \&quot;maxSize\&quot;: 100   }  },  \&quot;query\&quot;: {   \&quot;and\&quot;: [    {     \&quot;name\&quot;: \&quot;product\&quot;,     \&quot;in\&quot;: [      \&quot;multimedia\&quot;,      \&quot;news\&quot;     ]    },    {     \&quot;name\&quot;: \&quot;country\&quot;,     \&quot;in\&quot;: [      \&quot;FRA\&quot;,      \&quot;DEU\&quot;     ]    }   ]  } } &#x60;&#x60;&#x60; 
 */
@ApiModel(description = "The global query parameter, like ```json {  \"tz\": \"Europe/Paris\",  \"dateRange\": {   \"from\": \"now-1M\",   \"to\": \"now\"  },  \"sortField\": \"published\",  \"sortOrder\": \"desc\",  \"dateGap\": \"day\",  \"lang\": \"fr\",  \"wantCluster\": true,  \"maxRows\": 50,  \"fields\": [  ],  \"wantedFacets\": {   \"product\": {    \"minSize\": 1,    \"maxSize\": 5   },   \"iptc\": {    \"minSize\": 1,    \"maxSize\": 100   },   \"country\": {    \"minSize\": 1,    \"maxSize\": 100   },   \"city\": {    \"minSize\": 1,    \"maxSize\": 100   },   \"slug\": {    \"minSize\": 1,    \"maxSize\": 3   },   \"urgency\": {    \"minSize\": 1,    \"maxSize\": 100   }  },  \"query\": {   \"and\": [    {     \"name\": \"product\",     \"in\": [      \"multimedia\",      \"news\"     ]    },    {     \"name\": \"country\",     \"in\": [      \"FRA\",      \"DEU\"     ]    }   ]  } } ``` ")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-19T17:17:54.807+02:00")
public class Parameters   {
  @SerializedName("dateGap")
  private String dateGap = null;

  @SerializedName("dateRange")
  private DateRange dateRange = null;

  @SerializedName("fields")
  private List<String> fields = new ArrayList<String>();

  /**
   * Gets or Sets lang
   */
  public enum LangEnum {
    @SerializedName("ar")
    AR("ar"),
    
    @SerializedName("de")
    DE("de"),
    
    @SerializedName("en")
    EN("en"),
    
    @SerializedName("es")
    ES("es"),
    
    @SerializedName("fr")
    FR("fr"),
    
    @SerializedName("pt")
    PT("pt");

    private String value;

    LangEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("lang")
  private LangEnum lang = null;

  @SerializedName("maxRows")
  private Integer maxRows = null;

  @SerializedName("query")
  private Parameter query = null;

  @SerializedName("sortField")
  private String sortField = null;

  @SerializedName("sortOrder")
  private String sortOrder = null;

  @SerializedName("startAt")
  private Integer startAt = null;

  @SerializedName("tz")
  private String tz = null;

  @SerializedName("wantCluster")
  private Boolean wantCluster = null;

  @SerializedName("wantedFacets")
  private Map<String, Aggregat> wantedFacets = new HashMap<String, Aggregat>();

  public Parameters dateGap(String dateGap) {
    this.dateGap = dateGap;
    return this;
  }

   /**
   * Get dateGap
   * @return dateGap
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getDateGap() {
    return dateGap;
  }

  public void setDateGap(String dateGap) {
    this.dateGap = dateGap;
  }

  public Parameters dateRange(DateRange dateRange) {
    this.dateRange = dateRange;
    return this;
  }

   /**
   * Get dateRange
   * @return dateRange
  **/
  @ApiModelProperty(example = "null", value = "")
  public DateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(DateRange dateRange) {
    this.dateRange = dateRange;
  }

  public Parameters fields(List<String> fields) {
    this.fields = fields;
    return this;
  }

  public Parameters addFieldsItem(String fieldsItem) {
    this.fields.add(fieldsItem);
    return this;
  }

   /**
   * Get fields
   * @return fields
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getFields() {
    return fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }

  public Parameters lang(LangEnum lang) {
    this.lang = lang;
    return this;
  }

   /**
   * Get lang
   * @return lang
  **/
  @ApiModelProperty(example = "null", value = "")
  public LangEnum getLang() {
    return lang;
  }

  public void setLang(LangEnum lang) {
    this.lang = lang;
  }

  public Parameters maxRows(Integer maxRows) {
    this.maxRows = maxRows;
    return this;
  }

   /**
   * Get maxRows
   * @return maxRows
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getMaxRows() {
    return maxRows;
  }

  public void setMaxRows(Integer maxRows) {
    this.maxRows = maxRows;
  }

  public Parameters query(Parameter query) {
    this.query = query;
    return this;
  }

   /**
   * Get query
   * @return query
  **/
  @ApiModelProperty(example = "null", value = "")
  public Parameter getQuery() {
    return query;
  }

  public void setQuery(Parameter query) {
    this.query = query;
  }

  public Parameters sortField(String sortField) {
    this.sortField = sortField;
    return this;
  }

   /**
   * Get sortField
   * @return sortField
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getSortField() {
    return sortField;
  }

  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  public Parameters sortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
    return this;
  }

   /**
   * Get sortOrder
   * @return sortOrder
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  public Parameters startAt(Integer startAt) {
    this.startAt = startAt;
    return this;
  }

   /**
   * Get startAt
   * @return startAt
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getStartAt() {
    return startAt;
  }

  public void setStartAt(Integer startAt) {
    this.startAt = startAt;
  }

  public Parameters tz(String tz) {
    this.tz = tz;
    return this;
  }

   /**
   * Get tz
   * @return tz
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getTz() {
    return tz;
  }

  public void setTz(String tz) {
    this.tz = tz;
  }

  public Parameters wantCluster(Boolean wantCluster) {
    this.wantCluster = wantCluster;
    return this;
  }

   /**
   * Get wantCluster
   * @return wantCluster
  **/
  @ApiModelProperty(example = "false", value = "")
  public Boolean getWantCluster() {
    return wantCluster;
  }

  public void setWantCluster(Boolean wantCluster) {
    this.wantCluster = wantCluster;
  }

  public Parameters wantedFacets(Map<String, Aggregat> wantedFacets) {
    this.wantedFacets = wantedFacets;
    return this;
  }

  public Parameters putWantedFacetsItem(String key, Aggregat wantedFacetsItem) {
    this.wantedFacets.put(key, wantedFacetsItem);
    return this;
  }

   /**
   * Get wantedFacets
   * @return wantedFacets
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map<String, Aggregat> getWantedFacets() {
    return wantedFacets;
  }

  public void setWantedFacets(Map<String, Aggregat> wantedFacets) {
    this.wantedFacets = wantedFacets;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Parameters parameters = (Parameters) o;
    return Objects.equals(this.dateGap, parameters.dateGap) &&
        Objects.equals(this.dateRange, parameters.dateRange) &&
        Objects.equals(this.fields, parameters.fields) &&
        Objects.equals(this.lang, parameters.lang) &&
        Objects.equals(this.maxRows, parameters.maxRows) &&
        Objects.equals(this.query, parameters.query) &&
        Objects.equals(this.sortField, parameters.sortField) &&
        Objects.equals(this.sortOrder, parameters.sortOrder) &&
        Objects.equals(this.startAt, parameters.startAt) &&
        Objects.equals(this.tz, parameters.tz) &&
        Objects.equals(this.wantCluster, parameters.wantCluster) &&
        Objects.equals(this.wantedFacets, parameters.wantedFacets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateGap, dateRange, fields, lang, maxRows, query, sortField, sortOrder, startAt, tz, wantCluster, wantedFacets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Parameters {\n");
    
    sb.append("    dateGap: ").append(toIndentedString(dateGap)).append("\n");
    sb.append("    dateRange: ").append(toIndentedString(dateRange)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
    sb.append("    lang: ").append(toIndentedString(lang)).append("\n");
    sb.append("    maxRows: ").append(toIndentedString(maxRows)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    sortField: ").append(toIndentedString(sortField)).append("\n");
    sb.append("    sortOrder: ").append(toIndentedString(sortOrder)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    tz: ").append(toIndentedString(tz)).append("\n");
    sb.append("    wantCluster: ").append(toIndentedString(wantCluster)).append("\n");
    sb.append("    wantedFacets: ").append(toIndentedString(wantedFacets)).append("\n");
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

