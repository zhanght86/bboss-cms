/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.platform.dictionary.DictAttachField;

/**
 * <p>Title: InputCurrentTimeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-17 14:58:28
 * @author ge.tao
 * @version 1.0
 */
public class CurrentTimeScript  extends BaseInputTypeScript{

	public CurrentTimeScript(DictAttachField dictatt) {
		super(dictatt);
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getFunctionScript(java.lang.String, java.lang.String, java.lang.String, int)
	 */
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

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 */
//	public String getExtendHtmlContent(DictAttachField dictatt) {
//		String defaultValue = "";
//		Date currentDate = new Date();
//		Format format = new SimpleDateFormat(dictatt.getDateFormat());
//		defaultValue = format.format(currentDate);
//		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue())){
//			defaultValue = dictatt.getFieldValue();
//		}
//		int initLength = dictatt.getMaxLength();
//		if(initLength == 0){//没有设置长度 long int
//			initLength = 5;
//		}
//		String isNullStr = "";
//		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
//			isNullStr = "<span style='color:red'>必填</span>";
//		}
//		StringBuffer html = new StringBuffer()
//	    .append("<input type='text' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase())
//	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
//	    .append(defaultValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
//	    .append("  maxlength=100 ").append(">").append(isNullStr);
//		return html.toString();
//	}

	public String getEditExtendHtmlContent(HttpServletRequest request, HttpServletResponse response, Map keyWords) {
		boolean readonly = this.isReadOnly(keyWords);
		String defaultValue = "";
		Date currentDate = new Date();
		Format format = DataFormatUtil.getSimpleDateFormat(dictatt.getDateFormat());
		defaultValue = format.format(currentDate);
		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue())){
			defaultValue = dictatt.getFieldValue();
		}
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		StringBuffer html = new StringBuffer()
	    .append("<input type='text' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase());
		if(readonly){
			html.append("' readonly='true");
		}
	    html.append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(defaultValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=100 ").append(">").append(isNullStr);
		return html.toString();
	}

	public String getNewExtendHtmlContent(HttpServletRequest request, HttpServletResponse response) {
		String defaultValue = "";
		Date currentDate = new Date();
		Format format = DataFormatUtil.getSimpleDateFormat(request,dictatt.getDateFormat());
		defaultValue = format.format(currentDate);
		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue())){
			defaultValue = dictatt.getFieldValue();
		}
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		StringBuffer html = new StringBuffer()
	    .append("<input type='text'  readonly='true' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase())
	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(defaultValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=100 ").append(">").append(isNullStr);
		return html.toString();
	}

}
