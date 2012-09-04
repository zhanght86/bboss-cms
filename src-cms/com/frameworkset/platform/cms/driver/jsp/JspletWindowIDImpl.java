package com.frameworkset.platform.cms.driver.jsp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class JspletWindowIDImpl implements JspletWindowID {


	// Private Member Variables ------------------------------------------------
	
    private String stringId = null;
    private int intId;
    
    
    // Constructor -------------------------------------------------------------
    
    /**
     * Private constructor that prevents external instantiation.
     * @param intId  the integer ID.
     * @param stringId  the string ID.
     */
    private JspletWindowIDImpl(int intId, String stringId) {
        this.stringId = stringId;
        this.intId = intId;
    }   
    
    
    // JspletWindowID Impl ----------------------------------------------------
    
    public String getStringId() {
        return stringId;
    }
    
    // Internal Methods --------------------------------------------------------
    
    private void readObject(ObjectInputStream stream) throws IOException {
    	intId = stream.readInt();
        stringId = String.valueOf(intId);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.write(intId);
    }

    // Common Object Methods ---------------------------------------------------
    
    /**
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof PortletWindowIDImpl) {
            result = (intId == ((PortletWindowIDImpl) object).intId);
        } else if (object instanceof String) {
            result = stringId.equals(object);
        } else if (object instanceof Integer) {
            result = (intId == ((Integer) object).intValue());
        }
        return (result);
    }
    **/
    
    public int hashCode() {
        return intId;
    }
    
    
    // Additional Methods ------------------------------------------------------
    
    public int intValue() {
        return intId;
    }
    
    /**
     * Creates a portlet window ID instance from a string.
     * @param stringId  the string ID from which the instance is created.
     * @return a portlet window ID instance created from the string ID.
     */
    static public JspletWindowIDImpl createFromString(String stringId) {
        char[] id = stringId.toCharArray();
        int _id = 1;
        for (int i = 0; i < id.length; i++) {
            if ((i % 2) == 0) {
                _id *= id[i];
            } else {
                _id ^= id[i];
            }
            _id = Math.abs(_id);
        }
        return new JspletWindowIDImpl(_id, stringId);
    }

}
