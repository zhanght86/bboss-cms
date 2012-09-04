/* 功能: 用于递归查询子角色ID
 * @author wanghh
*/



package com.frameworkset.platform.ldap;

import java.util.ArrayList;

public class LdapBean implements java.io.Serializable {

	private LdapManager ldapMan = new LdapManager();

	public ArrayList childIDList = new ArrayList(); //用于存储父角色id下面的所有子角色id

	public LdapBean(){
		childIDList = new ArrayList();
	}



    public void searchChildID(String strParentID) throws Exception {

    	childIDList.add(strParentID);

    	ArrayList childList = ldapMan.searchAuthGroupByFilter("rolesireid="+strParentID);
    	for(int i=0; i<childList.size(); i++){
    		String childID = ((AuthRole)childList.get(i)).getRoleid();
    		searchChildID(childID);
    	}


    }


}
