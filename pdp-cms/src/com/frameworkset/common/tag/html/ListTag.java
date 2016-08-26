package com.frameworkset.common.tag.html;

import java.io.Serializable;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.search.SearchService;
import com.frameworkset.common.tag.BaseTag;


public class ListTag extends BaseTag {
	/////////////////////////////////////////////////////////

	private String name;

	private String type;
	private String datasource;
	private int left;
	private int top;
	private int width;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		
		this.name = name;
	}
	
	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}
	
	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
    
   
  //////////////////////////////////////////////////////////
	public int doStartTag() throws JspException{
	
		try{
		
			StringBuffer buffer = new StringBuffer();
			buffer.append("<select name=\"select"+name+"\"  multiple  style=\"position:absolute; left: "+ left +"px; top: "+ top + "px; width:"+ width + "px;display:none\" id=\"select"+name+"\"  onclick='change(\"select"+name+"\",\""+name+"\",\"icon"+name+"\")' onkeydown='keydown(\"select"+name+"\",\""+name+"\",\"icon"+name+"\")'  onkeyup='keyup(\"select"+name+"\",\""+name+"\",\"icon"+name+"\",\""+type+"\",\""+datasource+"\");'>\n");
			String[] resultall = SearchService.search(type,"",datasource);
			for(int i = 0;i<resultall.length;i++)
			{
				buffer.append("<option value='"+ resultall[i] + "'>"+ resultall[i] + "</option>\n");	
			}
			int top1,left1;
			top1=top - 20;
			left1=left + width - 17;
			buffer.append("</select>\n");
			buffer.append("<input type=\"text\" style=\"position:absolute; left: "+ left +"px; top: "+ top1 + "px; width:"+ width + "px; height: 20px\"   name=\""+ name +"\" onkeyup='keyup(\"select"+name+"\",\""+name+"\",\"icon"+name+"\",\""+type+"\",\""+datasource+"\");' onkeydown='keydown(\"select"+name+"\",\""+name+"\",\"icon"+name+"\")'>\n");
			buffer.append("<img src=\"cc.jpg\" name=\"icon"+name+"\" onclick='changestatus(\"select"+name+"\",\""+name+"\",\"icon"+name+"\")' style=\"position:absolute; left: "+ left1 +"px; top: "+ top1 + "px;\">\n");
			out.print(buffer.toString());
			return EVAL_BODY_INCLUDE;
		}catch(Exception e){
		
			throw new JspException(e.getMessage());
		}
		
	}
	public int doEndTag() throws JspException{
	try{
		
	   return EVAL_PAGE;//SKIP_PAGE;
	}catch(Exception e){
		
		throw new JspException(e.getMessage());
	}	
 }
	public void release(){
		super.release();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	@Override
	public void doFinally() {
		 name = null;

			 type= null;
			 datasource= null;
			 left= 0;
			 top= 0;
			  width= 0;
		super.doFinally();
	}
	
}