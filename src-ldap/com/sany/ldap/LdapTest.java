/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.sany.ldap;

import java.util.Map;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

import com.sany.ldap.ad.AdAccountLogin;

/**
 * <p>Title: LdapTest.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-7-18
 * @author biaoping.yin
 * @version 1.0
 */
public class LdapTest {
	@Test
	public void test() throws Exception
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("property-ldap.xml");
		Authetication authentication = context.getTBeanObject("authetication", Authetication.class);
		Map<String,String> user = authentication.authenticate("yinbp","Sany_006");
		
		try {
			Map<String,String> user_ = authentication.authenticate("marc","mrc@368.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> user__ = authentication.authenticate("yinbp","Sany_006");
		
		user__ = authentication.authenticate("yinbp","Sany_006");
		
		user__ = authentication.authenticate("yinbp","Sany_006");
		System.out.println();
		
	}
	@Test
	public void test1()
	{
		AdAccountLogin login = new AdAccountLogin();
		login.validateUser("yinbp", "Sany_006", null);
		login.validateUser("yinbp", "Sany_006", null);
		login.validateUser("marc", "mrc@368.com", null);
		System.out.println();
	}
	
	@Test
	public void decrpt() throws Exception
	{
		com.frameworkset.common.poolman.security.DESCipher s = new com.frameworkset.common.poolman.security.DESCipher();
		 
		System.out.println(s.decrypt("b23b9112ad5b60215fd1d24a8e5ac502"));
	}

}
