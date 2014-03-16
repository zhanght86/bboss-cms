package com.frameworkset.platform.cms.searchmanager;

import java.util.Date;
import java.util.Timer;

import com.frameworkset.platform.config.BaseSystemInit;
import com.frameworkset.platform.config.DestroyException;
import com.frameworkset.platform.config.InitException;

public class CMSSearchInit extends BaseSystemInit {

	
	public void init() throws InitException {
	      //定义并启动定时器
	  	  Timer timer = new Timer();
	  	  CMSSearchTask task=new CMSSearchTask(this.context.getContextPath());
	  	  timer.schedule(task,new Date(),1*60*1000);
	  	  System.out.println("搜索引擎定时器启动成功！");
	}

	public void destroy() throws DestroyException {
		// TODO Auto-generated method stub
	}

}
