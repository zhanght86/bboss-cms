package com.frameworkset.platform.cms.documentmanager.action;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.cms.documentmanager.DocClassManager;
import com.frameworkset.platform.cms.documentmanager.bean.DocClass;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title: DocClassController.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-8-13 下午3:38:10
 * @author biaoping.yin
 * @version 1.0.0
 */
public class DocClassController {
	DocClassManager docClassManager;
	public @ResponseBody String addDocclass(DocClass docClass)
	{
		return "success";
	}
	
	public @ResponseBody String deleteDocclass(String doc_name)
	{
		return "success";
	}
	
	public String queryDocclass(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "15") int pagesize,ModelMap model)
	{
		try {
			model.addAttribute("datas", docClassManager.queryListInfoDocclass(offset, pagesize));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "path:queryDocclass";
	}
	public String main(ModelMap model,HttpServletRequest request,HttpServletResponse response)
	{
		try {
			AccessControl accesscontroler = AccessControl.getAccessControl();
			   
		    com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
			cmsManager.init(request,request.getSession(false),response,accesscontroler);
			
			String currentSiteid = cmsManager.getSiteID();
			model.addAttribute("currentSiteid", currentSiteid);
			model.addAttribute("datas", docClassManager.queryListDocclass(currentSiteid));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "path:main";
	}
	
	public @ResponseBody String saveClasses(List<DocClass> docClasses,String site_id)
	{
		try {
			docClassManager.saveClasses(docClasses, site_id);
			
		} catch (Exception e) {
			return StringUtil.exceptionToString(e);
		}
		return "success";
	}

}
