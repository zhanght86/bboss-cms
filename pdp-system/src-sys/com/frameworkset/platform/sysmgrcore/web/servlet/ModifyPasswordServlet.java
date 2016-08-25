package com.frameworkset.platform.sysmgrcore.web.servlet;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.util.StringUtil;

public class ModifyPasswordServlet extends HttpServlet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletResponse response ;

	/**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	this.response = response; 
    	String loginName = StringUtil.replaceNull(request.getParameter("userAccount"));
      	String passWord    = StringUtil.replaceNull(request.getParameter("password"));
      	try {
			modifyPassword(loginName,passWord);
		} catch (SPIException e) {			
			e.printStackTrace();
		}
    }
    
    protected void modifyPassword(String userAccount,String newPassword) throws SPIException
    {
    	UserManager userManager = SecurityDatabase.getUserManager();
        try{
            User user = userManager.getUserByName(userAccount); 
            if(user==null){
            
            	response.getWriter().write("修改用户密码失败！系统不存在该用户！");
            	return;
            }  
            user.setUserPassword(newPassword);  
            //修改用户密码
            userManager.updateUser(user);
            //修改用户邮箱密码
            AccessControl.updateMailPassword(user.getUserEmail(),newPassword);      	
                     
         }
         catch(Exception e){
     		e.printStackTrace();
         
     		try {
				response.getWriter().write("修改用户密码失败！\n可能是数据库联接失败，请您稍候重新登陆后重新修改！");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                              
	  }
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to
     * post.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	this.response = response; 
    	String loginName = StringUtil.replaceNull(request.getParameter("loginName"));
      	String passWord    = StringUtil.replaceNull(request.getParameter("passWord"));
      	try {
			modifyPassword(loginName,passWord);
			
		} catch (SPIException e) {			
			e.printStackTrace();
		}
    }



}
