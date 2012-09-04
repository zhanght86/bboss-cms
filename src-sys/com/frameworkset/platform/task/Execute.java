package com.frameworkset.platform.task;

import java.io.Serializable;
import java.util.Map;

/**
 * 计划执行体
 * @author biaoping.yin
 * @date create on 2006-7-1
 * @version 1.0
 */
public interface Execute extends Serializable{
	public void execute(Map parameters);
}
