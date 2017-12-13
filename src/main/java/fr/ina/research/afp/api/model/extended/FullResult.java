package fr.ina.research.afp.api.model.extended;

import fr.ina.research.afp.api.model.Result;

public class FullResult {
	private Result internal;
	private long retrievedDate;

	public FullResult() {
		super();
	}

	public FullResult(Result internal, long retrievedDate) {
		this();
		this.internal = internal;
		this.retrievedDate = retrievedDate;
	}

	public Result getInternal() {
		return internal;
	}

	public long getRetrievedDate() {
		return retrievedDate;
	}

	public void setInternal(Result internal) {
		this.internal = internal;
	}

	public void setRetrievedDate(long retrievedDate) {
		this.retrievedDate = retrievedDate;
	}
}
