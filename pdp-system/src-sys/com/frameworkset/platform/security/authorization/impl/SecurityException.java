package com.frameworkset.platform.security.authorization.impl;

import java.io.Serializable;

/**
 * <p>Title: SecurityException</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class SecurityException extends Exception implements Serializable{

    /**
     * @param string
     */
    public SecurityException(String msg) {
        super(msg);
        
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param Exception
     * add by 20080721  gao.tang
     */
    public SecurityException(Exception e ) {
        super(e);
        
        // TODO Auto-generated constructor stub
    }

}
