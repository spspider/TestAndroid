package com.caplab.iot.capmqtt;

public class Scenario {

	private String id;
	private String name;
	private String contents;
 
	public Scenario(String id, String name, String contents) {
		this.id = id;
		this.name = name;
		this.contents = contents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
