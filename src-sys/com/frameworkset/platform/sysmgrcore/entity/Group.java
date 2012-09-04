/*
 * Created Wed Feb 08 15:34:35 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the 'td_sm_group' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Group
    extends AbstractGroup
    implements Serializable
{
    /**
     * Simple constructor of Group instances.
     */
    public Group()
    {
    }

    /**
     * Constructor of Group instances given a simple primary key.
     * @param groupId
     */
    public Group(int groupId)
    {
        super(groupId);
    }

    /* Add customized code below */

}
