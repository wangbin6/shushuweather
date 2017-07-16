package com.example.shushuweather.models;
/*
 * 市模型类
 * 
 * @author WangBin
 * @time 2017-07-13 14:00
 * */
public class City {
	
	private int id;//城市列表表ID
	private String citynm;//城市中文名称
	private String provincenm;//省

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
	public String getProvincenm() {
		return provincenm;
	}
	public void setProvincenm(String provincenm) {
		this.provincenm = provincenm;
	}
	
}
