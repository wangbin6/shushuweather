package com.example.shushuweather.models;
/**
 * 区县模型类
 * */
public class County {
	
	private int id;
	private String citynm;//所属市
	private String countynm;//区县名称
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCitynm() {
		return citynm;
	}
	public void setCitynm(String citynm) {
		this.citynm = citynm;
	}
	public String getCountynm() {
		return countynm;
	}
	public void setCountynm(String countynm) {
		this.countynm = countynm;
	}
	
	
}
