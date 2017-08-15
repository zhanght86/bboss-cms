package com.frameworkset.platform.dictionary;

import org.frameworkset.event.AbstractEventType;

/**
 * 
 * 
 * <p>Title: DictionaryChangeEventImpl.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date May 23, 2008 9:56:57 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class DictionaryChangeEventImpl extends AbstractEventType implements DictionaryChangeEvent{
	public DictionaryChangeEventImpl()
	{
		
	}
	
	public DictionaryChangeEventImpl(String eventtype)
	{
		this.eventtype = eventtype;
	}

}
