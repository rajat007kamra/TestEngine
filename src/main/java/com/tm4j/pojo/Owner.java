package com.tm4j.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {
	@SerializedName("self")
	@Expose
	private String self;
	@SerializedName("accountId")
	@Expose
	private String accountId;

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
}
