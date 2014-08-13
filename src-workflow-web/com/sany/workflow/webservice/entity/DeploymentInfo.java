package com.sany.workflow.webservice.entity;

import java.io.FileInputStream;
import java.io.Serializable;

/**
 * 流程部署信息
 * 
 * @author yinbp
 * @since 2012-3-22 下午7:42:10
 */
public class DeploymentInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String deployName;// 部署名称
	private String wfAppId;// 所属应用id
	private String businessTypeId;// 业务类型id
	private String needConfig;// 是否清除待办处理人和节点参数
	private int upgradepolicy = 0;// 部署策略
	private byte[] processDefFile;// 部署资源
	private byte[] paramFile;// 参数资源

	public byte[] getParamFile() {
		return paramFile;
	}

	public void setParamFile(byte[] paramFile) {
		this.paramFile = paramFile;
	}

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

	public byte[] getProcessDefFile() {
		return processDefFile;
	}

	public void setProcessDefFile(byte[] processDefFile) {
		this.processDefFile = processDefFile;
	}

}
