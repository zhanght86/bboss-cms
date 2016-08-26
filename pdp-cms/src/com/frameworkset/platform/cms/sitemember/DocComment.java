//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemember\\DocComment.java

package com.frameworkset.platform.cms.sitemember;

import java.util.Date;


/**
 * 文档评论
 * 包括用户信息/ip等,
 */
public class DocComment implements java.io.Serializable
{
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 评论者ip
     */
    private String ip;
    
    /**
     * 评论时间
     */
    private Date commontTime;
    
    /**
     * 状态标志位
     * 控制评论是否可发布
     * 
     * 通过审核修改评论状态标识位
     */
    private int status;
    private String desc;
    
    /**
     * @since 2006.12
     */
    public DocComment() 
    {
     
    }
    
    /**
     * Access method for the content property.
     * 
     * @return   the current value of the content property
     */
    public String getContent() 
    {
        return content;     
    }
    
    /**
     * Sets the value of the content property.
     * 
     * @param aContent the new value of the content property
     */
    public void setContent(String aContent) 
    {
        content = aContent;     
    }
    
    /**
     * Access method for the ip property.
     * 
     * @return   the current value of the ip property
     */
    public String getIp() 
    {
        return ip;     
    }
    
    /**
     * Sets the value of the ip property.
     * 
     * @param aIp the new value of the ip property
     */
    public void setIp(String aIp) 
    {
        ip = aIp;     
    }
    
    /**
     * Access method for the commontTime property.
     * 
     * @return   the current value of the commontTime property
     */
    public Date getCommontTime() 
    {
        return commontTime;     
    }
    
    /**
     * Sets the value of the commontTime property.
     * 
     * @param aCommontTime the new value of the commontTime property
     */
    public void setCommontTime(Date aCommontTime) 
    {
        commontTime = aCommontTime;     
    }
    
    /**
     * Access method for the status property.
     * 
     * @return   the current value of the status property
     */
    public int getStatus() 
    {
        return status;     
    }
    
    /**
     * Sets the value of the status property.
     * 
     * @param aStatus the new value of the status property
     */
    public void setStatus(int aStatus) 
    {
        status = aStatus;     
    }
    
    /**
     * Access method for the desc property.
     * 
     * @return   the current value of the desc property
     */
    public String getDesc() 
    {
        return desc;     
    }
    
    /**
     * Sets the value of the desc property.
     * 
     * @param aDesc the new value of the desc property
     */
    public void setDesc(String aDesc) 
    {
        desc = aDesc;     
    }
}
