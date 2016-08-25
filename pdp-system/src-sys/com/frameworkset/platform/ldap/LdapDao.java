/**
 *
 */
package com.frameworkset.platform.ldap;

import java.util.ArrayList;

/**
 * @author wanghh
 *
 */
public interface LdapDao extends java.io.Serializable {

	public ArrayList searchUserByFilter(String filter) throws Exception;

	public ArrayList searchPerson(String id) throws Exception;

	public ArrayList searchPersonByFilter(String filter) throws Exception;

	public void deletePerson(String id) throws Exception;

	public ArrayList searchAuthGroupByFilter(String filter) throws Exception;

	public void insertPerson(AuthPerson authPerson) throws Exception;

	public void insertPerson(AuthPerson authPerson,AuthUser authUser) throws Exception;

	public ArrayList searchChildAuthGroup(String strAuthGroupID) throws Exception;

	public ArrayList searchAllAuthGroup() throws Exception;

	public ArrayList searchAuthGroup(String id) throws Exception;

	public void insertUser(AuthUser authUser) throws Exception;

	public void updateUser(AuthUser authUser) throws Exception;

	public void updatePerson(AuthPerson authPerson) throws Exception;

	public ArrayList searchUser(String id) throws Exception;

	public void updateUser(String id,ArrayList list) throws Exception ;

	public void updateAuthGroup(AuthRole authRole) throws Exception;

	 public void insertAuthGroup(AuthRole authRole) throws Exception;

	 public void deleteAuthGroup(String id) throws Exception;

	 public void updateAuthGroup(String id,ArrayList list) throws Exception;

	 public ArrayList searchAllPerson() throws Exception;

	 public ArrayList searchAllUser() throws Exception;
}
