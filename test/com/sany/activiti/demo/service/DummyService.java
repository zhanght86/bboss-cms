package com.sany.activiti.demo.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DummyService implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("sap冲销完成");
//		throw new Exception("sap冲销失败");
		
	}

}
