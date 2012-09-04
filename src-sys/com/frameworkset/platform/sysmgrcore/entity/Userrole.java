/*
 * Created Wed Feb 08 15:38:36 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

//import net.sf.hibernate.CallbackException;
//import net.sf.hibernate.Lifecycle;
//import net.sf.hibernate.Session;

/**
 * A class that represents a row in the 'td_sm_userrole' table. This class may
 * be customized as it is never re-generated after being created.
 */
public class Userrole extends AbstractUserrole implements 
        Serializable {

    boolean isSave = false;

    /**
     * Simple constructor of Userrole instances.
     */
    public Userrole() {
    }

    /**
     * Constructor of Userrole instances given a composite primary key.
     * 
     * @param id
     */
    public Userrole(UserroleKey id) {
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