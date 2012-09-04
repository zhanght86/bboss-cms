package com.frameworkset.platform.security.authentication;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: UsernamePasswordCallbackHandler</p>
 *
 * <p>Description: 帐号密码的</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class UsernamePasswordCallbackHandler extends ACLCallbackHandler {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
    private String password; 
    private String[] userTypes = null;
    private HttpServletRequest request;
    public UsernamePasswordCallbackHandler(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    public UsernamePasswordCallbackHandler(String userName, String password,String[] userTypes,HttpServletRequest request) {
        this.userName = userName;
        this.password = password;
        this.userTypes = userTypes;
        this.request = request;
    }

    /**
     * <p> Retrieve or display the information requested in the provided
     * Callbacks.
     *
     * @param callbacks an array of <code>Callback</code> objects provided
     *   by an underlying security service which contains the information
     *   requested to be retrieved or displayed.
     * @throws IOException if an input or output error occurs. <p>
     * @throws UnsupportedCallbackException if the implementation of this
     *   method does not support one or more of the Callbacks specified in
     *   the <code>callbacks</code> parameter.
     * @todo Implement this javax.security.auth.callback.CallbackHandler
     *   method
     */
    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            Callback cb = callbacks[i];

            if (cb instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) cb;
                //System.out.print( nameCallback.getPrompt() + "? ");
                //System.out.flush();
//           String username = new BufferedReader(
//               new InputStreamReader(System.in)).readLine();
                nameCallback.setName(userName);
                //
                // Handle password aquisition
            } else if (cb instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) cb;
//           System.out.print( passwordCallback.getPrompt() + "? ");
//           System.out.flush();
//           String password = new BufferedReader(
//               new InputStreamReader(System.in)).readLine();
                passwordCallback.setPassword(password.toCharArray());
                password = null;
                //
                // Other callback types are not handled here
            }
            
            else if (cb instanceof UserTypeCallBack) {
            	UserTypeCallBack userTypeCallback = (UserTypeCallBack) cb;
//           System.out.print( passwordCallback.getPrompt() + "? ");
//           System.out.flush();
//           String password = new BufferedReader(
//               new InputStreamReader(System.in)).readLine();
            	userTypeCallback.setUserTypes(userTypes);
            	userTypes = null;
                //
                // Other callback types are not handled here
            }
            else if (cb instanceof RequestCallBack) {
            	RequestCallBack userTypeCallback = (RequestCallBack) cb;
//           System.out.print( passwordCallback.getPrompt() + "? ");
//           System.out.flush();
//           String password = new BufferedReader(
//               new InputStreamReader(System.in)).readLine();
            	userTypeCallback.setRequest(request);
            	request = null;
                //
                // Other callback types are not handled here
            }
            else {
                throw new UnsupportedCallbackException(cb,
                        "Unsupported Callback Type");
            }

        }

    }

	public String[] getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(String[] userTypes) {
		this.userTypes = userTypes;
	}
 }
