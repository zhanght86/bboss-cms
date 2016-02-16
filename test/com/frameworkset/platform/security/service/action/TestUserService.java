package com.frameworkset.platform.security.service.action;

import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.frameworkset.platform.security.service.CommonUserManagerInf;
import com.frameworkset.platform.security.service.entity.Result;

public class TestUserService {

	public TestUserService() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void testGetUser() throws Exception
	{
		HessianProxyFactory factory = new HessianProxyFactory();
		//String url = "http://10.25.192.142:8081/context/hessian?service=tokenService";
		String url = "http://localhost/hessian/commonuserService";
		CommonUserManagerInf tokenService = (CommonUserManagerInf) factory.create(CommonUserManagerInf.class, url);
		Result result = tokenService.getUserByUserAccount("yinbp");
		System.out.println();
	}

}
