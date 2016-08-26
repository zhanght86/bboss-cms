package com.frameworkset.platform.cms.flowmanager;

import java.util.List;

public interface FlowManager extends java.io.Serializable {
	
	public List getFlowList() throws FlowManagerException;
	public List getFlowList2() throws FlowManagerException;
}
