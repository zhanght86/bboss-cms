//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.List;

/** 
 * MyEclipse Struts
 * Creation date: 03-10-2006
 * 
 * XDoclet definition:
 * @struts.form name="jobManagerForm"
 */
public class JobManagerForm  implements Serializable{

	private String orgId = null; //机构ID
	private List jobList = null; //岗位列表
	
	public String getOrgId(){
		return orgId;
	}
	public void setOrgId(String orgId){
		this.orgId=orgId;
	}
	public List getJobList(){
		return jobList;
	}
	public void setJobList(List jobList) {
		this.jobList=jobList;
	}
	
	 

}

