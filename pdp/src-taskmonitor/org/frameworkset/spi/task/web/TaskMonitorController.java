package org.frameworkset.spi.task.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.spi.task.entity.TaskEngine;
import org.frameworkset.task.TaskService;
import org.frameworkset.util.annotations.ResponseBody;



public class TaskMonitorController {
	public @ResponseBody List<TaskEngine> getTaskEngines()
	{
		Map<String ,TaskService> taskservices = TaskService.getTaskServices(); 
		List<TaskEngine> engines = new ArrayList<TaskEngine>();
		if(taskservices == null || taskservices.size() == 0)
			return engines;
		Set<Map.Entry<String ,TaskService>> entrys = taskservices.entrySet();
		
		for(Map.Entry<String ,TaskService> task:entrys)
		{
			TaskEngine engine = new TaskEngine();
			engine.setTaskEngineID(task.getKey());
			engine.setTaskEngineName(task.getValue().getSchedulerName());
		}
		return engines;
		
		
	}

}
