/*
 *
 * Title:
 *
 * Copyright: Copyright (c) 2004
 *
 * Company: iSany Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-6-10
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 */

package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 张建会
 * 资源的数据封装。
 */
public class Resource implements Serializable {
    /* 资源ID */
    private int resourceID;

    /* 上层资源ID */
    private int parentID;

    /* 资源描述 */
    private String resourceDescription;

    /* 资源名称*/
    private String resourceName;

    /* 资源级别*/
    private int resourceLevel;

    /* 资源类型*/
    private int resourceType;

    /* 该资源对应的角色列表 */
    private List roles = new ArrayList();

    /* 该资源的子资源列表*/
    private List children = new ArrayList();


    /**x
     * 构造函数
     */
    public Resource() {

    }

    /**
     * 得到资源ID
     */
    public int getResourceID() {
        return this.resourceID;
    }

    /**
     * 设置资源ID
     */
    public void setResourceID(int value) {
        this.resourceID = value;
    }

    /**
     * 得到资源名称
     */
    public String getResourceName() {
        return this.resourceName;
    }

    /**
     * 设置资源名称
     */
    public void setResourceName(String value) {
        this.resourceName = value;
    }

    /**
     * 得到资源级别
     */
    public int getResourceLevel() {
        return this.resourceLevel;
    }

    /**
     * 设置资源级别
     */
    public void setResourceLevel(int value) {
        this.resourceLevel = value;
    }

    /**
     * 得到资源描述
     */
    public String getResourceDescription() {
        return this.resourceDescription;
    }

    /**
     * 设置资源描述
     */
    public void setResourceDescription(String value) {
        this.resourceDescription = value;
    }

    /**
     * 得到资源类型
     */
    public int getResourceType() {
        return this.resourceType;
    }

    /**
     * 设置资源类型
     */
    public void setResourceType(int value) {
        this.resourceType = value;
    }

    /**
     * 得到该资源对应的角色列表
     */
    public List getRoles() {
        return this.roles;
    }

    /**
     * 设置该资源对应的角色列表
     */
    public void setRoles(List value) {
        this.roles = value;
    }

    /**
     * 得到该资源的子资源列表
     */
    public List getChildren() {
        return this.children;
    }

    /**
     * 设置该资源的子资源列表
     */
    public void setChildren(List value) {
        this.children = value;
    }

    /**
     * 得到该资源的父资源列表
     */
    public int getParentID() {
        return this.parentID;
    }

    /**
     * 设置资源的父资源列表
     */
    public void setParentID(int value) {
        this.parentID = value;
    }
}
