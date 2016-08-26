

package com.frameworkset.platform.cms.sitemanager;


public class SiteFile implements java.io.Serializable
{
    private String parentDirectory;
    
    /**
     * @since 2006.12
     */
    public SiteFile() 
    {
     
    }
    
    /**
     * Access method for the parentDirectory property.
     * 
     * @return   the current value of the parentDirectory property
     */
    public String getParentDirectory() 
    {
        return parentDirectory;
    }
    
    /**
     * Sets the value of the parentDirectory property.
     * 
     * @param aParentDirectory the new value of the parentDirectory property
     */
    public void setParentDirectory(String aParentDirectory) 
    {
        parentDirectory = aParentDirectory;
    }
}
