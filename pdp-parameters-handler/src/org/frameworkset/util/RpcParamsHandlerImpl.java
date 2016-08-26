package org.frameworkset.util;

import java.util.List;

import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProMap;

public class RpcParamsHandlerImpl implements RpcParamsHandlerInf {

	
	public ProMap<String, Pro> getNodeParamsWithProMapName(
			String paramshandler, String propertyMapName, String nodeId,
			String paramsType) {
		ParamsHandler handler = ParamsHandler.getParamsHandler(paramshandler);
		return handler.getNodeParamsWithProMapName(propertyMapName, nodeId,
				paramsType);
	}

	
	public boolean insertConParams(String paramshandler, int NODE_ID,
			List<Pro> params, String paramsType) {
		ParamsHandler handler = ParamsHandler.getParamsHandler(paramshandler);
		return handler.insertConParams(NODE_ID, params, paramsType);
	}

}
