package com.frameworkset.platform.sysmgrcore.purviewmanager.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.ParamException;
import org.frameworkset.util.ParamsHandler;
import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.util.ParamsHandler.Params;


public class UserOrgParamManager {
	private String orgparamHandler = "sys.org.paramshandler";
	private String userparamHandler = "sys.user.paramshandler";
	private String orgparamType = "org";
	private String userparamType = "user";
	/**
	 * 新增用户、用户调入、调出时将用户与组织机构的关系固化
	 * @param userid
	 */
	public void fixuserorg(String userid,String orgid)
	{
		ParamsHandler paramHandler = ParamsHandler.getParamsHandler(userparamHandler);
		if(paramHandler == null)
			return ;
//		List<Param> params = new ArrayList<Param>();
//		Param param = new Param(userparamType, userid, "fixedorg",
//				orgid, 0) ;
//		params.add(param);
//		Params paramsList = new Params();
//		paramsList.setParams(params);
//		ParamsHandler.getParamsHandler(userparamHandler).saveParams(paramsList);
		
		Params paramsList = paramHandler._getParams(userid, userparamType);
		if(paramsList == null)
		{
			paramsList = new Params();
			List<Param> params = new ArrayList<Param>();
			Param param = new Param(userparamType, userid, "fixedorg",
					orgid, 0) ;
			params.add(param);
			paramsList.setParams(params);
		}
		else
		{
			Param param = new Param(userparamType, userid, "fixedorg",
					orgid, 0) ;
			paramsList.updateParam(param);
		}
		paramHandler.saveParams(paramsList);
	}
	
	/**
	 * 新增机构、机构转移时将新增或者转移的组织机构与父机构的关系固化
	 * @param userid
	 */
	public void fixorg(String orgid,String parentorgid)
	{
		ParamsHandler paramHandler = ParamsHandler.getParamsHandler(orgparamHandler);
		if(paramHandler == null)
			return ;
		Params paramsList = paramHandler._getParams(orgid, orgparamType);
		if(paramsList == null)
		{
			paramsList = new Params();
			List<Param> params = new ArrayList<Param>();
			Param param = new Param(orgparamType, orgid, "fixedparentorg",
					parentorgid, 0) ;
			params.add(param);
			paramsList.setParams(params);
		}
		else
		{
			Param param = new Param(orgparamType, orgid, "fixedparentorg",
					parentorgid, 0) ;
			paramsList.updateParam(param);
		}
		paramHandler.saveParams(paramsList);
	}
	
	public Map<String,String> getFixedOrgInfos()
	{
		ParamsHandler paramHandler = ParamsHandler.getParamsHandler(orgparamHandler);
		if(paramHandler == null)
			return null;
		try {
			return paramHandler.getStringParamMap(orgparamType, "fixedparentorg");
		} catch (ParamException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String,String> getFixedUserOrgInfos()
	{
		ParamsHandler paramHandler = ParamsHandler.getParamsHandler(this.userparamHandler);
		if(paramHandler == null)
			return null;
		try {
			return paramHandler.getStringParamMap(this.userparamType, "fixedorg");
		} catch (ParamException e) {
			e.printStackTrace();
			return null;
		}
	}

}
