package fr.ina.research.afp.api.model.extended;

import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

import fr.ina.research.afp.api.model.Parameter;

public class FullParameter extends Parameter {
	@SerializedName("name")
	private String name = null;

	@SerializedName("and")
	private String[] and = null;

	@SerializedName("in")
	private String[] in = null;

	@SerializedName("or")
	private String[] or = null;

	public String[] getAnd() {
		return and;
	}

	public String[] getIn() {
		return in;
	}

	public String getName() {
		return name;
	}

	public String[] getOr() {
		return or;
	}

	public void setAnd(String[] and) {
		this.and = and;
	}

	public void setIn(String[] in) {
		this.in = in;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOr(String[] or) {
		this.or = or;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class FullParameter {\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    and: ").append(toIndentedString(Arrays.toString(and))).append("\n");
		sb.append("    or: ").append(toIndentedString(Arrays.toString(or))).append("\n");
		sb.append("    in: ").append(toIndentedString(Arrays.toString(in))).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
