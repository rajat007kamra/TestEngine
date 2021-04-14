package com.tm4j.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestScript {
	@SerializedName("self")
	@Expose
	private String self;

	public String getSelf() {
	return self;
	}

	public void setSelf(String self) {
	this.self = self;
	}
}
