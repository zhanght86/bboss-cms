/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Mar 15 14:50:46 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;


/**
 * A class that represents a row in the TD_SM_OPERGROUP table. 
 * You can customize the behavior of this class by editing the class, {@link TdSmOpergroup()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractOpergpry 
    implements Serializable
{

    /** The simple primary key value. */
    private OpergpryKey id = new OpergpryKey();

    /**
     * 所关联的资源类型实体
     */
    private Restype restype = null;

    /**
     * 所关联的操作实体
     */
    private Operation operation = null;

    
    private Opergroup opergroup = null;    
    /**
     * @return 返回 operation。
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @param operation
     *            要设置的 operation。
     */
    public void setOperation(Operation operation) {
        this.operation = operation;

        if (id.getOpId() == null && operation != null)
            id.setOpId(operation.getOpId());
    }

    /**
     * @return 返回 res。
     */
    public Restype getRestype() {
        return restype;
    }

    /**
     * @param res
     *            要设置的 res。
     */
    public void setRestype(Restype restype) {
        this.restype = restype;

        if (id.getRestypeId() == null && restype != null)
        	restype.setRestypeId(restype.getRestypeId());
    }
    
    public Opergroup getOpergroup() {
        return opergroup;
    }

    /**
     * @param res
     *            要设置的 res。
     */
    public void setOpergroup(Opergroup opergroup) {
        this.opergroup = opergroup;

        if (id.getGroupId() == null && opergroup != null)
        	opergroup.setGroupId(opergroup.getGroupId());
    }    
    /**
     * Simple constructor of AbstractRoleresop instances.
     */
    public AbstractOpergpry() {
    }

    /**
     * Constructor of AbstractRoleresop instances given a composite primary
     * key.
     * 
     * @param id
     */
    public AbstractOpergpry(OpergpryKey id) {
        this.setId(id);
    }

    /**
     * Return the composite id instance that identifies this object.
     * 
     * @return RoleresopKey
     */
    public OpergpryKey getId() {
        return this.id;
    }

    /**
     * Set the composite id instance that identifies this object.
     * 
     * @param id
     */
    public void setId(OpergpryKey id) {
        this.id = id;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the
     * primary key values.
     * 
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs) {
        if (rhs == null)
            return false;
        if (!(rhs instanceof Roleresop))
            return false;
        Opergpry that = (Opergpry) rhs;
        if (this.getId() != null && that.getId() != null) {
            return (this.getId().equals(that.getId()));
        }
        return true;
    }

}
