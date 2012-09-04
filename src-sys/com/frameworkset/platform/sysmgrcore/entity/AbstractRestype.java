/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Feb 08 15:34:02 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * A class that represents a row in the tb_sm_restype table. You can customize
 * the behavior of this class by editing the class, {@link Restype()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized *
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractRestype implements Serializable {
    /**
     * The cached hash code value for this instance. Settting to 0 triggers
     * re-calculation.
     */
    private int hashValue = 0;

    /** The composite primary key value. */
    private String restypeId;

    /** The value of the simple restypeName property. */
    private String restypeName;

    /** The value of the simple parentRestypeId property. */
    private String parentRestypeId;

    /** The value of the simple reserved1 property. */
    private String reserved1;

    /** The value of the simple reserved2 property. */
    private String reserved2;

    /** The value of the simple reserved3 property. */
    private String reserved3;

    /** The value of the simple reserved4 property. */
    private String reserved4;

    /** The value of the simple reserved5 property. */
    private String reserved5;

    /**
     * 资源实体集合
     */
    private Set resSet = null;

    /**
     * 资源类型实体集合
     */
    private Set attrdescSet = null;

    /**
     * @return 返回 attrdescSet。
     */
    public Set getAttrdescSet() {
        return attrdescSet;
    }

    /**
     * @param attrdescSet
     *            要设置的 attrdescSet。
     */
    public void setAttrdescSet(Set attrdescSet) {
        this.attrdescSet = attrdescSet;
    }

    /**
     * @return 返回 resSet。
     */
    public Set getResSet() {
        return resSet;
    }

    /**
     * @param resSet
     *            要设置的 resSet。
     */
    public void setResSet(Set resSet) {
        this.resSet = resSet;
    }

    /**
     * Simple constructor of AbstractRestype instances.
     */
    public AbstractRestype() {
    }

    /**
     * Constructor of AbstractRestype instances given a simple primary key.
     * 
     * @param restypeId
     */
    public AbstractRestype(String restypeId) {
        this.setRestypeId(restypeId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * 
     * @return String
     */
    public String getRestypeId() {
        return restypeId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * 
     * @param restypeId
     */
    public void setRestypeId(String restypeId) {
        this.hashValue = 0;
        this.restypeId = restypeId;
    }

    /**
     * Return the value of the RESTYPE_NAME column.
     * 
     * @return String
     */
    public String getRestypeName() {
        return this.restypeName;
    }

    /**
     * Set the value of the RESTYPE_NAME column.
     * 
     * @param restypeName
     */
    public void setRestypeName(String restypeName) {
        this.restypeName = restypeName;
    }

    /**
     * Return the value of the PARENT_RESTYPE_ID column.
     * 
     * @return String
     */
    public String getParentRestypeId() {
        return this.parentRestypeId;
    }

    /**
     * Set the value of the PARENT_RESTYPE_ID column.
     * 
     * @param parentRestypeId
     */
    public void setParentRestypeId(String parentRestypeId) {
        this.parentRestypeId = parentRestypeId;
    }

    /**
     * Return the value of the RESERVED1 column.
     * 
     * @return String
     */
    public String getReserved1() {
        return this.reserved1;
    }

    /**
     * Set the value of the RESERVED1 column.
     * 
     * @param reserved1
     */
    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    /**
     * Return the value of the RESERVED2 column.
     * 
     * @return String
     */
    public String getReserved2() {
        return this.reserved2;
    }

    /**
     * Set the value of the RESERVED2 column.
     * 
     * @param reserved2
     */
    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    /**
     * Return the value of the RESERVED3 column.
     * 
     * @return String
     */
    public String getReserved3() {
        return this.reserved3;
    }

    /**
     * Set the value of the RESERVED3 column.
     * 
     * @param reserved3
     */
    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    /**
     * Return the value of the RESERVED4 column.
     * 
     * @return String
     */
    public String getReserved4() {
        return this.reserved4;
    }

    /**
     * Set the value of the RESERVED4 column.
     * 
     * @param reserved4
     */
    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    /**
     * Return the value of the RESERVED5 column.
     * 
     * @return String
     */
    public String getReserved5() {
        return this.reserved5;
    }

    /**
     * Set the value of the RESERVED5 column.
     * 
     * @param reserved5
     */
    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
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
        if (!(rhs instanceof Restype))
            return false;
        Restype that = (Restype) rhs;
        if (this.getRestypeId() != null && that.getRestypeId() != null) {
            if (!this.getRestypeId().equals(that.getRestypeId())) {
                return false;
            }
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
            int restypeIdValue = this.getRestypeId() == null ? 0 : this
                    .getRestypeId().hashCode();
            result = result * 37 + restypeIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}