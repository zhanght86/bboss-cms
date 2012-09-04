package com.frameworkset.platform.security.authentication;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.security.auth.RefreshFailedException;
import javax.security.auth.Refreshable;
import javax.security.auth.Subject;

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
public class Credential implements Refreshable,Destroyable,java.io.Serializable{
    private Subject subject;
    private String loginModule;
    private CheckCallBack checkCallBack;
    public Credential(CheckCallBack checkCallBack,String loginModule,Subject subject)
    {
        this.checkCallBack = checkCallBack;
        this.loginModule = loginModule;
        this.subject = subject;
    }

    public void destroy() throws DestroyFailedException {
    }

    public boolean isDestroyed() {
        return false;
    }

    public boolean isCurrent() {
        return true;
    }

    public void refresh() throws RefreshFailedException {
    }

    public CheckCallBack getCheckCallBack() {
        return checkCallBack;
    }

    public String getLoginModule() {
        return loginModule;
    }

    public Subject getSubject() {
        return subject;
    }

}
