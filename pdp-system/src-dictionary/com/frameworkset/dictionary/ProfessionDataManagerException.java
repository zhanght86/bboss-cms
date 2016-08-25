
package com.frameworkset.dictionary;

import java.io.Serializable;

/**
 * @author biaoping.yin
 */
public class ProfessionDataManagerException extends Exception 
				implements Serializable
				{
				

    /**
     * @param string
     */
    public ProfessionDataManagerException(String string) {
        super(string);
    }

}
