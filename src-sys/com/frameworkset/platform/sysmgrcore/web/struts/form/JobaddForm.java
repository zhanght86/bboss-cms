//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 03-09-2006
 * 
 * XDoclet definition:
 * 
 * @struts.form name="jobaddForm"
 */
public class JobaddForm  implements Serializable{
	private String jobname;

	private String jobdesc;

	/** 岗位职责 */
	private String jobFunction;

	/** 岗位编制人数 */
	private String jobAmount;

	/** 岗位编号 */
	private String jobNumber;

	/** 任职条件 */
	private String jobCondition;
	
	/** 岗位等级 */
	private String jobRank;
	
	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobdesc() {
		return jobdesc;
	}

	public void setJobdesc(String jobdesc) {
		this.jobdesc = jobdesc;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method reset
	 * 
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		// TODO Auto-generated method stub
	}

	public String getJobAmount() {
		return jobAmount;
	}

	public void setJobAmount(String jobAmount) {
		this.jobAmount = jobAmount;
	}

	public String getJobCondition() {
		return jobCondition;
	}

	public void setJobCondition(String jobCondition) {
		this.jobCondition = jobCondition;
	}

	public String getJobFunction() {
		return jobFunction;
	}

	public void setJobFunction(String jobFunction) {
		this.jobFunction = jobFunction;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getJobRank() {
		return jobRank;
	}

	public void setJobRank(String jobRank) {
		this.jobRank = jobRank;
	}

}
