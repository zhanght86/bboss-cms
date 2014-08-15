package com.sany.workflow.webservice.entity;

import java.io.Serializable;

import org.frameworkset.web.multipart.MultipartFile;

public class DeploymentZipFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String deployName;// 部署名称
	private String wfAppId;// 所属应用id
	private String businessTypeId;// 业务类型id
	private String needConfig;// 是否清除待办处理人和节点参数
	private int upgradepolicy = 0;// 部署策略	
	
	private MultipartFile processZipDef; 
	
	private MultipartFile processParamFile; 
	public String getDeployName() {
		return deployName;
	}

	public void setDeployName(String deployName) {
		this.deployName = deployName;
	}

	public String getWfAppId() {
		return wfAppId;
	}

	public void setWfAppId(String wfAppId) {
		this.wfAppId = wfAppId;
	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getNeedConfig() {
		return needConfig;
	}

	public void setNeedConfig(String needConfig) {
		this.needConfig = needConfig;
	}

	public int getUpgradepolicy() {
		return upgradepolicy;
	}

	public void setUpgradepolicy(int upgradepolicy) {
		this.upgradepolicy = upgradepolicy;
	}

	
	public MultipartFile getProcessZipDef() {
		return processZipDef;
	}

	public void setProcessZipDef(MultipartFile processZipDef) {
		this.processZipDef = processZipDef;
	}

	public MultipartFile getProcessParamFile() {
		return processParamFile;
	}

	public void setProcessParamFile(MultipartFile processParamFile) {
		this.processParamFile = processParamFile;
	}

}
