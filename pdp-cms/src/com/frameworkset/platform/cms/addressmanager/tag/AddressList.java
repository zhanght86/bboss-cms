package com.frameworkset.platform.cms.addressmanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.addressmanager.AddressManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class AddressList extends DataInfoImpl implements java.io.Serializable
{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
		String userId = request.getParameter("userId");
		String isAll = request.getParameter("isAll");
		String presonName = request.getParameter("presonName");
		String phoneType = request.getParameter("phoneType");
		String phone = request.getParameter("phone");
		String company = request.getParameter("company");
		String fax = request.getParameter("fax");
		if(userId==null){
			return null;
		}
		ListInfo listInfo = new ListInfo();
		String sql = " select * from td_cms_person_addressbook where USER_ID = "+userId;
		if("no".equals(isAll) || isAll == null || "".equals(isAll)){
			if(presonName != null && !"".equals(presonName)){
				sql += " and PERSON_NAME like '%"+presonName+"%' ";
			}
			if(phone != null && !"".equals(phone)){
				if(phoneType != null && "PERSON_MOBILETEL".equals(phoneType)){
					sql += " and (PERSON_MOBILETEL1 like '%"+phone+"%' or PERSON_MOBILETEL2 like '%"+phone+"%' ) ";
				}else{
					sql += " and "+phoneType+" like '%"+phone+"%' ";
				}
			}
			if(company != null && !"".equals(company)){
				sql += " and PERSON_COMPANY like '%"+company+"%' ";
			}
			if(fax != null && !"".equals(fax)){
				sql += " and PERSON_FAX like '%"+fax+"%' ";
			}
		}
		try {
			listInfo = new AddressManagerImpl().getAddressList(sql,(int)offset,maxPagesize);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}