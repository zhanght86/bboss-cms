package com.frameworkset.platform.dictionary.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.util.ListInfo;

public class DictList extends DataInfoImpl implements java.io.Serializable {

	private static final Logger logger = LoggerFactory.getLogger(DictList.class
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
			//高级字段查询条件
			String attachFieldSql = (String)request.getAttribute("attachFieldSql");
			//System.out.println("attachFieldSqlList = " + attachFieldSql);
			
//			listInfo = dictManager.getDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize);
			listInfo = dictManager.getDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,attachFieldSql,(int)offset,maxPagesize);
			return listInfo;
			
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
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
			//高级字段查询条件
			String attachFieldSql = (String)request.getAttribute("attachFieldSql");
			//System.out.println("attachFieldSqlList = " + attachFieldSql);
			
//			listInfo = dictManager.getDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,(int)offset,maxPagesize);
			listInfo = dictManager.getDictdataList(dictId,showdata,realitydata,occurOrg,isaVailability,attachFieldSql,-2,-2);
			return listInfo;
			
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}

}
