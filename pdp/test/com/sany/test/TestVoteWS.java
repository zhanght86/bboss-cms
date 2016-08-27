/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sany.test;

import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.frameworkset.platform.cms.votemanager.ws.VoteTitle;
import com.frameworkset.platform.cms.votemanager.ws.VoteWebService;

/**
 * 
 * @author gw_yuel
 */
public class TestVoteWS {
	private String context = "http://127.0.0.1:8080/SanyPDP/";
	// hessian服务方式
	private VoteWebService hassianService = null;
	// webservice方式
	private VoteWebService cxfService = null;

	@Before
	public void init() throws Exception {
		// hessian服务方式
		HessianProxyFactory factory = new HessianProxyFactory();
		String url = context + "hessian?service=voteService";
		hassianService = (VoteWebService) factory.create(
				VoteWebService.class, url);

		// webservice方式
		String cxfUrl = context + "cxfservices/voteService";
		JaxWsProxyFactoryBean WSServiceClientFactory = new JaxWsProxyFactoryBean();
		WSServiceClientFactory.setAddress(cxfUrl);
		WSServiceClientFactory.setServiceClass(VoteWebService.class);
		cxfService = (VoteWebService) WSServiceClientFactory.create();

	}

	@Test
	public void testGetVoteCount() throws Exception {
		String count = hassianService.getVoteCount("21018438", "MobileVote");
		System.out.println(count);
	}
	
	@Test
	public void testGetVoteListByWorkNo() throws Exception {
		List<VoteTitle> list = hassianService.getVoteListByWorkNo("10005873", "MobileVote","vote1");
		System.out.println(list.size());
	}
}
