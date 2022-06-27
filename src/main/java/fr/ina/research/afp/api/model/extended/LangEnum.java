package fr.ina.research.afp.api.model.extended;

import com.google.gson.annotations.SerializedName;

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
