package com.frameworkset.platform.cms.searchmanager.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.cms.searchmanager.CMSKeyWord;
import com.frameworkset.platform.cms.searchmanager.CMSMultiSearcher;
import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.platform.cms.searchmanager.HitResult;
import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchHit;
import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;
import com.frameworkset.platform.cms.searchmanager.handler.ContentHandler;
import com.frameworkset.util.StringUtil;

public class CMSSearchControler {
	public String searchIndex()
	{
		return "path:searchIndex";
	}
	public String processSearch(@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "15") int hitsPerSet,HttpServletRequest request,ModelMap model)
	{
		String queryString = request.getParameter("queryString");
		if(queryString != null)
		{
			queryString = queryString.trim();
		}
		model.addAttribute("queryString", queryString);
		String field = request.getParameter("field");
		String key = queryString;
		String siteId;
		String chnlId;
		String searchType;

		String indexName;//增加索引名称参数,da.wei

		//tagFlag为入口标志，1表示是从搜索标签页面传过来的，其它表示从其它页面传过来的
		String tagFlag = request.getParameter("tagFlag");
//		if("1".equals(tagFlag))
		{
			siteId = request.getParameter("siteId");
			chnlId = request.getParameter("chnlId");
			searchType = request.getParameter("searchType");
			indexName = request.getParameter("indexName");//增加索引名称参数,da.wei
		}
//		else
//		{
//			siteId = (String)session.getAttribute("siteId");
//			chnlId = (String)session.getAttribute("chnlId");
//			searchType = (String)session.getAttribute("searchType");
//			indexName = (String)session.getAttribute("indexName");//增加索引名称参数,da.wei
//		}
		searchType = "2";
//		session.setAttribute("siteId",siteId);
//		session.setAttribute("chnlId",chnlId);
//		session.setAttribute("searchType",searchType);
//		session.setAttribute("indexName",indexName);//增加索引名称参数,da.wei
			    
		if(searchType == null && "".equals(searchType))
			searchType = "0";

		CMSSearchManager cmsSm = new CMSSearchManager();

		try{
			if(queryString == null || queryString.length() <= 0){
				model.addAttribute("result", "没有指定查询条件");
//				response.sendRedirect("search_results_empty.jsp");
				
			}else{
			    String flag = request.getParameter("flag");
			    String indexId = "all";		
//			    String hitsPerSet = "" +pagesize;
			    //String fromDays = "~";
			    String dayBegin = "";
				String dayEnd = "";
			    String fileFormat = "all";
			    String andor = "and";
			    String sort = "time";
			    String isInResult = request.getParameter("isInResult");
			    
			    //为0表示advanced_search.jsp提交来的，

			    //为1表示为search_results.jsp,search_results_empty.jsp提交来的,
			    //其它的如从help.jsp提交来或者标签页面传来时，flag为null,采用默认值

//			    if("0".equals(flag)){
			        indexId = request.getParameter("indexId");
//				    hitsPerSet = request.getParameter("hitsPerSet");
				    //fromDays = request.getParameter("fromDays");
				    dayBegin = request.getParameter("dayBegin");
				    dayEnd = request.getParameter("dayEnd");
			        fileFormat = request.getParameter("fileFormat");
			        andor = request.getParameter("andor");
			        sort = request.getParameter("sort");
			        field = request.getParameter("field");
//			    }else if("1".equals(flag)){
//				    indexId = (String)session.getAttribute("indexId");
//				    hitsPerSet = (String)session.getAttribute("hitsPerSet");
//				    //fromDays = (String)session.getAttribute("fromDays");
//				    dayBegin = (String)session.getAttribute("dayBegin");
//				    dayEnd = (String)session.getAttribute("dayEnd");
//				    fileFormat = (String)session.getAttribute("fileFormat");
//				    andor = (String)session.getAttribute("andor");
//				    sort = request.getParameter("sort");
//				    if(sort == null || sort.equals(""))
//				    	sort = (String)session.getAttribute("sort");
//				    field = (String)session.getAttribute("field");
//			    }
			    
				
				//优先按照索引名搜索,tempIndex被覆盖,weida
		 	    //如果chnlId不为空或者不为，则是频道搜索，根据频道id可以得到该频道的索引id,只针对标签页面传来的情况
		 		if(chnlId != null && chnlId.length()>0 )
		 		{
		 			CMSSearchIndex tempIndex = cmsSm.getIndexByChnlId(chnlId);
		 			if(tempIndex != null && tempIndex.getId()>0)
		 				indexId = tempIndex.getId() + "";
		 		}
				//如果indexName(索引名称)不为空或者不为，则是索引名称搜索，根据索引名称可以得到该索引的id,只针对标签页面传来的情况,weida
		 		else if(indexName != null && indexName.length()>0)
		 		{
					CMSSearchIndex tempIndex = cmsSm.getIndexIdByIndexName(indexName);
		 			if(tempIndex != null && tempIndex.getId()>0)
		 				indexId = tempIndex.getId() + "";
		 		}
			    
			    String realFileFormat = "all";
			    if("word".equals(fileFormat))
			       	realFileFormat = ContentHandler.WORD_FILEFOMAT;
			    else if("pdf".equals(fileFormat))
			    	realFileFormat = ContentHandler.PDF_FILEFOMAT;
			    else if("text/html".equals(fileFormat))
			    	realFileFormat = ContentHandler.TEXT_HTML_FILEFOMAT;
			    else if("excel".equals(fileFormat))
			    	realFileFormat = ContentHandler.EXCEL_FILEFOMAT;
			    else if("ppt".equals(fileFormat))
			    	realFileFormat = ContentHandler.PPT_FILEFOMAT;
			    else if("rtf".equals(fileFormat))
			    	realFileFormat = ContentHandler.RTF_FILEFOMAT;
			    else
			    	realFileFormat = "all";
			    
			    if(searchType=="4" || searchType.equals("4")){
			    	realFileFormat = ContentHandler.DBT_FILEFOMAT;
			    }
			    	
			    
			    List indexList = new ArrayList();
			    List tempIndexList = null;
			    if("all".equals(indexId) || indexId == null || "all_advanced".equals(indexId)){
			    	if("all".equals(indexId) || indexId == null){				//针对具体一种搜索类型，如站内搜索，站外搜索
			   			tempIndexList = cmsSm.getIndexListOfSite(siteId,Integer.parseInt(searchType));
						//searchType的作用到此,因此对于以后的搜索不同的索引文件不分站内站外及频道

			   		}else if("all_advanced".equals(indexId)){						//联合站内搜索和站外搜索，即包括整站索引和所有站外索引

			   			tempIndexList = cmsSm.getIndexListOfSite(siteId,1); 
			   			List tempIndexList2 = cmsSm.getIndexListOfSite(siteId,2);
			  			for(int i=0;i<tempIndexList2.size();i++)
			  				tempIndexList.add(tempIndexList2.get(i));
			   		}
			   		//测试
			   		//tempIndexList = cmsSm.getIndexListOfSite(siteId,0);
			   		int inedexSize = tempIndexList.size();
			   		for(int i=0;i<inedexSize;i++){
			   			CMSSearchIndex tempIndex = (CMSSearchIndex)tempIndexList.get(i);
			   			if(new File(cmsSm.getAbsoluteIndexFilePath(tempIndex)).exists())
			   				indexList.add(tempIndex);
			   		}
			   	}else{
			   		CMSSearchIndex tempIndex = cmsSm.getIndex(indexId);
			   		if(new File(cmsSm.getAbsoluteIndexFilePath(tempIndex)).exists())
			   			indexList.add(tempIndex);
			   	}
			    CMSMultiSearcher cmsSearcher = new CMSMultiSearcher();
			    cmsSearcher.setIndexes(indexList);			
			    cmsSearcher.setFileFormat(realFileFormat);
				//每页显示条数
//				if (hitsPerSet == null) {
//					hitsPerSet="10";
					cmsSearcher.setHitsPerSet(hitsPerSet);
//				}else {
//					cmsSearcher.setHitsPerSet(Integer.parseInt(hitsPerSet));
//				}
					    
			    //时间段

				//if(fromDays==null || fromDays.equals("~")) {
				//	fromDays="~";
				//}else{
				//	cmsSearcher.setFrom(Integer.valueOf(fromDays).intValue());
				//}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				if(dayBegin != null && !dayBegin.equals(""))
				{
					cmsSearcher.setFrom(dateFormat.parse(dayBegin).getTime());
					//System.out.println(dateFormat.parse(dayBegin).getTime());
				}
				if(dayEnd != null && !dayEnd.equals(""))
				{
					cmsSearcher.setTo(dateFormat.parse(dayEnd).getTime() + 1000*60*60*24);
					//System.out.println(dateFormat.parse(dayEnd).getTime());
				}
				
				//排序，按时间或者相关度排序
				if(sort == null || sort.length() <= 0){
					sort = "time";
				}
				cmsSearcher.setSort(sort);
				//查询字段
				if(field == null || field.length() <= 0){
					field = "all";
				}
				cmsSearcher.setField(field);
			    //查询字符串

			    String keyWord="";
			    String output="";
			    StringTokenizer st=new StringTokenizer(queryString);
			    if(queryString.toLowerCase().indexOf("and")>=0||queryString.toLowerCase().indexOf("or")>=0){
					output=queryString.toLowerCase(); ///////////////////////////////////////////////////////////
					//st=new StringTokenizer(output,"and");
				}else{
					String str1="";
					str1=st.nextToken();
					output=str1;
					keyWord=str1;
					if(null == andor || "".equals(andor))
						andor = "and";	
					while(st.countTokens()>0){
						str1=st.nextToken();
						keyWord+="&";
						keyWord+=str1;
						output+=" " + andor +" ";
						output+=str1;
					}
				}
				cmsSearcher.setQueryStr(output);
				//是否在结果中查询
				if (isInResult!=null && isInResult.equals("on")){
					output = queryString + "  and  "+output;
					String oldKeyWord = request.getParameter("oldKeyWord");
					if(StringUtil.isEmpty(oldKeyWord))
					{
						model.addAttribute("oldKeyWord", oldKeyWord);
						keyWord = oldKeyWord + "&" + keyWord;
					}
					keyWord = request.getAttribute("keyWord") + "&" + keyWord;
				}
				//执行查询动作
				HitResult searchhit = cmsSm.search(cmsSearcher);
				List<CMSSearchHit> searchhitList = searchhit.getHits();
				for(int i = 0; searchhitList != null && i < searchhitList.size(); i ++)
				{
					CMSSearchHit hit = searchhitList.get(i);
					String content = hit.getContent();
					CMSKeyWord kw = new CMSKeyWord(keyWord);
					String str1 = cmsSm.getInterceptContent(key,content);
					String outString = kw.display(str1);
					hit.setContent(outString);
					String contentType = hit.getContentType();
					String contentTypeDes = "[HTM]";
				
					if(ContentHandler.EXCEL_FILEFOMAT.equals(contentType))
					{
						contentTypeDes = "[XLS]";
					}
					else if(ContentHandler.PDF_FILEFOMAT.equals(contentType))
					{
						contentTypeDes = "[XLS]";
					}
					else if(ContentHandler.PPT_FILEFOMAT.equals(contentType))
					{
						contentTypeDes = "[PPT]";
					}
					else if(ContentHandler.RTF_FILEFOMAT.equals(contentType))
					{
						contentTypeDes = "[PPT]";
					}
					else if(ContentHandler.WORD_FILEFOMAT.equals(contentType))
					{
						contentTypeDes = "[DOC]";
					}
					else if(ContentHandler.TEXT_HTML_FILEFOMAT.equals(contentType))
					{
						contentTypeDes = "[HTM]"; 
					}
					hit.setContentTypeDes(contentTypeDes);
					int rank = i;					//编号
					String bgcolor = (rank%2==0)?"#F3FBFB":"#EEF5FA";
					hit.setBgcolor(bgcolor);
					
					
				}
				model.addAttribute("searchhitList", searchhitList);
				model.addAttribute("searchTime", searchhit.getSearchTime());
				model.addAttribute("rowcount", searchhitList.size());
//				session.setAttribute("searchhitList", searchhitList);
//				session.setAttribute("qStr", output);
//				session.setAttribute("queryString", queryString);
//				session.setAttribute("hitsPerSet", hitsPerSet);
//				session.setAttribute("indexId", indexId);
//			    //session.setAttribute("fromDays", fromDays);
//			    session.setAttribute("dayBegin", dayBegin);
//			    session.setAttribute("dayEnd", dayEnd);
//			    session.setAttribute("fileFormat", fileFormat);
//			    session.setAttribute("keyWord", keyWord);
//			    session.setAttribute("sort", sort);
//			    session.setAttribute("field", field);
			    
//			    response.sendRedirect("search_results.jsp");
			}   
				
		}catch(Exception e){
//			out.println(e.toString());
			model.addAttribute("result", StringUtil.formatBRException(e));
		}
		return "path:searchresult";
	}

}
