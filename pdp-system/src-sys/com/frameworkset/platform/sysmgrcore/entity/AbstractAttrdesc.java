/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Feb 08 15:34:27 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * A class that represents a row in the td_sm_attrdesc table. You can customize
 * the behavior of this class by editing the class, {@link Attrdesc()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized *
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractAttrdesc implements Serializable {
    /**
     * The cached hash code value for this instance. Settting to 0 triggers
     * re-calculation.
     */
    private int hashValue = 0;

    /** The composite primary key value. */
    private String attrDescId;

    /** The value of the simple restypeId property. */
    private String restypeId;

    /** The value of the simple attrName property. */
    private String attrName;

    /** The value of the simple attrDesc property. */
    private String attrDesc;

    /** The value of the simple contenttype property. */
    private String contenttype;

    /** The value of the simple contentlength property. */
    private String contentlength;

    /** The value of the simple remark1 property. */
    private String remark1;

    /** The value of the simple remark2 property. */
    private String remark2;

    /** The value of the simple remark3 property. */
    private String remark3;

    /** The value of the simple remark4 property. */
    private String remark4;

    /** The value of the simple remark5 property. */
    private String remark5;

    /**
     * 所关联的资源类型
     */
    private Restype restype = null;

    /**
     * @return 返回 restype。
     */
    public Restype getRestype() {
        return restype;
    }

    /**
     * @param restype
     *            要设置的 restype。
     */
    public void setRestype(Restype restype) {
        this.restype = restype;
    }

    /**
     * Simple constructor of AbstractAttrdesc instances.
     */
    public AbstractAttrdesc() {
    }

    /**
     * Constructor of AbstractAttrdesc instances given a simple primary key.
     * 
     * @param attrDescId
     */
    public AbstractAttrdesc(String attrDescId) {
        this.setAttrDescId(attrDescId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * 
     * @return String
     */
    public String getAttrDescId() {
        return attrDescId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * 
     * @param attrDescId
     */
    public void setAttrDescId(String attrDescId) {
        this.hashValue = 0;
        this.attrDescId = attrDescId;
    }

    /**
     * Return the value of the RESTYPE_ID column.
     * 
     * @return String
     */
    public String getRestypeId() {
        return this.restypeId;
    }

    /**
     * Set the value of the RESTYPE_ID column.
     * 
     * @param restypeId
     */
    public void setRestypeId(String restypeId) {
        this.restypeId = restypeId;
    }

    /**
     * Return the value of the ATTR_NAME column.
     * 
     * @return String
     */
    public String getAttrName() {
        return this.attrName;
    }

    /**
     * Set the value of the ATTR_NAME column.
     * 
     * @param attrName
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * Return the value of the ATTR_DESC column.
     * 
     * @return String
     */
    public String getAttrDesc() {
        return this.attrDesc;
    }

    /**
     * Set the value of the ATTR_DESC column.
     * 
     * @param attrDesc
     */
    public void setAttrDesc(String attrDesc) {
        this.attrDesc = attrDesc;
    }

    /**
     * Return the value of the CONTENTTYPE column.
     * 
     * @return String
     */
    public String getContenttype() {
        return this.contenttype;
    }

    /**
     * Set the value of the CONTENTTYPE column.
     * 
     * @param contenttype
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    /**
     * Return the value of the CONTENTLENGTH column.
     * 
     * @return String
     */
    public String getContentlength() {
        return this.contentlength;
    }

    /**
     * Set the value of the CONTENTLENGTH column.
     * 
     * @param contentlength
     */
    public void setContentlength(String contentlength) {
        this.contentlength = contentlength;
    }

    /**
     * Return the value of the REMARK1 column.
     * 
     * @return String
     */
    public String getRemark1() {
        return this.remark1;
    }

    /**
     * Set the value of the REMARK1 column.
     * 
     * @param remark1
     */
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    /**
     * Return the value of the REMARK2 column.
     * 
     * @return String
     */
    public String getRemark2() {
        return this.remark2;
    }

    /**
     * Set the value of the REMARK2 column.
     * 
     * @param remark2
     */
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    /**
     * Return the value of the REMARK3 column.
     * 
     * @return String
     */
    public String getRemark3() {
        return this.remark3;
    }

    /**
     * Set the value of the REMARK3 column.
     * 
     * @param remark3
     */
    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    /**
     * Return the value of the REMARK4 column.
     * 
     * @return String
     */
    public String getRemark4() {
        return this.remark4;
    }

    /**
     * Set the value of the REMARK4 column.
     * 
     * @param remark4
     */
    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    /**
     * Return the value of the REMARK5 column.
     * 
     * @return String
     */
    public String getRemark5() {
        return this.remark5;
    }

    /**
     * Set the value of the REMARK5 column.
     * 
     * @param remark5
     */
    public void setRemark5(String remark5) {
        this.remark5 = remark5;
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
        if (!(rhs instanceof Attrdesc))
            return false;
        Attrdesc that = (Attrdesc) rhs;
        if (this.getAttrDescId() != null && that.getAttrDescId() != null) {
            if (!this.getAttrDescId().equals(that.getAttrDescId())) {
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
            int attrDescIdValue = this.getAttrDescId() == null ? 0 : this
                    .getAttrDescId().hashCode();
            result = result * 37 + attrDescIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}