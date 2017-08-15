package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.frameworkset.spi.ProviderInterceptor;

/**
 * <p>Title: </p>
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
public class UserManagerInterceptor extends ProviderInterceptor implements Serializable {

    /**
     * after
     *
     * @param method Method
     * @param objectArray Object[]
     * @todo Implement this com.frameworkset.proxy.Interceptor method
     */
    public void after(Method method, Object[] objectArray) {
    }

    /**
     * afterFinally
     *
     * @param method Method
     * @param objectArray Object[]
     * @todo Implement this com.frameworkset.proxy.Interceptor method
     */
    public void afterFinally(Method method, Object[] objectArray) {
    }

    /**
     * afterThrowing
     *
     * @param method Method
     * @param objectArray Object[]
     * @todo Implement this com.frameworkset.proxy.Interceptor method
     */
    public void afterThrowing(Method method, Object[] objectArray) {
    }
    
    /**
     * afterThrowing
     *
     * @param method Method
     * @param objectArray Object[]
     * @todo Implement this com.frameworkset.proxy.Interceptor method
     */
    public void afterThrowing(Method method, Object[] objectArray,Throwable throwable) {
    }

    /**
     * before
     * 
     * @param method Method
     * @param objectArray Object[]
     * @todo Implement this com.frameworkset.proxy.Interceptor method
     */
    public void before(Method method, Object[] objectArray) {
    }




}
