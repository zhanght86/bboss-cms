/*
 * Title: The ERP System of kelamayi Downhole Company [PMIP]
 *
 * Copyright: Copyright (c) 2000-2004 westerasoft Co., Ltd All right reserved.
 *
 * Company: westerasoft Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-7-29
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 *
 */
package com.frameworkset.dictionary.xml;

import java.io.Serializable;

/**
 * @author biaoping.yin
 * 2004-7-29
 */
public class XMLParserException extends Exception implements Serializable
{

	/**
	 * @param string
	 */
	public XMLParserException(String string)
	{
		super(string);
	}
}
