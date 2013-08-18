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

import java.util.List;
import java.util.Map;

import javax.naming.directory.DirContext;

/**
 * <p>Title: Authetication.java</p>
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
public class Authetication {
	private LdapManagerFactory ldapManagerFactory;
	private String searchDc="DC=sany,DC=com,DC=cn";	
	private String searchObjectCategory="CN=Person,CN=Schema,CN=Configuration,DC=sany,DC=com,DC=cn";
	public Map<String,String> authenticate(String user,String password) throws Exception
	{
		List<Map<String,String>> rs = ldapManagerFactory.getAttibutes(searchObjectCategory,this.searchDc,"cn", user);
		Map<String,String> man = rs == null?null:rs.get(0);
		String name = man.get("distinguishedName");
		DirContext context = ldapManagerFactory.authenticate(name, password);
		if(context == null)
			throw new Exception("authorfailed");
		else
		{
			context.close();
		}
		return rs == null?null:rs.get(0);
	}
	public LdapManagerFactory getLdapManagerFactory() {
		return ldapManagerFactory;
	}
	public void setLdapManagerFactory(LdapManagerFactory ldapManagerFactory) {
		this.ldapManagerFactory = ldapManagerFactory;
	}
	public String getSearchDc() {
		return searchDc;
	}
	public void setSearchDc(String searchDc) {
		this.searchDc = searchDc;
	}
	public String getSearchObjectCategory() {
		return searchObjectCategory;
	}
	public void setSearchObjectCategory(String searchObjectCategory) {
		this.searchObjectCategory = searchObjectCategory;
	}

}
