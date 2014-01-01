/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
 
/**
 * <p>Title: InputCurrentOrgScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-17 14:58:47
 * @author ge.tao
 * @version 1.0
 */
public class CurrentOrgScript  extends BaseInputTypeScript{
	public CurrentOrgScript(DictAttachField dictatt) {
		super(dictatt);
	}

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
//
//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.input.InputTypeScript#getExtendHtmlContent(com.frameworkset.platform.dictionary.DictAttachField)
//	 */
//	public String getExtendHtmlContent(DictAttachField dictatt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	/**
	 * 根据上线问获取生成信息 当前机构 orgid
	 * @param dictatt
	 * @param request
	 * @param response
	 * @return 
	 * InputCurrentUserScript.java
	 * @author: ge.tao
	 */
//	public String getContextExtendHtml(DictAttachField dictatt,HttpServletRequest request,HttpServletResponse response){
//		String defaultValue = "";
//		String defaultName = "";
//		AccessControl accesscontroler = AccessControl.getInstance();
//		accesscontroler.checkAccess(request, response);
//		
//		//ID值
//		defaultValue = accesscontroler.getChargeOrgId()==null?"0":accesscontroler.getChargeOrgId();
//		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue().trim())){
//			defaultValue = dictatt.getFieldValue();
//		}
//		//名称值
//		if(defaultValue.trim().length() > 0){
//			OrgManagerImpl orgImpl = new OrgManagerImpl();
//			Organization org = null;
//			try {
//				org = orgImpl.getOrgById(defaultValue);
//				if(org != null){
//					defaultName = org.getRemark5();
//				}
//			} catch (ManagerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		if(defaultName.trim().length()==0){
//			defaultName = "请检查当前用户是否设置了主管机构!";
//		}
//		//最大常长度
//		int initLength = dictatt.getMaxLength();
//		if(initLength == 0){//没有设置长度 long int
//			initLength = 5;
//		}
//		//是否为空
//		String isNullStr = "";
//		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
//			isNullStr = "<span style='color:red'>必填</span>";
//		}
//		StringBuffer html = new StringBuffer()
//	    .append("<input type='hidden' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase())
//	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
//	    .append(defaultValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
//	    .append("  maxlength=").append(initLength).append(">")
//	    .append("<input type='text' style='width:150px' name='showDefaultOrgName'  value='")
//	    .append(defaultName).append("' >")
//	    .append(isNullStr);
//		return html.toString();
//	}

	public String getEditExtendHtmlContent(HttpServletRequest request, HttpServletResponse response, Map keyWords) {
		String defaultValue = "";
		String defaultName = "";
		AccessControl accesscontroler = AccessControl.getAccessControl();
		
		//ID值
		defaultValue = accesscontroler.getChargeOrgId()==null?"0":accesscontroler.getChargeOrgId();
		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue().trim())){
			defaultValue = dictatt.getFieldValue();
		}
		//名称值
		if(defaultValue.trim().length() > 0){
			OrgManagerImpl orgImpl = new OrgManagerImpl();
			Organization org = null;
			try {
				org = orgImpl.getOrgById(defaultValue);
				if(org != null){
					defaultName = org.getRemark5();
				}
			} catch (ManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(defaultName.trim().length()==0){
			defaultName = "请检查当前用户是否设置了主管机构!";
		}
		//最大常长度
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		//是否为空
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		StringBuffer html = new StringBuffer()
	    .append("<input type='hidden' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase())
	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(defaultValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=").append(initLength).append(">")
	    .append("<input type='text' readonly='true' style='width:150px' name='showDefaultOrgName'  value='")
	    .append(defaultName).append("' >")
	    .append(isNullStr);
		return html.toString();
	}

	public String getNewExtendHtmlContent(HttpServletRequest request, HttpServletResponse response) {
		String defaultValue = "";
		String defaultName = "";
		AccessControl accesscontroler = AccessControl.getAccessControl();
		//ID值
		defaultValue = accesscontroler.getChargeOrgId()==null?"0":accesscontroler.getChargeOrgId();
		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue().trim())){
			defaultValue = dictatt.getFieldValue();
		}
		//名称值
		if(defaultValue.trim().length() > 0){
			OrgManagerImpl orgImpl = new OrgManagerImpl();
			Organization org = null;
			try {
				org = orgImpl.getOrgById(defaultValue);
				if(org != null){
					defaultName = org.getRemark5();
				}
			} catch (ManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(defaultName.trim().length()==0){
			defaultName = "请检查当前用户是否设置了主管机构!";
		}
		//最大常长度
		int initLength = dictatt.getMaxLength();
		if(initLength == 0){//没有设置长度 long int
			initLength = 5;
		}
		//是否为空
		String isNullStr = "";
		if(dictatt.getIsnullable()==DictAttachField.NOTNULLABLE){
			isNullStr = "<span style='color:red'>必填</span>";
		}
		StringBuffer html = new StringBuffer()
	    .append("<input type='hidden' style='width:150px' name='").append(dictatt.getTable_column().toLowerCase())
	    .append("' style='width:150px' validator='").append(dictatt.getFieldValidType()).append("' value='")
	    .append(defaultValue).append("' cnname='").append(dictatt.getDictFieldName()).append("' ")
	    .append("  maxlength=").append(initLength).append(">")
	    .append("<input type='text' readonly='true' style='width:150px' name='showDefaultOrgName'  value='")
	    .append(defaultName).append("' >")
	    .append(isNullStr);
		return html.toString();
	}

}
