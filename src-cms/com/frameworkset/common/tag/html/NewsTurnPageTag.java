package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.common.tag.CMSSupportTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.common.tag.pager.tags.PagerContext;
/**
 * useless
 * @author 陶格
 *
 */
public class NewsTurnPageTag extends  CMSSupportTag{
    private CMSTagUtil tagUtil = new CMSTagUtil();
    private String prevstr = "上一页";
    private String nextstr = "下一页";
    private int currentpage = 1;
    private int totalpage = 10; 
	
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

    /**
     * 需要上下文给出: 当前页; 总页数; 根据当前页生的 链接地址;
     * @return
     * @throws Exception
     */
	public String turnPageString()throws Exception{
		StringBuffer output = new StringBuffer();		
		Context top = new VelocityContext();
		top.put("currentpage",getCurrentPage()+"");
		top.put("totalpage",getTatalPage()+"");
		top.put("prevstr",getPrevstr());
		top.put("currentpath",getCurrentPath(getCurrentPage()-1)+"");
		output.append(tagUtil.loadTemplate("publish/newsTurnPage/content-top.vm",top));
		/* 中间页码 */
		for(int i=1;i<=totalpage;i++){
			if(i==currentpage){
				output.append(i+" ");
			}else{
				Context loop = new VelocityContext();		
				loop.put("count",i+"");
				loop.put("currentpath",getCurrentPath(i));				
				output.append(tagUtil.loadTemplate("publish/newsTurnPage/content-loop.vm",loop));
			}
		}
		
		/* 向后翻处理 */
		Context down = new VelocityContext();
		down.put("currentpage",getCurrentPage()+"");
		down.put("totalpage",getTatalPage()+"");
		top.put("currentpath",getCurrentPath(getCurrentPage()+1)+"");
		output.append(tagUtil.loadTemplate("publish/newsTurnPage/content-down.vm",down));		

		 
		return output.toString();
	}
	
	public int doEndTag()throws JspException{
		try {
			ContentContext contentContext = (ContentContext)context;			
			if(contentContext!=null){
				if(!contentContext.isPagintion()) return this.EVAL_PAGE;
			}				
			
			String output = turnPageString();
			out.println(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
		
	}
	
    /**
     * 获得链接路径
     * @param currentpage
     * @return
     */
	public String getCurrentPath(long currentpage){			
		String path = ""; 
		String indexURL = context.getSite().getIndexFileName();
		indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);
		if(currentpage==1) return indexURL;
		else{
			indexURL = indexURL.substring(0,indexURL.indexOf(".htm"));
			path = indexURL+currentpage+".html";
		}
		return path;
	}
	
	public long getCurrentPage(){
		long currentPage = 0;
		PagerContext pgcontext = listTag.getPagerContext();
		long offsetNumber = pgcontext.getOffset();
		int maxNumber = listTag.getMaxPageItems();
		currentPage = (offsetNumber+1)%maxNumber;
		return currentPage;
	}
	
	public long getTatalPage(){
		return listTag.getRowcount();
	}

	

}
