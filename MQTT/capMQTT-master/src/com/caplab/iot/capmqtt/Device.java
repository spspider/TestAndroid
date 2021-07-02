package com.caplab.iot.capmqtt;

public class Device {

	private String id;
	private String value;
	private String function;
 
	public Device(String id, String value, String function) {
		this.id = id;
		this.value = value;
		this.function = function;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}
	

}
