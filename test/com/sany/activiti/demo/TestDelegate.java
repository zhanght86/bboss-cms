package com.sany.activiti.demo;

import org.activiti.engine.delegate.AutoJavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;

public class TestDelegate extends AutoJavaDelegate {

	@Override
	public void autoexecute(DelegateExecution execution) throws Exception {
		execution.setVariable("testaaa", "aaaa");
		System.out.println("我来了");

	}

}
