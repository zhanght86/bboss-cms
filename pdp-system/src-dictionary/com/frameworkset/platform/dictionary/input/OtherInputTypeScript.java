package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;
/**
 * 其他类型
 * 
 * <p>Title: OtherInputTypeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date  2008-3-25 9:15:14
 * @author gao.tang
 * @version 1.0
 */
public class OtherInputTypeScript extends BaseInputTypeScript {
	private String inputScript; 
	public OtherInputTypeScript(DictAttachField dictatt,String inputScript) {
		super(dictatt);
		this.inputScript = inputScript;
		// TODO Auto-generated constructor stub
	}

	public String getEditExtendHtmlContent(HttpServletRequest request,
			HttpServletResponse response, Map keyWords) {
		boolean readonly = this.isReadOnly(keyWords);
		int index = inputScript.indexOf("{extend}");
		StringBuffer html = new StringBuffer();
		if(index > 0){
			html.append(inputScript.substring(0,index)).append(dictatt.getTable_column().toLowerCase()).append(inputScript.substring(index+1));		
			this.inputScript = html.toString();
			html.setLength(0);
		}else{
			html.append("<input type='text' name='").append(dictatt.getTable_column().toLowerCase()).append("'>");
			this.inputScript = html.toString();
			html.setLength(0);
		}
		return html.toString();
	}

	public String getNewExtendHtmlContent(HttpServletRequest request,
			HttpServletResponse response) {
		
		int index = inputScript.indexOf("{extend}");
		StringBuffer html = new StringBuffer();
		if(index > 0){
			html.append(inputScript.substring(0,index)).append(dictatt.getTable_column().toLowerCase()).append(inputScript.substring(index+1));		
			this.inputScript = html.toString();
			html.setLength(0);
		}else{
			html.append("<input type='text' name='").append(dictatt.getTable_column().toLowerCase()).append("'>");
			this.inputScript = html.toString();
			html.setLength(0);
		}
		return html.toString();
	}

}
