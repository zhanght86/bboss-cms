package com.frameworkset.platform.dictionary.tag;

import org.apache.log4j.Logger;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 常用:管理员, 授权字典 :全部看到
 *            常用字典 :全部看到
 *     其他人:机构管理员, 普通用户
 *        机构管理员:
 *            授权字典: 所管理机构的 可见过滤
 *            常用字典: 全部看到 
 *        普通用户: 
 *            授权字典: 所属机构的 可见过滤
 *            常用字典: 全部看到
 * <p>Title: UserDictList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-24 8:39:24
 * @author ge.tao
 * @version 1.0
 */

public class UserUsualDictList extends DataInfoImpl implements java.io.Serializable {

	private static final Logger logger = Logger.getLogger(UserUsualDictList.class
			.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
			
		try {
			ListInfo listInfo = new ListInfo();
			String dictId = request.getParameter("did");

			if (dictId == null || dictId.equals("")) {
				dictId = (String)request.getAttribute("did");
			}
			
			if (dictId == null || dictId.equals("")) {
				dictId = (String)request.getParameter("selected");
			}
			
			if (dictId == null || dictId.equals("")) {
				return listInfo;
			}
			DictManager dictManager = new DictManagerImpl();			
            /**
             * dictId 是123:123:aa 字典类型ID:数据项ID:数据项名称 
             */
			String showdata = (String)request.getParameter("showdata");
			if(showdata == null || "".equals(showdata)){
				showdata = "";
			}
			String realitydata = (String)request.getParameter("realitydata");
			if(realitydata == null || "".equals(realitydata)){
				realitydata = "";
			}
			String occurOrg = (String)request.getParameter("occurOrg");
			if(occurOrg == null || "".equals(occurOrg)){
				occurOrg = "";
			}
			String isaVailability = (String)request.getParameter("isaVailability");
			if(isaVailability == null || "".equals(isaVailability)){
				isaVailability = "-1";
			}
			//选择机构和没选择机构是不同的
			//因为当前登陆用户能"可见"的编码, 对选择的机构, 并不一定是可见的
			//所以,当选择机构进行"常用"授权时, 必须通过选择机构(被选择的机构肯定是登陆用户所在机构的下级机构!!!) 进行编码过滤.
			String selectOrgId = request.getParameter("orgId")==null?"":request.getParameter("orgId");
			
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(this.session);
			if((accesscontroler.isAdmin()&& "".equalsIgnoreCase(selectOrgId)) || dictManager.getDicttype_type(dictId) == DictManager.ALLREAD_BUSINESS_DICTTYPE){//是管理员并且没选机构,或者是通用字典类型,全部的
				return dictManager.getDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize);				
			}else{//其他用户和授权字典
				String userId = accesscontroler.getUserID();
//				//判断用户是否部门管理员
//				OrgAdministrator orgAdminImpl = new OrgAdministratorImpl();
//				if(orgAdminImpl.isOrgAdmin(userId,"")){//是部门管理员 能看到 所管理的机构 可见编码
//					return dictManager.getOrgManagerDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,userId);
//				}else{//普通用户 能看到 所属机构 可见编码 
//					return dictManager.getNormalUserDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,userId);
//				}
				
				//普通用户==机构(部门)管理员, 都是看到用户 所属机构 可见编码 
				//(1)没选择机构, 看登陆用户所在机构的 "可见" 权限 的所有编码
				if(selectOrgId==null || selectOrgId.trim().length()==0){
					return dictManager.getNormalUserDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,userId);
				}else{//(2)选择了机构,看当前选中机构的 "可见" 权限 的所有编码
					return dictManager.getOrgReadDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize,selectOrgId);
					
				}
			}
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
