/*
 * Created Wed Feb 08 15:35:25 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the 'td_sm_job' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Job
    extends AbstractJob
    implements Serializable
{
    /**
     * Simple constructor of Job instances.
     */
    public Job()
    {
    }

    /**
     * Constructor of Job instances given a simple primary key.
     * @param jobId
     */
    public Job(String jobId)
    {
        super(jobId);
    }

    /* Add customized code below */

}
