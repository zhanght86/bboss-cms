package com.frameworkset.platform.dictionary.tag;

import org.apache.log4j.Logger;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.platform.security.AccessControl;
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

public class UserUsualDictListSelected extends DataInfoImpl implements java.io.Serializable {

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
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(session);
			String occurOrg = accesscontroler.getChargeOrgId();
			String identifier = request.getParameter("identifier");
			//选择机构和没选择机构是不同的
			//因为当前登陆用户能"可见"的编码, 对选择的机构, 并不一定是可见的
			//所以,当选择机构进行"常用"授权时, 必须通过选择机构(被选择的机构肯定是登陆用户所在机构的下级机构!!!) 进行编码过滤.
			String selectOrgId = request.getParameter("orgId")==null?"":request.getParameter("orgId");
			
			//如果是通用字典类型 业务字典
			if(dictManager.getDicttype_type(dictId) == DictManager.ALLREAD_BUSINESS_DICTTYPE){
				return dictManager.getUsualDictdataList(dictId,showdata,selectOrgId,identifier,(int)offset,maxPagesize);				
			}else{//授权字典类型 业务字典  过滤
				if(!selectOrgId.equals("")){//如果选择了机构则按照选择机构的授权类型显示数据项
					return dictManager.getReadDictdataList(dictId,showdata,selectOrgId,identifier,(int)offset,maxPagesize);
				}else{//如果没选择机构
					if(accesscontroler.isAdmin()){//如果是超级管理员，则看到所有字典数据项
						return dictManager.getDictdataList(dictId,"",showdata,identifier,(int)offset,maxPagesize);
					}else{//否则只能看到用户所能管理的部门的已授权的数据项
						if(identifier.equals("sealed")){
							return dictManager.getUserDictdataList(dictId,occurOrg,showdata,"selected",(int)offset,maxPagesize,accesscontroler.getUserID());
						}else{
							return null;
						}
					}
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
