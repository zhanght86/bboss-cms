/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl;
 
/**
 * <p>Title: InputCurrentUserScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-17 14:58:40
 * @author ge.tao
 * @version 1.0
 */
public class CurrentUserScript  extends BaseInputTypeScript{

	public CurrentUserScript(DictAttachField dictatt) {
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
//	
	/**
	 * 根据上线问获取生成信息 当前用户 userid
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
//		defaultValue = accesscontroler.getUserID()==null?"1":accesscontroler.getUserID();
//	
//		//ID值
//		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue().trim())){
//			defaultValue = dictatt.getFieldValue();
//		}
//		//名称值
//		if(defaultValue.trim().length() > 0){
//			UserManagerImpl u = new UserManagerImpl();
//			User user = null ;
//			try {
//				user = u.getUserById(defaultValue);
//				if(user != null){
//					defaultName = user.getUserRealname();
//				}
//			} catch (ManagerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		//最大长度
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
//	    .append("<input type='text' style='width:150px' name='showDefaultUserName' value='")
//	    .append(defaultName).append("'>")
//	    .append(isNullStr);
//		return html.toString();
//	}

	public String getEditExtendHtmlContent(HttpServletRequest request, HttpServletResponse response, Map keyWords) {
		boolean readonly = this.isReadOnly(keyWords);
		String defaultValue = "";
		String defaultName = "";
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		defaultValue = accesscontroler.getUserID()==null?"1":accesscontroler.getUserID();
	
		//ID值
		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue().trim())){
			defaultValue = dictatt.getFieldValue();
		}
		//名称值
		if(defaultValue.trim().length() > 0){
			UserManagerImpl u = new UserManagerImpl();
			User user = null ;
			try {
				user = u.getUserById(defaultValue);
				if(user != null){
					defaultName = user.getUserRealname();
				}
			} catch (ManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//最大长度
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
	    .append("<input type='text' style='width:150px' name='showDefaultUserName' value='")
	    .append(defaultName);
		if(readonly){
			html.append("' readonly='true");
		}
		html.append("'>")
	    .append(isNullStr);
		return html.toString();
	}

	public String getNewExtendHtmlContent(HttpServletRequest request, HttpServletResponse response) {
		String defaultValue = "";
		String defaultName = "";
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		defaultValue = accesscontroler.getUserID()==null?"1":accesscontroler.getUserID();
	
		//ID值
		if(dictatt.getFieldValue()!=null && !"".equals(dictatt.getFieldValue().trim())){
			defaultValue = dictatt.getFieldValue();
		}
		//名称值
		if(defaultValue.trim().length() > 0){
			UserManagerImpl u = new UserManagerImpl();
			User user = null ;
			try {
				user = u.getUserById(defaultValue);
				if(user != null){
					defaultName = user.getUserRealname();
				}
			} catch (ManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//最大长度
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
	    .append("<input type='text'  readonly='true' style='width:150px' name='showDefaultUserName' value='")
	    .append(defaultName).append("'>")
	    .append(isNullStr);
		return html.toString();
	}


}
