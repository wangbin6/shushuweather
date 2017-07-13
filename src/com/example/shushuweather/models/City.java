package com.example.shushuweather.models;
/*
 * 城市模型类
 * 
 * @author WangBin
 * @time 2017-07-13 14:00
 * */
public class City {
	
	private int id;//城市列表表ID
	private String citynm;//城市中文名称
	private String cityno;//城市名称拼音
	private String cityid;//城市编号
	private String province;//省
	private String municipality;//市
	private String county;//区、县
	private String weaid;//天气ID
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
	public String getCityno() {
		return cityno;
	}
	public void setCityno(String cityno) {
		this.cityno = cityno;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getMunicipality() {
		return municipality;
	}
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getWeaid() {
		return weaid;
	}
	public void setWeaid(String weaid) {
		this.weaid = weaid;
	}
}
