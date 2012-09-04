package com.frameworkset.platform.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.frameworkset.platform.config.model.LoginModuleInfo;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class LoginModuleInfoQueue implements java.io.Serializable {
    private List loginModuleQueue = new ArrayList();
    public static void main(String[] args) {
        LoginModuleInfoQueue loginmodulequeue = new LoginModuleInfoQueue();
    }

    public void addLoginModuleInfo(LoginModuleInfo loginModuleInfo)
    {
        this.loginModuleQueue.add(loginModuleInfo);
    }

    public LoginModuleInfo getLoginModuleInfo(int i)
    {
        return (LoginModuleInfo)this.loginModuleQueue.get(i);
    }

    public int size()
    {
        return loginModuleQueue.size();
    }

    public Iterator iterator()
    {
        return loginModuleQueue.iterator();
    }



}
