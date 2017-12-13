package fr.ina.research.afp.api.model.extended;

import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

import fr.ina.research.afp.api.model.Parameter;

public class OrParameter extends Parameter {
	@SerializedName("or")
	private Parameter[] parameters = null;

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OrParameter {\n");
		sb.append("    parameters: ").append(toIndentedString(Arrays.toString(parameters))).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
