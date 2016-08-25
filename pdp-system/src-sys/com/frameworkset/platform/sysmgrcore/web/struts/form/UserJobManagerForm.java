package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author feng.jing
 * @version 1.0
 */
public class UserJobManagerForm implements Serializable{
    public UserJobManagerForm() {
    }

    private String userId = "";
    private List allJob = null;
    private List existJob = null;
    private String[] jobId;
    private String orgId;


    public List getAllJob() {
        return allJob;
    }

    public List getExistJob() {
        return existJob;
    }

    public String getUserId() {
        return userId;
    }

    public String[] getJobId() {
        return jobId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setExistJob(List existJob) {
        this.existJob = existJob;
    }

    public void setAllJob(List allJob) {
        this.allJob = allJob;
    }

    public void setJobId(String[] jobId) {
        this.jobId = jobId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
