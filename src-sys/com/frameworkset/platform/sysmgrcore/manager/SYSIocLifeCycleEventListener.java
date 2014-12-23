package com.frameworkset.platform.sysmgrcore.manager;

import org.frameworkset.remote.EventUtils;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.event.IocLifeCycleEventListener;
import org.frameworkset.task.TaskService;

import com.frameworkset.platform.sysmgrcore.purviewmanager.CacheManager;

public class SYSIocLifeCycleEventListener implements IocLifeCycleEventListener {

	public SYSIocLifeCycleEventListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void afterstart(BaseApplicationContext arg0) {
		// 初始化任务管理服务
		TaskService service = TaskService.getTaskService();
		service.startService();
		CacheManager.registRefreshEventListener();
		//启动分布式事件服务
		EventUtils.init();  

	}

	@Override
	public void beforestart() {
		

	}

}
