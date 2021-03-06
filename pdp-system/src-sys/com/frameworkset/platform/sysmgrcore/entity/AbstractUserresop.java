/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Feb 08 15:38:28 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the td_sm_userresop table. You can customize
 * the behavior of this class by editing the class, {@link Userresop()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized *
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractUserresop implements Serializable {
    /**
     * The cached hash code value for this instance. Settting to 0 triggers
     * re-calculation.
     */
    private int hashValue = 0;

    /** The simple primary key value. */
    private UserresopKey id = new UserresopKey();

    /** The value of the simple state property. */
    private String state;

    /**
     * 用户实体
     */
    private User user = null;

    /**
     * 资源实体
     */
    private Res res = null;

    /**
     * 操作实体
     */
    private Operation operation = null;

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
    public Res getRes() {
        return res;
    }

    /**
     * @param res
     *            要设置的 res。
     */
    public void setRes(Res res) {
        this.res = res;
        
        if (id.getResId() == null && res != null)
            id.setResId(res.getResId());
    }

    /**
     * @return 返回 user。
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            要设置的 user。
     */
    public void setUser(User user) {
        this.user = user;
        
        if (id.getUserId() == null && user != null)
            id.setUserId(user.getUserId());
    }

    /**
     * Simple constructor of AbstractUserresop instances.
     */
    public AbstractUserresop() {
    }

    /**
     * Constructor of AbstractUserresop instances given a composite primary
     * key.
     * 
     * @param id
     */
    public AbstractUserresop(UserresopKey id) {
        this.setId(id);
    }

    /**
     * Return the composite id instance that identifies this object.
     * 
     * @return UserresopKey
     */
    public UserresopKey getId() {
        return this.id;
    }

    /**
     * Set the composite id instance that identifies this object.
     * 
     * @param id
     */
    public void setId(UserresopKey id) {
        this.hashValue = 0;
        this.id = id;
    }

    /**
     * Return the value of the STATE column.
     * 
     * @return String
     */
    public String getState() {
        return this.state;
    }

    /**
     * Set the value of the STATE column.
     * 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
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
        if (!(rhs instanceof Userresop))
            return false;
        Userresop that = (Userresop) rhs;
        if (this.getId() != null && that.getId() != null) {
            return (this.getId().equals(that.getId()));
        }
        return true;
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern
     * with the exception of array properties (these are very unlikely primary
     * key types).
     * 
     * @return int
     */
    public int hashCode() {
        if (this.hashValue == 0) {
            int result = 17;
            if (this.getId() == null) {
                result = super.hashCode();
            } else {
                result = this.getId().hashCode();
            }
            this.hashValue = result;
        }
        return this.hashValue;
    }
}