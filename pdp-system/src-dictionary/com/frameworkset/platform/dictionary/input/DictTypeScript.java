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
 * 输出选择字典类型的脚本
 * <p>Title: DictTypeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-15 9:15:15
 * @author ge.tao
 * @version 1.0
 */
public class DictTypeScript  extends BaseInputTypeScript{
	
	public DictTypeScript(DictAttachField dictatt) {
		super(dictatt);
	}
 
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getFunctionScript(java.lang.String, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent()
	 */
//	public String getExtendHtmlContent(String fieldName,String inputTypeName,String value,int maxLength) {
//		return null;
//	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 * 没有赋值
	 */
//	public String getExtendHtmlContent(DictAttachField dictatt) {
//		String[] fieldNames = dictatt.getDictField().split(":");
//		String fname = "";
//		String dtypeId = "";
//		String opcode = "";
//		if(fieldNames.length==3){
//			fname = fieldNames[0];
//			dtypeId = fieldNames[1];
//			opcode = fieldNames[2];
//		}
//		//生成一个select框,调用标签
//		StringBuffer html = new StringBuffer();
//		//字典类型下所有的字典数据:
//		List dictdatas = null;
//		try {
//			dictdatas = dictManager.getDictdataList(dtypeId);
//		} catch (ManagerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}					
//		if("read".equalsIgnoreCase(opcode)){//权限过滤
//			html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' >");
//		}else if("usual".equalsIgnoreCase(opcode)){//做权限过滤
//			html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' >");
//		}else{//不做权限过滤
//			html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' >");						
//		}
//		for(int j=0;dictdatas != null && j<dictdatas.size();j++){
//			Item dictdata = (Item)dictdatas.get(j);
//			html.append("<option value='")
//			.append(dictdata.getItemId()).append("'>").append(dictdata.getName()).append("</option>");
//		}
//		html.append("</select>");
//		if(dictatt.getFieldValue()!=null){
//			html.append("<script languaget='javascript'>")
//			    .append("var v = '").append(dictatt.getFieldValue()).append("';  ")
//			    .append("if(document.all.").append(dictatt.getTable_column().toLowerCase()).append("){")
//				.append("document.all.").append(dictatt.getTable_column().toLowerCase())
//				.append(".value = v")
//				.append("}");			
//			html.append("</script>");
//		}
//		return html.toString();
//	}

	public String getEditExtendHtmlContent(HttpServletRequest request, HttpServletResponse response, Map keyWords) {
		String[] fieldNames = dictatt.getDictField().split(":");
		boolean readonly = this.isReadOnly(keyWords);
		String fname = "";
		String dtypeId = "";
		String opcode = "";
		if(fieldNames.length==3){
			fname = fieldNames[0];
			dtypeId = fieldNames[1];
			opcode = fieldNames[2];
		}
		//生成一个select框,调用标签
		StringBuffer html = new StringBuffer();
		//字典类型下所有的字典数据:
		List dictdatas = null;
		try {
			dictdatas = dictManager.getDictdataList(dtypeId);
		} catch (ManagerException e) {
			e.printStackTrace();
		}					
		if(readonly){
			html.append("<input name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' ")
			.append("value='").append(dictatt.getFieldValue()).append("' readonly='true' >");
		}else{
			if("read".equalsIgnoreCase(opcode)){//权限过滤
				html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' >");
			}else if("usual".equalsIgnoreCase(opcode)){//做权限过滤
				html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' >");
			}else{//不做权限过滤
				html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' >");						
			}
			for(int j=0;dictdatas != null && j<dictdatas.size();j++){
				Item dictdata = (Item)dictdatas.get(j);
				html.append("<option value='")
				.append(dictdata.getItemId()).append("'>").append(dictdata.getName()).append("</option>");
			}
			html.append("</select>");
			if(dictatt.getFieldValue()!=null){
				html.append("<script languaget='javascript'>")
				    .append("var v = '").append(dictatt.getFieldValue()).append("';  ")
				    .append("if(document.all.").append(dictatt.getTable_column().toLowerCase()).append("){")
					.append("document.all.").append(dictatt.getTable_column().toLowerCase())
					.append(".value = v")
					.append("}");			
				html.append("</script>");
			}
		}
		return html.toString();
	}

	public String getNewExtendHtmlContent(HttpServletRequest request, HttpServletResponse response) {
		String isNullStr = "";
		String isUniqueCheckStr = "";
		boolean isNull = dictatt.getIsnullable()==DictAttachField.NOTNULLABLE;
		boolean isUnique = dictatt.getIsunique() == DictAttachField.UNIQUE;
		if(isNull){
			isNullStr = "<span style='color:red'>必选</span>";
		}
		if(isUnique){
			isUniqueCheckStr = " <span style='color:red'>自动去重校验</span>";
		}
		String[] fieldNames = dictatt.getDictField().split(":");
		String fname = "";
		String dtypeId = "";
		String opcode = "";
		if(fieldNames.length==3){
			fname = fieldNames[0];
			dtypeId = fieldNames[1];
			opcode = fieldNames[2];
		}
		//生成一个select框,调用标签
		StringBuffer html = new StringBuffer();
		//字典类型下所有的字典数据:
		List dictdatas = null;
		try {
			dictdatas = dictManager.getDictdataList(dtypeId);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		if("read".equalsIgnoreCase(opcode)){//权限过滤
			html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' ");
			//校验唯一性-- || 必选
			if(isUnique){
				html.append(" onchange=\"send_requestSelect(this,'")
					.append(isNull).append("','").append(isUnique)
					.append("','").append(dictatt.getDicttypeId()).append("')\" ");
			}
			html.append(">");
		}else if("usual".equalsIgnoreCase(opcode)){//做权限过滤
			html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' ");
			//校验唯一性-- || 必选
			if(isUnique){
				html.append(" onchange=\"send_requestSelect(this,'")
					.append(isNull).append("','").append(isUnique)
					.append("','").append(dictatt.getDicttypeId()).append("')\" ");
			}
			html.append(">");
		}else{//不做权限过滤
			html.append("<select name='").append(dictatt.getTable_column().toLowerCase()).append("' style='width:150px' ");
			//校验唯一性-- || 必选
			if(isUnique){
				html.append(" onchange=\"send_requestSelect(this,'")
					.append(isNull).append("','").append(isUnique)
					.append("','").append(dictatt.getDicttypeId()).append("')\" ");
			}
			
			html.append(">");
		}
		
		if(!isNull){
			html.append("<option value='' selected>请选择</option>");
		}
		for(int j=0;dictdatas != null && j<dictdatas.size();j++){
			Item dictdata = (Item)dictdatas.get(j);
			html.append("<option value='")
			.append(dictdata.getItemId()).append("'>").append(dictdata.getName()).append("</option>");
		}
		html.append("</select>").append(isNullStr).append(isUniqueCheckStr);
//		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue())){
//			html.append("<script languaget='javascript'>")
//			    .append("var v = '").append(dictatt.getFieldValue()).append("';  ")
//			    .append("if(document.all.").append(dictatt.getTable_column().toLowerCase()).append("){")
//				.append("document.all.").append(dictatt.getTable_column().toLowerCase())
//				.append(".value = v")
//				.append("}");			
//			html.append("</script>");
//		}
		return html.toString();
	}
}
