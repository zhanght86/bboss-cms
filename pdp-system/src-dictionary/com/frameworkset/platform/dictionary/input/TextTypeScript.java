/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;

/**
 * <p>Title: TextTypeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-15 17:24:13
 * @author ge.tao
 * @version 1.0
 */
public class TextTypeScript extends BaseInputTypeScript{

//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getFunctionScript(java.lang.String, java.lang.String, java.lang.String, int)
//	 */
//	public String getFunctionScript(String fiedName, String inputTypeName, String value, int maxLength) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getFunctionContent(java.lang.String, java.lang.String, java.lang.String, int)
//	 */
//	public String getFunctionContent(String fiedName, String inputTypeName, String value, int maxLength) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public TextTypeScript(DictAttachField dictatt) {
		super(dictatt);
	}

//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(java.lang.String, java.lang.String, java.lang.String, int)
//	 */
//	public String getExtendHtmlContent(String fieldName, String inputTypeName, String value, int maxLength) {
//		//String extendStr = "name='" + dictatt.getDictField()+ "' style='width:150px' " +
//        //" validator='" + fieldValidType + "' value='' cnname='" + dictatt.getDictFieldName() + "'";
//		return null;
//	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 * 没有赋值
	 */
	public String getNewExtendHtmlContent(HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		String isNullStr = "";
		String isUniqueCheckStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		if(dictatt.getIsunique() == DictAttachField.UNIQUE){
			isUniqueCheckStr = " <span style='color:red'>自动去重校验</span>";
		}
		StringBuffer html = new StringBuffer()
	    .append("<input type='text' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase())
	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(dictatt.getFieldValue()).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=").append(initLength).append(" ");
		//校验唯一性
		if(dictatt.getIsunique() == DictAttachField.UNIQUE){
			html.append(" onchange=\"send_request(this,'").append(dictatt.getDicttypeId()).append("')\" ");
		}
	    html.append(">")
	    .append(isNullStr).append(isUniqueCheckStr);
	    //System.out.println(html.toString());
		return html.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 * 没有赋值
	 */
	public String getEditExtendHtmlContent(HttpServletRequest request,HttpServletResponse response,Map keyWords) {
		// TODO Auto-generated method stub
		int initLength = dictatt.getMaxLength();
		boolean readonly = this.isReadOnly(keyWords);
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		String isNullStr = "";
		String isUniqueCheckStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		if(dictatt.getIsunique() == DictAttachField.UNIQUE){
			isUniqueCheckStr = " <span style='color:red'>自动去重校验</span>";
		}
		StringBuffer html = new StringBuffer()
	    .append("<input type='text' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase());
		if(readonly){
			html.append("' readonly='true");
		}
	    html.append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(dictatt.getFieldValue()).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=").append(initLength).append(" ");
		//校验唯一性
		if(dictatt.getIsunique() == DictAttachField.UNIQUE){
			html.append(" onchange=\"send_request_update(this,'").append(dictatt.getDicttypeId())
				.append("','").append(dictatt.getFieldValue()).append("')\" ");
		}
	    html.append(">")
	    .append(isNullStr).append(isUniqueCheckStr);
		return html.toString();
	}

}
