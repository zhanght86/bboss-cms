/*
 * Created Wed Feb 08 15:37:14 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the 'td_sm_role' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Role
    extends AbstractRole
    implements Serializable
{
    /**
     * Simple constructor of Role instances.
     */
    public Role()
    {
    }

    /**
     * Constructor of Role instances given a simple primary key.
     * @param roleId
     */
    public Role(String roleId)
    {
        super(roleId);
    }

    /* Add customized code below */

}
