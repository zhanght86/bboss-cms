package com.frameworkset.platform.cms.docsourcemanager.tag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.cms.docsourcemanager.Docsource;
import com.frameworkset.util.ListInfo;

public class DocsourceList extends DataInfoImpl  
{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
		String srcName = request.getParameter("srcName");
		String provider = request.getParameter("provider");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		ListInfo listInfo = new ListInfo();
		try
		{
			ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/cms/documentmanager/document.xml");
			SQLParams params = new SQLParams();
			final SimpleDateFormat dateformat = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
			if (null != srcName && !"".equals(srcName))
				params.addSQLParam("srcName", "%" + srcName +"%", SQLParams.STRING);
	//			sqlWhere += " and SRCNAME like '%" + srcName + "%' ";
			if (null != provider && !"".equals(provider))
				params.addSQLParam("provider", "%" + provider +"%", SQLParams.STRING);
	//			sqlWhere += " and user_name like '%" + provider + "%' ";
			if (null != fromDate && !"".equals(fromDate))
				params.addSQLParam("fromDate",  dateformat.parse(fromDate), SQLParams.DATE);
	//			sqlWhere += " and to_char(CRTIME,'YYYY-MM-DD') >= '" + fromDate + "' ";
			if (null != toDate && !"".equals(toDate))
				params.addSQLParam("toDate",  dateformat.parse(toDate), SQLParams.DATE);
	//			sqlWhere += " and to_char(CRTIME,'YYYY-MM-DD') <= '" + toDate + "' ";
				
			 
	
			
			final List<Docsource> list = new ArrayList<Docsource>();
			listInfo = executor.queryListInfoBeanByNullRowHandler(new NullRowHandler(){
	
				@Override
				public void handleRow(Record dbUtil)
						throws Exception {
					Docsource docsource = new Docsource();
					if (!"null".equals(dbUtil.getString("srcname")))
						docsource.setSRCNAME(dbUtil.getString("srcname"));
					if (!"null".equals(dbUtil.getString("srcdesc")))
						docsource.setSRCDESC(dbUtil.getString("srcdesc"));
					if (!"null".equals(dbUtil.getString("srclink")))
						docsource.setSRCLINK(dbUtil.getString("srclink"));
					docsource.setCRUSER(dbUtil.getInt("CRUSER"));
					docsource.setUsername(dbUtil.getString("user_name"));
					Date crdate = dbUtil.getDate("crdate");
					if (crdate != null)
						docsource.setCRTIME(dateformat.format(crdate));
					docsource.setDOCSOURCE_ID(dbUtil.getInt("docsource_id"));
					list.add(docsource);
					
					
				}
				
			}, "getDocsourceList", offset, maxPagesize, params);
			listInfo.setDatas(list);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
			 
		return listInfo;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}