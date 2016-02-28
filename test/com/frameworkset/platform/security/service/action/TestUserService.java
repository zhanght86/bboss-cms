package com.frameworkset.platform.security.service.action;

import java.util.List;

import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.frameworkset.platform.security.service.CommonUserManagerInf;
import com.frameworkset.platform.security.service.entity.CommonOrganization;
import com.frameworkset.platform.security.service.entity.Result;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.sany.masterdata.hr.sync.OrgTreeLevel;

public class TestUserService {

	public TestUserService() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void buildOrgTreeLevel()
	{
		 OrgTreeLevel.run();
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
	@Test
	public void testCreateOrg() throws Exception
	{
		HessianProxyFactory factory = new HessianProxyFactory();
		//String url = "http://10.25.192.142:8081/context/hessian?service=tokenService";
//		String url = "http://localhost/hessian/commonuserService";
		String url = "http://pdp.bbossgroups.com/hessian?service=commonuserService";
		CommonUserManagerInf tokenService = (CommonUserManagerInf) factory.create(CommonUserManagerInf.class, url);
		CommonOrganization org = new CommonOrganization();
		/**
		 * 常用字段：
		 * 
		 * orgId,
		 * orgName,
		 * parentId,
		 * code,
		 
		 * orgnumber,
		 * orgdesc,
		 * remark5, 显示名称
		 * orgTreeLevel,部门层级，自动运算
		 * orgleader 部门主管
		 * @author yinbp
		 *
		 */
		org.setOrgName("测试机构");
		org.setCode("code");
		org.setOrgnumber("orgnumber");
		org.setParentId("0");
		org.setOrgdesc("测试机构");
		org.setRemark5("测试机构");
		org.setOrgleader("10006673");
		 
		Result result = tokenService.addOrganization(org);
		System.out.println();
	}
	
	@Test
	public void testBuildUserOrgRelationWithEventTrigger() throws Exception
	{
		int userId=18;
		String org = "d926e547-6e6c-4792-94a1-54e1414f92b0";
		HessianProxyFactory factory = new HessianProxyFactory();
		//String url = "http://10.25.192.142:8081/context/hessian?service=tokenService";
//		String url = "http://localhost/hessian/commonuserService";
		String url = "http://pdp.bbossgroups.com/hessian?service=commonuserService";
		CommonUserManagerInf tokenService = (CommonUserManagerInf) factory.create(CommonUserManagerInf.class, url);
	 
		 
		Result result = tokenService.buildUserOrgRelationWithEventTrigger(userId, org, true,true);
		System.out.println();
	}
	
	
	@Test
	public void testDeleteOrg() throws Exception
	{
	 
		String org = "50060786";
		HessianProxyFactory factory = new HessianProxyFactory();
		//String url = "http://10.25.192.142:8081/context/hessian?service=tokenService";
//		String url = "http://localhost/hessian/commonuserService";
		String url = "http://pdp.bbossgroups.com/hessian?service=commonuserService";
		CommonUserManagerInf tokenService = (CommonUserManagerInf) factory.create(CommonUserManagerInf.class, url);
	 
		 
		Result result = tokenService.deleteOrganization(org, true);
		System.out.println();
	}
	
	@Test
	public void testgetFathers() throws Exception
	{
	 
		String orgid = "50459816";
		Organization org = OrgCacheManager.getInstance().getFatherOrganization(orgid);
		List<Organization> orgs = OrgCacheManager.getInstance().getFatherOrganizations(orgid);
		orgs = OrgCacheManager.getInstance().getFatherOrganizations(orgid);
		System.out.println();
	}
	
	@Test
	public void testupdateOrg() throws Exception
	{
	 
		CommonOrganization org = new CommonOrganization();
		/**
		 * 常用字段：
		 * 
		 * orgId,
		 * orgName,
		 * parentId,
		 * code,
		 
		 * orgnumber,
		 * orgdesc,
		 * remark5, 显示名称
		 * orgTreeLevel,部门层级，自动运算
		 * orgleader 部门主管
		 * @author yinbp
		 *
		 */
		org.setOrgName("测试机构");
		org.setCode("code");
		org.setOrgnumber("orgnumber");
		
		org.setOrgdesc("测试机构");
		org.setRemark5("测试机构");
		org.setOrgleader("10006673");
		org.setOrgId("10");
		HessianProxyFactory factory = new HessianProxyFactory();
		//String url = "http://10.25.192.142:8081/context/hessian?service=tokenService";
//		String url = "http://localhost/hessian/commonuserService";
		String url = "http://pdp.bbossgroups.com/hessian?service=commonuserService";
		CommonUserManagerInf tokenService = (CommonUserManagerInf) factory.create(CommonUserManagerInf.class, url);
	 
		 
		Result result = tokenService.updateOrganization(org,true);
		System.out.println();
	}
	
	
	
}
