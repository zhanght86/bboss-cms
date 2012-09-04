package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;

import com.frameworkset.common.tag.BaseCellTag;

public class SurveyListTag extends BaseCellTag {

	String style = "";
	String addr = "";
	int maxlength = 20;
	String replacemark="...";
	String target = "_blank";
	
	public int doStartTag() throws JspException {
		super.doStartTag();

		StringBuffer str = new StringBuffer();
		try {
			if (super.dataSet != null) {
				 A a = new A();
				 a.setStyle(style);
				 String txt = dataSet.getString("name");
				 if (txt.length()>maxlength)
					 txt = txt.substring(0,maxlength)+replacemark;
				 a.setTagText(txt);
				 a.setHref(addr+"?id="+dataSet.getInt("id"));
				 a.setTarget(target);
				 out.print(a.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.SKIP_BODY;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	public String getReplacemark() {
		return replacemark;
	}

	public void setReplacemark(String replacemark) {
		this.replacemark = replacemark;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
