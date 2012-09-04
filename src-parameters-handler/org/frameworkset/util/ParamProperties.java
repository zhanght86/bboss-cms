package org.frameworkset.util;

/**
 * params util 组件名称类
 * @author gao.tang
 *
 */
public class ParamProperties {
	
	/**
	 * 远程组件调用名称
	 * 默认对应的实现类：org.frameworkset.util.RpcParamsHandlerImpl
	 */
	public static final String PARAM_HANDLER_PRO="param.handler.pro";
	
	public static void mian(String[] args){
		String str = ParamProperties.PARAM_HANDLER_PRO;
	}

}
