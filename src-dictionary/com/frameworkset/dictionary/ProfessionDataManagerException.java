/*
 * Title: The ERP System of kelamayi Downhole Company [PMIP]
 *
 * Copyright: Copyright (c) 2000-2004 westerasoft Co., Ltd All right reserved.
 *
 * Company: westerasoft Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-7-30
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 */
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
