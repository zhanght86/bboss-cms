package com.frameworkset.platform.security.service.action;

import javax.jws.WebService;

import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.security.service.CommonUserManagerInf;
import com.frameworkset.platform.security.service.entity.CommonOrganization;
import com.frameworkset.platform.security.service.entity.CommonUser;
import com.frameworkset.platform.security.service.entity.Result;
@WebService(name = "commonuserService", targetNamespace = "com.frameworkset.platform.security.service.CommonuserService") 
public class CommonUserControl implements CommonUserManagerInf{
	private CommonUserManagerInf commonUserManager;
	public CommonUserControl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public @ResponseBody Result createUser(CommonUser user) {
		// TODO Auto-generated method stub
		return commonUserManager.createUser(user);
	}
	
	public @ResponseBody Result createTempUser(CommonUser user) {
		return commonUserManager.createTempUser(user);
	}

	@Override
	public @ResponseBody Result updateUser(CommonUser user) {
		return commonUserManager.updateUser(user);
	}

	@Override
	public @ResponseBody Result deleteUser(String useraccount) {
		return commonUserManager.deleteUser(useraccount);
	}

	@Override
	public @ResponseBody Result deleteUserByID(int userid) {
		return commonUserManager.deleteUserByID(userid);
	}

	@Override
	public @ResponseBody Result deleteUserByWorknumber(String worknumber) {
		return commonUserManager.deleteUserByWorknumber(worknumber);
	}

	@Override
	public @ResponseBody Result disableUser(String useraccount) {
		return commonUserManager.disableUser(useraccount);
	}

	@Override
	public@ResponseBody  Result disableUserByID(int userid) {
		return commonUserManager.disableUserByID(userid);
	}

	@Override
	public @ResponseBody Result disableUserByWorknumber(String worknumber) {
		return commonUserManager.disableUserByWorknumber(worknumber);
	}

	@Override
	public @ResponseBody Result updatePassword(int user_id, String password) {
		return commonUserManager.updatePassword(  user_id,   password);
	}

	@Override
	public @ResponseBody Result getUserByUserAccount(String user_account) {
		return commonUserManager.getUserByUserAccount(user_account);
	}

	@Override
	public @ResponseBody Result getUserById(int user_id) {
		return commonUserManager.getUserById(user_id);
	}

	@Override
	public @ResponseBody Result getUserByWorknumber(String user_worknumber) {
		return commonUserManager.getUserByWorknumber(user_worknumber);
	}

	@Override
	public @ResponseBody boolean exist(String useraccount) throws Exception {
		return commonUserManager.exist(useraccount);
	}

	@Override
	public @ResponseBody Result openUser(String useraccount) {
		return commonUserManager.openUser(useraccount);
	}

	@Override
	public @ResponseBody Result openUserByID(int userid) {
		return commonUserManager.openUserByID(userid);
	}

	@Override
	public @ResponseBody Result openUserByWorknumber(String worknumber) {
		return commonUserManager.openUserByWorknumber(worknumber);
	}

	@Override
	public Result buildUserOrgRelation(int userid, String orgid,boolean deleteotherorgjobrelation) {
		 
		return commonUserManager.buildUserOrgRelation(  userid,   orgid,deleteotherorgjobrelation);
	}

	@Override
	public Result addOrganization(CommonOrganization org) {
		// TODO Auto-generated method stub
		return commonUserManager.addOrganization(  org);
	}

	@Override
	public Result addOrganizationWithEventTrigger(CommonOrganization org, boolean triggerEvent) {
		// TODO Auto-generated method stub
		return commonUserManager.addOrganizationWithEventTrigger(  org,triggerEvent);
	}

	@Override
	public Result buildUserOrgRelationWithEventTrigger(int userid, String orgid, boolean broadcastevent,boolean deleteotherorgjobrelation) {
		// TODO Auto-generated method stub
		return commonUserManager.buildUserOrgRelationWithEventTrigger(  userid,   orgid,   broadcastevent, deleteotherorgjobrelation) ;
	}

	@Override
	public Result createOnlyUser(CommonUser user) {
		return commonUserManager.createOnlyUser(  user) ;
	}

	@Override
	public Result updateOrganization(CommonOrganization org, boolean broadcastevent) {
		// TODO Auto-generated method stub
		return commonUserManager.updateOrganization(  org,   broadcastevent);
	}

	@Override
	public Result invalidateOrganization(String orgid, boolean broadcastevent) {
		// TODO Auto-generated method stub
		return commonUserManager.invalidateOrganization(  orgid,   broadcastevent);
	}

}
