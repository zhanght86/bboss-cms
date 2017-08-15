/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;

/**
 * <p>Title: PrimaryKeyTypeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-15 10:23:24
 * @author ge.tao
 * @version 1.0
 */
public class PrimaryKeyTypeScript extends BaseInputTypeScript{
 
	public PrimaryKeyTypeScript(DictAttachField dictatt) {
		super(dictatt);
		// TODO Auto-generated constructor stub
	}

//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getFunctionScript(java.lang.String, java.lang.String)
//	 */
//	public String getFunctionScript(String fiedName, String inputTypeName,String value,int maxLength) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getFunctionContent(java.lang.String, java.lang.String)
//	 */
//	public String getFunctionContent(String fiedName, String inputTypeName,String value,int maxLength) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(java.lang.String, java.lang.String)
//	 */
//	public String getExtendHtmlContent(String fieldName, String inputTypeName,String value,int maxLength) {		
//		return null;
//	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 * 没有赋值
	 */
	public String getNewExtendHtmlContent(HttpServletRequest request,HttpServletResponse response) {
//		fieldName:主键生成规则:主键的值(sequence,class,value);
		String[] fieldNames = dictatt.getDictField().split(":");
		String fname = "";
		//rule : auto sequence interface handle
		String rule = "";
		String handlevalue = "";
		if(fieldNames.length==3){
			fname = dictatt.getTable_column().toLowerCase();
			rule = fieldNames[1];
			handlevalue = fieldNames[2];
		}else if(fieldNames.length==2){
			fname = dictatt.getTable_column().toLowerCase();
			rule = fieldNames[1];
		}else{
			fname = dictatt.getTable_column().toLowerCase();
		}
		StringBuffer html = new StringBuffer();
		//最大长度
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		//是否必填
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		//生成选择时间的脚本:
		String keyValue = "";
		if("auto".equalsIgnoreCase(rule)){//自动生成 
			html.append("<input type='text' style='width:150px' name='").append(fname.toLowerCase())
		    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
		    .append(keyValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
		    .append("  maxlength=").append(initLength).append(">")
		    .append(isNullStr);
			
			return html.toString();
		}else if(("sequence").equalsIgnoreCase(rule)){//序列生成
			handlevalue = "";
			keyValue = "";
		}else if(("interface").equalsIgnoreCase(rule)){//接口生成
			handlevalue = "";
			keyValue = "";
		}else{//手工输入
			keyValue = "";
		}
		
	    html.append("<input type='text'  style='width:150px' name='").append(fname.toLowerCase())
	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(keyValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=").append(initLength).append(">")
	    .append(isNullStr);
		
		return html.toString();
	}
	
	public String getEditExtendHtmlContent(HttpServletRequest request,HttpServletResponse response,Map keyWords) {
//		fieldName:主键生成规则:主键的值(sequence,class,value);
		boolean readonly = this.isReadOnly(keyWords); 
		String[] fieldNames = dictatt.getDictField().split(":");
		String fname = "";
		//rule : auto sequence interface handle
		String rule = "";
		String handlevalue = "";
		if(fieldNames.length==3){
			fname = dictatt.getTable_column().toLowerCase();
			rule = fieldNames[1];
			handlevalue = fieldNames[2];
		}else if(fieldNames.length==2){
			fname = dictatt.getTable_column().toLowerCase();
			rule = fieldNames[1];
		}else{
			fname = dictatt.getTable_column().toLowerCase();
		}
		StringBuffer html = new StringBuffer();
		//最大长度
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		//是否必填
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		//生成选择时间的脚本:
		String keyValue = "";
		if("auto".equalsIgnoreCase(rule)){//自动生成 
			html.append("<input type='text' style='width:150px' name='").append(fname.toLowerCase());
			if(readonly){
				html.append("' readonly='true");
			}
		    html.append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
		    .append(keyValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
		    .append("  maxlength=").append(initLength).append(">")
		    .append(isNullStr);
			
			return html.toString();
		}else if(("sequence").equalsIgnoreCase(rule)){//序列生成
			handlevalue = "";
			keyValue = "";
		}else if(("interface").equalsIgnoreCase(rule)){//接口生成
			handlevalue = "";
			keyValue = "";
		}else{//手工输入
			keyValue = "";
		}
		
	    html.append("<input type='text'  style='width:150px' name='").append(fname.toLowerCase());
	    if(readonly){
			html.append("' readonly='true");
		}
	    html.append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(keyValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=").append(initLength).append(">")
	    .append(isNullStr);
		
		return html.toString();
	}

}
