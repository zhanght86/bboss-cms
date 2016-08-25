/*
 * Created Wed Feb 08 15:38:04 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

//import net.sf.hibernate.CallbackException;
//import net.sf.hibernate.Lifecycle;
//import net.sf.hibernate.Session;

/**
 * A class that represents a row in the 'td_sm_usergroup' table. This class may
 * be customized as it is never re-generated after being created.
 */
public class Usergroup extends AbstractUsergroup implements
        Serializable {

    private boolean isSave = false;

    /**
     * Simple constructor of Usergroup instances.
     */
    public Usergroup() {
    }

    /**
     * Constructor of Usergroup instances given a composite primary key.
     * 
     * @param id
     */
    public Usergroup(UsergroupKey id) {
        super(id);
    }

//    public boolean onDelete(Session arg0) throws CallbackException {
//        return false;
//    }
//
//    public void onLoad(Session arg0, Serializable arg1) {
//        isSave = true;
//    }
//
//    public boolean onSave(Session arg0) throws CallbackException {
//        if (isSave)
//            return true;
//        else {
//            isSave = true;
//            return false;
//        }
//    }
//
//    public boolean onUpdate(Session arg0) throws CallbackException {
//        return false;
//    }

}