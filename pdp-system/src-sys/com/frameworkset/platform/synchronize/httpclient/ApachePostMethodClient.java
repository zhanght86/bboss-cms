/*
 * @class: ApachePostMethodClient
 * @version: 1.0
 * @Date: 2005/3/9 15:51:40
 */
package com.frameworkset.platform.synchronize.httpclient;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 *
 * <p>Title: ApachePostMethodClient</p>
 * <p>Description: Apache的httpclient的post进行封装的类</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: iSany</p>
 * @author
 * @version 1.0
 * @see
 */
public class ApachePostMethodClient{
	//日志文件
	Logger logger = Logger.getLogger(ApachePostMethodClient.class);

  private String url = "";
  private Properties props = null;

  private String _contentType =
      "application/x-www-form-urlencoded; charset=ISO-8859-1";

  public ApachePostMethodClient(String url, Properties props) {
    this.url = url;
    this.props = props;
  }

  public ApachePostMethodClient(String url, String method) {
    this(url, new Properties());
  }

  public ApachePostMethodClient(String url) {
    this(url, new Properties());
  }

  public String sendRequest() throws
      HttpMethodException {
    String sXml = ""; //发送的xml字符串
    String ret = ""; //返回值

    PostMethod post = new PostMethod(url);
    //post.setRequestEntity(new StringRequestEntity(sXml));
    post.setRequestBody(sXml);//pwl
    post.setRequestHeader("Content-type", _contentType);
    
    parseProperties(post);
    HttpClient httpclient = new HttpClient();

    //读入行
    try {
      int result = httpclient.executeMethod(post);
      ret = post.getResponseBodyAsString();

      //log.debug("Response status code: " + result);
      //log.debug("Response body: " + ret);

    }
    catch (Exception e) {
      throw new HttpMethodException(e.toString());
    }
    finally {
      post.releaseConnection();
    }

    return ret;
  }

  /**
   * 解释出传入的属性
   * @return String
   */
  private void parseProperties(PostMethod post) {
    if (!props.isEmpty()) {
      Enumeration enum1 = props.keys();
      int iCount = 0;

      while (enum1.hasMoreElements()) {
        String name = (String) enum1.nextElement();
        String value = props.getProperty(name);

        post.addParameter(name, value);

        iCount++;
      }
    }
  }

}
