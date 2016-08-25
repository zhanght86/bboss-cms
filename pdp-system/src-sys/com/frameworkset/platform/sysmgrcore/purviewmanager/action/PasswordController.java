/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.spi.SPIException;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.util.RandImgCreater;
import com.frameworkset.util.StringUtil;
import com.liferay.portlet.iframe.action.WebDes;

/**
 * <p>Title: PasswordController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008-2010</p>
 * @Date 2013-6-15
 * @author biaoping.yin
 * @version 1.0
 */
public class PasswordController {
	private  String[] codelist = RandImgCreater.CODE_LIST;
	private  int codenum = RandImgCreater.FONT_NUM;
	public void generateImageCode(HttpServletRequest request,HttpServletResponse response)
	{
		String codekey  = "rand";
		HttpSession session = request.getSession(true);
		RandImgCreater rc = new RandImgCreater(response,codenum,codelist);
		String rand = rc.createRandImage();
		session.setAttribute(codekey,rand);
	
	}
	public @ResponseBody String modifyExpiredPassword(String loginName,String oldpassword,String passWord,String passWordConfirm)
	{
		try {
			WebDes wd = new WebDes();
			if(oldpassword != null && !oldpassword.equals(""))
				oldpassword = wd.strDec(oldpassword, loginName, "", "");
			if(passWord != null && !passWord.equals(""))
				passWord = wd.strDec(passWord, loginName, "", "");
			if(passWordConfirm != null && !passWordConfirm.equals(""))
				passWordConfirm = wd.strDec(passWordConfirm, loginName, "", "");
			UserManager userManager = SecurityDatabase.getUserManager();       
			User user = userManager.getUserByName(loginName);
			if(user == null)
			{
				return "用户"+loginName+"不存在";
			}
			if(!EncrpyPwd.encodePassword(oldpassword).equals(user.getUserPassword()))
				return "你输入的旧密码不对";
			user.setUserPassword(passWord);
			userManager.updateUserPassword(user);
			return "success";
		} catch (SPIException e) {
			return StringUtil.formatBRException(e);
		} catch (ManagerException e) {
			return e.getMessage();
		}
		catch (Exception e) {
			return StringUtil.formatBRException(e);
		} 
		
	}

}
