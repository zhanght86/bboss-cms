/*
 * Created Wed Feb 08 15:36:08 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the 'td_sm_operations' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Operation
    extends AbstractOperation
    implements Serializable
{
    /**
     * Simple constructor of Operation instances.
     */
    public Operation()
    {
    }

    /**
     * Constructor of Operation instances given a simple primary key.
     * @param opId
     */
    public Operation(String opId)
    {
        super(opId);
    }

    /* Add customized code below */

}
