/*
 * Created Wed Feb 08 15:37:05 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the 'td_sm_reslog' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Reslog
    extends AbstractReslog
    implements Serializable
{
    /**
     * Simple constructor of Reslog instances.
     */
    public Reslog()
    {
    }

    /**
     * Constructor of Reslog instances given a simple primary key.
     * @param reslogId
     */
    public Reslog(String reslogId)
    {
        super(reslogId);
    }

    /* Add customized code below */

}
