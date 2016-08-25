package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.Date;

public class NotepaperForm implements Serializable
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

}
