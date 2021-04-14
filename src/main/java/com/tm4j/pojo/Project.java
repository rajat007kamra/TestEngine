package com.tm4j.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project {
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("self")
	@Expose
	private String self;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}
}
