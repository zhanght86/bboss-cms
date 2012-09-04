package com.frameworkset.platform.cms.driver.publish;



public interface PublishEngine extends java.io.Serializable {
	
	/**
	 * 发布入口
	 * @param publishObject 具体的发布对象
	 * @param callBack 发布回调接口，通过回调接口立即向请求发布的客户端反馈发布任务的执行情况，包括：
	 * 				   任务被拒绝，任务被执行，任务存入任务队列等待执行。
	 * @throws PublishException
	 */
	public void publish(PublishObject publishObject,
						PublishCallBack callBack) throws PublishException ;
	
	/**
	 * 递归发布入口，不做外部调用的接口
	 * @param publishObject
	 * @throws PublishException
	 */
	public void publish(PublishObject publishObject)  throws PublishException ;	
	
	

}
