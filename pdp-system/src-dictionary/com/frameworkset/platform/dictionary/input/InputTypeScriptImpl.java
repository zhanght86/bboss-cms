package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl;
/**
 * 选择机构 选择人员
 * <p>Title: InputTypeScriptImpl.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-15 18:35:09
 * @author gao.tang
 * @version 1.0
 */
public class InputTypeScriptImpl extends BaseInputTypeScript {

	
	public InputTypeScriptImpl(DictAttachField dictatt) {
		super(dictatt);
	}

	public String getFunctionScript(String fiedName, String inputTypeName,String value,int maxLength) {
		return null;
	}
	//html.append(this.getFunctionContent(dictatt.getTable_column(),dictatt.getInputTypeName(),dictatt.getFieldValue(),dictatt.getMaxLength(),isUnique,dictatt.getDicttypeId()));
//	public String getFunctionContent(String fiedName, String inputTypeName,String value,int maxLength,boolean isUnique,String dicttypeId) {
	public String getFunctionContent(String orgoruserName,boolean isUnique) {
		StringBuffer functionContent = new StringBuffer();
		String fiedName = dictatt.getTable_column();
		String inputTypeName = dictatt.getInputTypeName();
		String dicttypeId = dictatt.getDicttypeId();
		if(inputTypeName.equals("选择机构")){
			functionContent.append("<script language='javascript'>")
				.append("function ").append(fiedName.toLowerCase()).append("_(obj){")
				.append("var oldValue = '").append(orgoruserName).append("';")
				.append("var valueWin = window.showModalDialog('orgSelectTree.jsp?")
				.append("orgNames='+obj.value,window,'dialogWidth:'+(400)+'px;dialogHeight:'+(600)+'px;help:no;scroll:auto;status:no');")
				.append("if(valueWin!=null){var restr = valueWin; obj.value = valueWin.split('\\^')[0]; document.all.")
				.append(fiedName.toLowerCase()).append(".value=valueWin.split('\\^')[1];}");
			if(isUnique){
				functionContent.append("if(valueWin && oldValue!=valueWin.split('\\^')[0])send_request_name(document.all.")
					.append(fiedName.toLowerCase()).append(",document.all.").append(fiedName.toLowerCase()+"_name")
					.append(",'").append(dicttypeId).append("'); ");
			}
			functionContent.append("}</script>");
		}
		if(inputTypeName.equals("选择人员")){
			functionContent.append("<script language='javascript'>")
				.append("function ").append(fiedName.toLowerCase()).append("_(obj){")
				.append("var oldValue = '").append(orgoruserName).append("';")
				.append("var valueWin = window.showModalDialog('selectexecutor.jsp?displayNameInput=partner&displayValueInput=executors&userNames='+obj.value,window,")
				.append("'dialogWidth:'+(800)+'px;dialogHeight:'+(600)+'px;help:no;scroll:auto;status:no');")
				.append("if(valueWin!=null){var restr = valueWin;obj.value = valueWin.split('\\^')[0]; document.all.")
				.append(fiedName.toLowerCase()).append(".value=valueWin.split('\\^')[1]; }");
				if(isUnique){
					functionContent.append("if(valueWin && oldValue!=valueWin.split('\\^')[0])send_request_name(document.all.")
						.append(fiedName.toLowerCase()).append(",document.all.").append(fiedName.toLowerCase()+"_name")
						.append(",'").append(dicttypeId).append("'); ");
				}
			functionContent.append("}</script>");
		}
		return functionContent.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 * 没有赋值
	 */
	public String getNewExtendHtmlContent(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer html = new StringBuffer();		
		String isNullStr = "";
		String isUniqueCheckStr = "";
		//校验唯一性
		boolean isUnique = dictatt.getIsunique() == DictAttachField.UNIQUE;
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		if(isUnique){
			isUniqueCheckStr = " <span style='color:red'>自动去重校验</span>";
		}
			
		html.append("<input type='hidden' id='").append(dictatt.getTable_column().toLowerCase())
			.append("' name='").append(dictatt.getTable_column().toLowerCase())
			.append("' value='").append(dictatt.getFieldValue()).append("' ");
		html.append("> ");
		String orgoruserName = "";
		if(dictatt.getInputTypeName().equals("选择机构")){
			//String orgName = "";
			String orgId = dictatt.getFieldValue();
			Organization organization = null;
			try {
				organization = OrgCacheManager.getInstance().getOrganization(orgId);
				if(organization!=null){
					orgoruserName = orgId + " " + organization.getRemark5();
				}
			} catch (ManagerException e) {
				e.printStackTrace();
			}
			
			html.append("<input type='text' id='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("' name='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("' ")
			.append("value='").append(orgoruserName).append("'  maxlength=100 ")
			.append(" validator='").append(dictatt.getFieldValidType())
			.append("'  cnname='")
			.append(dictatt.getDictFieldName()).append("' ");
			
			
//			html.append(" onClick='").append(dictatt.getTable_column().toLowerCase()).append("_(this);' readonly='true' style='width:200px' ")
//				.append("/>");
			//orgSelectFinal(ifid,windowname,fieldName,isUnique,fileTextName)
			html.append(" onClick='$.dictionary.orgSelectFinal(\"").append(request.getContextPath()).append("\",\"iframe_").append(dictatt.getTable_column()).append("\",\"选择机构\",\"").append(dictatt.getTable_column().toLowerCase()).append("\",")
			.append(isUnique)
			.append(",\"")
			.append(dictatt.getTable_column().toLowerCase()).append("_name\")' readonly='true' style='width:200px' ")
			.append("/>");
		}
		if(dictatt.getInputTypeName().equals("选择人员")){
			//String userName = "";
			String userId = dictatt.getFieldValue();
			User user = null;
			UserManagerImpl userImpl = new UserManagerImpl();
			try {				
				user = userImpl.getUserById(userId);
				if(user != null){
					orgoruserName = userId + " " + user.getUserRealname();
				}
			} catch (ManagerException e) {
				e.printStackTrace();
			}
			
			html.append("<input type='text' id='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("' name='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("' ")
			.append("value='").append(orgoruserName).append("'  maxlength=100 ")
			.append(" validator='").append(dictatt.getFieldValidType())
			.append("'  cnname='")
			.append(dictatt.getDictFieldName()).append("' ");
			html.append(" onClick='$.dictionary.userSelectFinal(\"").append(request.getContextPath()).append("\",\"iframe_").append(dictatt.getTable_column()).append("\",\"选择人员\",\"").append(dictatt.getTable_column().toLowerCase()).append("\",")
			.append(isUnique)
			.append(",\"")
			.append(dictatt.getTable_column().toLowerCase()).append("_name\")' readonly='true' style='width:200px' ")
				.append("/>");
		}
		html.append(isNullStr).append(isUniqueCheckStr);
		//html.append(this.getFunctionContent(dictatt.getTable_column(),dictatt.getInputTypeName(),dictatt.getFieldValue(),dictatt.getMaxLength(),isUnique,dictatt.getDicttypeId()));
//		html.append(getFunctionContent(orgoruserName,isUnique));
		//System.out.println(html.toString());
		return html.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
	 * 没有赋值
	 */
	public String getEditExtendHtmlContent(HttpServletRequest request, HttpServletResponse response, Map keyWords) {
		StringBuffer html = new StringBuffer();		
		String isNullStr = "";
		String isUniqueCheckStr = "";
		boolean readonly = this.isReadOnly(keyWords); 
		DictManager dictManager = new DictManagerImpl();
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		//校验唯一性
		boolean isUnique = dictatt.getIsunique() == DictAttachField.UNIQUE;
		if(isUnique){
			isUniqueCheckStr = " <span style='color:red'>自动去重校验</span>";
		}	
		html.append("<input type='hidden' id='").append(dictatt.getTable_column().toLowerCase()).append("'  name='").append(dictatt.getTable_column().toLowerCase())
			.append("' value='").append(dictatt.getFieldValue()).append("'>");
		
		String orgoruserName = "";
		if(dictatt.getInputTypeName().equals("选择机构")){
			//String orgName = "";
			String orgId = dictatt.getFieldValue();
//			Organization organization = null;
//			try {
//				organization = OrgCacheManager.getInstance().getOrganization(orgId);
//				if(organization!=null){
//					orgoruserName = orgId + " " + organization.getRemark5();
//				}
//			} catch (ManagerException e) {
//				e.printStackTrace();
//			}
			try {
				orgoruserName = dictManager.getOrgNames(orgId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			html.append("<input type='text' id='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("'  name='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("' ")
			.append("value='").append(orgoruserName).append("'  maxlength=100 ")
			.append(" validator='").append(dictatt.getFieldValidType())
			.append("'  cnname='")
			.append(dictatt.getDictFieldName()).append("' ");
			if(!readonly){
				html.append(" onClick='$.dictionary.orgSelectFinal(\"").append(request.getContextPath()).append("\",\"iframe_").append(dictatt.getTable_column()).append("\",\"选择机构\",\"").append(dictatt.getTable_column().toLowerCase()).append("\",")
				.append(isUnique)
				.append(",\"")
				.append(dictatt.getTable_column().toLowerCase()).append("_name\")'");
			}
			html.append(" readonly='true' style='width:200px' ")
			.append("/>");
		}
		if(dictatt.getInputTypeName().equals("选择人员")){
			//String userName = "";
			String userId = dictatt.getFieldValue();
//			User user = null;
//			UserManagerImpl userImpl = new UserManagerImpl();
//			try {				
//				user = userImpl.getUserById(userId);
//				if(user != null){
//					orgoruserName = userId + " " + user.getUserRealname();
//				}
//			} catch (ManagerException e) {
//				e.printStackTrace();
//			}
			try {
				orgoruserName = dictManager.getUserNames(userId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			html.append("<input type='text' id='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("'  name='").append(dictatt.getTable_column().toLowerCase()).append("_name").append("' ")
			.append("value='").append(orgoruserName).append("'  maxlength=100 ")
			.append(" validator='").append(dictatt.getFieldValidType())
			.append("'  cnname='")
			.append(dictatt.getDictFieldName()).append("' ");
			if(!readonly){
				html.append(" onClick='$.dictionary.userSelectFinal(\"").append(request.getContextPath()).append("\",\"iframe_").append(dictatt.getTable_column()).append("\",\"选择人员\",\"").append(dictatt.getTable_column().toLowerCase()).append("\",")
			.append(isUnique)
			.append(",\"")
			.append(dictatt.getTable_column().toLowerCase()).append("_name\")' ");
			}
			html.append(" readonly='true' style='width:200px' ")
				.append("/>");
		}
		html.append(isNullStr).append(isUniqueCheckStr);
//		html.append(getFunctionContent(orgoruserName,isUnique));
		return html.toString();
	}
}
