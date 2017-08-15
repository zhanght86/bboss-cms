/**
 * 
 */
package com.frameworkset.platform.dictionary;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.input.DateTypeScript;
import com.frameworkset.platform.dictionary.input.DictTypeScript;
import com.frameworkset.platform.dictionary.input.InputType;
import com.frameworkset.platform.dictionary.input.InputTypeScript;
import com.frameworkset.platform.dictionary.input.PrimaryKeyTypeScript;
import com.frameworkset.common.poolman.DBUtil;

/**
 * <p>Title: FormHelper.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-15 11:17:16
 * @author ge.tao
 * @version 1.0
 */
public class FormHelper implements java.io.Serializable {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public void init(HttpServletRequest request,HttpServletResponse response)
	{
		this.request = request;
		this.response = response; 
	}
	
	

	
	

}
