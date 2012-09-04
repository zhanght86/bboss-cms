//Source file: D:\\WorkSpace\\console\\src\\com\\frameworkset\\platform\\schedularmanage\\MeetingTaskNotic.java

package com.frameworkset.platform.schedularmanage;

import java.io.Serializable;
import java.util.Date;

/**
 * 便笺
 */
public class Notepaper implements Serializable
{
    /**
     * 便笺编号
     */
    private int notepaparID;
    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private Date time;

    
    /**
     * 用户id
     */
    private int userID;

     
    public Notepaper()
    {

    }


    public String getContent()
    {
        return content;
    }


    public void setContent(String content)
    {
        this.content = content;
    }


    public int getNotepaparID()
    {
        return notepaparID;
    }


    public void setNotepaparID(int notepaparID)
    {
        this.notepaparID = notepaparID;
    }


    public Date getTime()
    {
        return time;
    }


    public void setTime(Date time)
    {
        this.time = time;
    }


    public int getUserID()
    {
        return userID;
    }


    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    /**
     * Access method for the content property.
     * 
     * @return the current value of the content property
     */

}
