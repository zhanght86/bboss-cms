
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
