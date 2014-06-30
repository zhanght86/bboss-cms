package com.frameworkset.platform.holiday.area.bean;

public class Day {
    private String id;//对应页面上面的编码，即某月第几周的星期几
    private String value;//当月的第几天
    private String type;//当天的类型，工作日，休息日
    private String dayOfMonth;
	public String getId() {
		return id;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
