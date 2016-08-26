package com.frameworkset.common.tag.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

import com.frameworkset.common.tag.CMSSupportTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.driver.context.PagineContext;

/**
 * 生成概览翻页和文档分页索引的标签
 * 只能用于概览模板和细览模板，不能放于站点的首页模板
 * <p>Title: CMSIndexTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-25 11:28:58
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSIndexTag extends CMSSupportTag{
//	private CMSTagUtil tagUtil = new CMSTagUtil();	
    private String prevstr = "上一页";
    private String nextstr = "下一页";
    private String firststr = "首页";
    private String laststr = "尾页";
    private String jumpToStr = "跳转";
    private long currentPage = 0;
   
    private long totalPage = 0;   
    private long tagnumber = 0;
    /**
     * 外加样式名称 
     */
    private String classname = "";
    /**
     * 定义内部样式
     */
    private String style = "";
    public String getNextstr() {
		return nextstr;
	}

	public void setNextstr(String nextstr) {
		this.nextstr = nextstr;
	}

	public String getPrevstr() {
		return prevstr;
	}

	public void setPrevstr(String prevstr) {
		this.prevstr = prevstr;
	}

	public String getFirststr() {
		return firststr;
	}

	public void setFirststr(String firststr) {
		this.firststr = firststr;
	}

	public String getLaststr() {
		return laststr;
	}

	public void setLaststr(String laststr) {
		this.laststr = laststr;
	}
	
    /**
     * 获得链接路径
     * 概览和翻页两种情况
     * @param long currentpage 当前页
     * @return
     */
	public String getCurrentPath(long currentpage){
		String indexURL = "";
		if(context instanceof PagineContext){
			indexURL = ((PagineContext)context).getPagePath((int)currentpage - 1);
			indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);
			return indexURL;
		}else{//永远不会执行的分支
//			if(currentpage==1) return indexURL;
//			else{
//				indexURL = indexURL.substring(0,indexURL.indexOf(".htm"));
//				String path = indexURL+currentpage+".html";
//				return path;
//			}	
			return indexURL;
		}
	}
	

	/**
	 * 概览翻页标签
	 * listTag.getPagerContext().getOffset(); 当前页取到的最大记录码
	 * listTag.getMaxPageItems(); 单个页面记录条数
	 * listTag.getRowcount(); 总记录条数
	 * @return String
	 */
	public String generatorIndexScript(){
		if(context instanceof PagineContext)
		{
			PagineContext context = (PagineContext)super.context;
			currentPage = context.getCurrentPageNumber();
			totalPage = context.getTotalPages();
			if(totalPage == 0)
				return "";
			String output = this.publicHTML(totalPage,currentPage+1);
			return output;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * 公共HTML部分
	 * 包括处理当前页的链接,处理前一页,后一页,第一页和最后一页的链接
	 * 分别考虑了
	 * @param long totalPage 总页数
	 * @param long currentPage 当前页
	 * @return String
	 */
	public String publicHTML(long totalPage,long currentPage){		
		StringBuffer output = new StringBuffer();		
		Context top = new VelocityContext();
		top.put("currentpage",String.valueOf(currentPage));
		top.put("totalpage",String.valueOf(totalPage));
		top.put("prevstr",this.prevstr);
		/*向前翻*/
		top.put("currentpath",getCurrentPath(currentPage-1<0?currentPage:currentPage-1));
		top.put("firstPageLink",getCurrentPath(0));
		top.put("fisrt",this.firststr);
		output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-top.vm",top));
		/* 中间页码 */
		/* 起始页 */
		
		if(this.tagnumber>0){		
			int start = 1;
			int end = 1;
			if(this.tagnumber>3){
				if(currentPage>tagnumber){
					start = (int)currentPage-(int)Math.floor(tagnumber/2);			
				}else{
					if((currentPage%tagnumber)>=(int)Math.floor(tagnumber/2) || currentPage%tagnumber==0 )  start = (int)currentPage-(int)Math.floor(tagnumber/2);
					
				}
				start = start<=0?1:start;			
				/**
				 *  
				 */
				
				/* 尾页 */
				end = (int)this.tagnumber + start  ;
				if(end > totalPage) {
					//页码是从1开始的
					end = (int)totalPage + 1;
					start = end - (int)this.tagnumber;
					if(start<=0) start = 1;
				}
			}
			else{
				if(tagnumber<=2) tagnumber = 3;
				start = (int)currentPage-1;
				if(start<=0) start = 1 ;
				if(currentPage == 1) end = (int)currentPage + (int)this.tagnumber ;
				else end = (int)currentPage + (int)this.tagnumber - 1 ;
				if(end>totalPage){					
					end = (int)totalPage + 1;
					start = end - (int)tagnumber;
					if(start<=0) start = 1;
				}
			}
			
			for(int i=start;i<end;i++){
				if(totalPage>1){
					if(i==currentPage){
						output.append(i+" ");
					}else{
						Context loop = new VelocityContext();		
						loop.put("count",i+"");
						loop.put("currentpath",getCurrentPath(i));	
						loop.put("classname",this.classname);	
						loop.put("style",this.style);	
						output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-loop.vm",loop));
					}
				}else{
	//				Context loop = new VelocityContext();		
	//				loop.put("count"," ");
	//				loop.put("currentpath",getCurrentPath(i));				
	//				output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-loop.vm",loop));
				}
			}	
		}else{//设置为负数或者0的时候,全部显示页码
//			for(int i=1;i<totalPage+1;i++){
//				if(totalPage>1){
//					if(i==currentPage){
//						output.append(i+" ");
//					}else{
//						Context loop = new VelocityContext();		
//						loop.put("count",i+"");
//						loop.put("currentpath",getCurrentPath(i));			
//						loop.put("classname",this.classname);	
//						loop.put("style",this.style);	
//						output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-loop.vm",loop));
//					}
//				}else{
//	//				Context loop = new VelocityContext();		
//	//				loop.put("count"," ");
//	//				loop.put("currentpath",getCurrentPath(i));				
//	//				output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-loop.vm",loop));
//				}
//			}	
		}
		/* 向后 翻页处理 */
		String basePath = "";
		if(context instanceof PagineContext){
			PagineContext context = (PagineContext)super.context;
			basePath = context.getPaginJumpPath();
		}	
		if(basePath != null)//处理发布页面跳转分页路径问题，去除页面中包含的相对路径
		{
    		int idx = basePath.lastIndexOf("/");
    		if(idx > 0)
    		{
    		    basePath = basePath.substring(idx + 1);
    		}
		}
		Context down = new VelocityContext();
		down.put("currentpage",String.valueOf(currentPage));
		down.put("totalpage",String.valueOf(totalPage));
		down.put("currentpath",getCurrentPath(currentPage+1));
		down.put("nextstr",this.nextstr);
		down.put("lastPageLink",getCurrentPath(totalPage));
		down.put("last",this.laststr);
		down.put("jumpToStr",this.jumpToStr);
		down.put("basePath",basePath);
		output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-down.vm",down));
		return output.toString();
	}
	
	public int doStartTag() throws JspException{
		super.doStartTag();
		if(this.listTag != null ){
			if(!listTag.indextagSeted()){				
				this.listTag.setIndexTag(this);
			}			
		}else{
			try {
				out.print(this.generatorIndexScript());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}

	public static void main(String[] args)
	{
		String filePath = "D:/workspace/cms/src/com/frameworkset/common/tag/html/CMSIndexTag.java";
		File file = new File(filePath);
		try {
			FileReader reader = new FileReader(file);
			StringWriter writer = new StringWriter();
			int i = -1;
			while((i = reader.read()) != -1)
			{
				writer.write(i);
			}
			System.out.println(writer.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getJumpToStr() {
		return jumpToStr;
	}

	public void setJumpToStr(String jumpToStr) {
		this.jumpToStr = jumpToStr;
	}

	public long getTagnumber() {
		return tagnumber;
	}

	public void setTagnumber(long tagnumber) {
		this.tagnumber = tagnumber;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Override
	public void doFinally() {
		    prevstr = "上一页";
		     nextstr = "下一页";
		    firststr = "首页";
		     laststr = "尾页";
		     jumpToStr = "跳转";
		     currentPage = 0;
		   
		      totalPage = 0;   
		      tagnumber = 0;
		    /**
		     * 外加样式名称 
		     */
		     classname = "";
		    /**
		     * 定义内部样式
		     */
		     style = "";
		super.doFinally();
	}
	


}
