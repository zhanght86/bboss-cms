/*
 * Created on 2006-3-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.orgmanager;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @author ok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrgJobForm extends ActionForm implements Serializable{
      private String id;
      /**
       * @return Returns the id.
       */
      public String getId() {
            return id;
      }
      /**
       * @param id The id to set.
       */
      public void setId(String id) {
            this.id = id;
      }
      /**
       * @return Returns the jobId.
       */
      public String getJobId() {
            return jobId;
      }
      /**
       * @param jobId The jobId to set.
       */
      public void setJobId(String jobId) {
            this.jobId = jobId;
      }
      /**
       * @return Returns the orgId.
       */
      public String getOrgId() {
            return orgId;
      }
      /**
       * @param orgId The orgId to set.
       */
      public void setOrgId(String orgId) {
            this.orgId = orgId;
      }
      private String jobId;
      private String orgId;
}
