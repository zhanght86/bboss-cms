package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;

public class IndexPageFilter implements FileFilter,java.io.Serializable {
	private TemplateManager tm = null;
	private String siteId;
	private String uri;
	private boolean isFormFile = false;
	
	public IndexPageFilter(String siteId,String uri){
		this.siteId = siteId == null?"0":siteId;
		this.uri = uri == null?"":uri;
		this.isFormFile = false;
	}
	
	/**
	 * 用于动态表单的文件的选择
	 * @param isFormFile
	 * @param uri
	 */
	public IndexPageFilter(boolean isFormFile,String uri){
		this.siteId = "";
		this.uri = uri == null?"":uri;
		this.isFormFile = isFormFile;
	}
	
	public boolean accept(File pathname) {  
		if(pathname.getName().endsWith(".svn"))
			return false;
		if(!isFormFile)
		{
			tm = new TemplateManagerImpl();
			if(pathname.isDirectory())
				return false; 
			String fileName = pathname.getName();
			int startIndex = 0;
			int endIndex = uri.length(); 
			if(uri.equals("/"))
			{
				uri = "";
			}
			else
			{
				if(uri.startsWith("/"))
					startIndex = 1;
				if(uri.endsWith("/"))
					endIndex = endIndex-1;
				uri = uri.substring(startIndex,endIndex);
			}
				
			boolean isPageTemplate = tm.isPageTemplate(siteId,uri,fileName);
			String lowerName = pathname.getName().toLowerCase();
			if((lowerName.endsWith(".html")||lowerName.endsWith(".htm")||lowerName.endsWith(".jsp")) && !isPageTemplate)
				return true;
			return false;
		}
		else
		{
			//tm = new TemplateManagerImpl();
			if(pathname.isDirectory())
				return false; 
			/*String fileName = pathname.getName();
			int startIndex = 0;
			int endIndex = uri.length(); 
			if(uri.startsWith("/"))
				startIndex = 1;
			if(uri.endsWith("/"))
				endIndex = endIndex-1;
			uri = uri.substring(startIndex,endIndex);
			
			boolean isPageTemplate = tm.isTemplate(uri,fileName);*/
			String lowerName = pathname.getName().toLowerCase();
			/*if((lowerName.endsWith(".html")||lowerName.endsWith(".htm")||lowerName.endsWith(".jsp")) && !isPageTemplate)
				return true;*/
			if((lowerName.endsWith(".html")||lowerName.endsWith(".htm")||lowerName.endsWith(".jsp")))
				return true;
			return false;
		}
	}
}
