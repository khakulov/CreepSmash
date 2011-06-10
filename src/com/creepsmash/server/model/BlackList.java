package com.creepsmash.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BlackList")
public class BlackList {
	@Id
	private String data;

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
	
}
