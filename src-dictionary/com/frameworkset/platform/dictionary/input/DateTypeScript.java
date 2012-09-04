/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.dictionary.Item;
 
/**
 * <p>Title: DateTypeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-15 9:27:49
 * @author ge.tao
 * @version 1.0
 */
public class DateTypeScript  extends BaseInputTypeScript{
//	public String getExtendHtmlContent(DictAttachField dictatt) {
//		int initLength = dictatt.getMaxLength();
//		if(initLength == 0){//没有设置长度 long int
//			initLength = 5;
//		}
//		String isNullStr = "";
//		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
//			isNullStr = "<span style='color:red'>必填</span>";
//		}
//		StringBuffer html = new StringBuffer();
//		//生成选择时间的脚本:
//		html.append("<input name='").append(dictatt.getTable_column().toLowerCase())
//		    .append("' type='text' readonly='true' style='width:150px' ")
//		    .append("value='").append(dictatt.getFieldValue()).append("' validator='")
//		    .append(dictatt.getFieldValidType()).append("' cnname='")
//		    .append(dictatt.getDictFieldName()).append("' />")
//		    .append("<input type='button' class='input' value='时间' onclick='selectTime(\"document.all.")
//		    .append(dictatt.getTable_column().toLowerCase())
//		    .append("\",0)'>").append(isNullStr);
//		return html.toString();
//	}

	public DateTypeScript(DictAttachField dictatt) {
		super(dictatt);
	}

	public String getEditExtendHtmlContent(HttpServletRequest request, HttpServletResponse response, Map keyWords) {
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		StringBuffer html = new StringBuffer();
		boolean readonly = this.isReadOnly(keyWords); 
		
		//生成选择时间的脚本:
		html.append("<input name='").append(dictatt.getTable_column().toLowerCase())
		    .append("' type='text' readonly='true' style='width:150px' ")
		    .append("value='").append(dictatt.getFieldValue()).append("' validator='")
		    .append(dictatt.getFieldValidType()).append("' cnname='")
		    .append(dictatt.getDictFieldName()).append("' />")
		    .append("<input type='button' class='input' value='时间' ");
		if(readonly){
			html.append("disabled='true'");
		}
		html.append(" onclick='selectTime(\"document.all.")
		    .append(dictatt.getTable_column().toLowerCase())
		    .append("\",0)'>").append(isNullStr);
		return html.toString();
	}

	public String getNewExtendHtmlContent(HttpServletRequest request, HttpServletResponse response) {
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		StringBuffer html = new StringBuffer();
		//生成选择时间的脚本:
		html.append("<input name='").append(dictatt.getTable_column().toLowerCase())
		    .append("' type='text' readonly='true' style='width:150px' ")
		    .append("value='").append(dictatt.getFieldValue()).append("' validator='")
		    .append(dictatt.getFieldValidType()).append("' cnname='")
		    .append(dictatt.getDictFieldName()).append("' />")
		    .append("<input type='button' class='input' value='时间' onclick='selectTime(\"document.all.")
		    .append(dictatt.getTable_column().toLowerCase())
		    .append("\",0)'>").append(isNullStr);
		return html.toString();
	}

}
