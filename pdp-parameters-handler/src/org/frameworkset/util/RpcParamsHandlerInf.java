package org.frameworkset.util;

import java.util.List;

import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProMap;

public interface RpcParamsHandlerInf {
	
	public ProMap<String, Pro> getNodeParamsWithProMapName(String paramshandler,
			String propertyMapName, String nodeId, String params_type);

	public boolean insertConParams(String paramshandler,int NODE_ID, List<Pro> params,
			String paramsType);
}
