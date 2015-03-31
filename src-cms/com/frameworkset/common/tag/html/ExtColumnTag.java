package com.frameworkset.common.tag.html;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.documentmanager.DocumentExtColumnManager;

public class ExtColumnTag extends CMSBaseTag{
	private DocumentExtColumnManager docUtil = new DocumentExtColumnManager();
	private CMSTagUtil tagUtil = new CMSTagUtil();
	private String document_id="";
	private String column_name="";
	
	public String show(){
		StringBuffer sb = new StringBuffer();
		String tableName = "td_cms_document";
		String column_name = "";
		String column_type = "";
		String column_comments = "";
		String value = "";
		List list = DocumentExtColumnManager.getExtColumns();
		Context top = new VelocityContext();
		sb.append(tagUtil.loadTemplate("publish/extColumn/content-top.vm",top));
		/*没有指定列名,就输出全部的扩展字段*/
		if(this.column_name.length()<=0){
			for(int i=0;i<list.size();i++){
				String[] str = (String[])list.get(i);
	            column_name = str[0];
	            if(!(column_name.startsWith("ext_") || column_name.startsWith("EXT_"))) continue; 
	            column_type = str[1];
	            column_comments = str[3];
	            Context loop = new VelocityContext();
	            loop.put("name",column_name);
	            loop.put("comments",column_comments);
	            /*获得值*/            
	            if(this.document_id.length()>0){ 
	            	value = docUtil.getExtColumnValue(this.document_id,column_name);
	            }
	            loop.put("value",value);
	            if("date".equals(column_type)|| "DATE".equals(column_type)){
	            	sb.append(tagUtil.loadTemplate("publish/extColumn/content-loop.vm",loop));
	            }else{/*非日期型的*/ 
	    		    sb.append(tagUtil.loadTemplate("publish/extColumn/content-loop.vm",loop));
	            }
			}
		}else{/*指定列名,就输出指定的扩展字段*/
			Context loop = new VelocityContext();
			if(this.document_id.length()>0){ 
            	value = docUtil.getExtColumnValue(this.document_id,column_name);
            }
            loop.put("value",value);
			sb.append(tagUtil.loadTemplate("publish/extColumn/content-loop.vm",loop));
		}
		Context down = new VelocityContext();
		sb.append(tagUtil.loadTemplate("publish/extColumn/content-down.vm",down));
	    return sb.toString();	
	}
	
	
	
	public int doStartTag() throws JspException{
	    try {
	        pageContext.getOut().println(show());
	    }catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
		return super.doStartTag();
	}

	public String getDocument_id() {
		return document_id;
	}

	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}



	@Override
	public void doFinally() {
		document_id="";
		  column_name="";
		super.doFinally();
	}

	
}
