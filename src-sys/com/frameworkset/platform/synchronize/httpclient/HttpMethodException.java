/*
 * @class:HttpMethodException
 * @version: 1.0
 * @Date: 2004/12/15 17:08:40
 */
package com.frameworkset.platform.synchronize.httpclient;

import java.io.Serializable;

/**
 *
 * <p>Title: HttpMethodException</p>
 * <p>Description: 做http method异常</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: iSany</p>
 * @author hui.deng
 * @version 1.0
 * @see Exception
 */
class HttpMethodException
    extends Exception 
    implements Serializable{
  public HttpMethodException() {
    super();
  }

  public HttpMethodException(String s) {
    super(s);
  }
}
