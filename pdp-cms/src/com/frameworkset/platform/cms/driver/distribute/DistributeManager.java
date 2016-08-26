package com.frameworkset.platform.cms.driver.distribute;

import com.frameworkset.platform.cms.driver.distribute.impl.HTMLDistribute;
import com.frameworkset.platform.cms.driver.distribute.impl.MAILDistribute;
import com.frameworkset.platform.cms.driver.distribute.impl.RSSDistribute;

public class DistributeManager implements java.io.Serializable {
	public static final int HTML_DISTRIBUTE = 0;
	public static final int RSS_DISTRIBUTE = 1;
	public static final int MAIL_DISTRIBUTE = 2;
	
	public static Distribute getDistribute(int type)
	{
		
		switch(type)
		{
			case HTML_DISTRIBUTE:
				return new HTMLDistribute();
			case RSS_DISTRIBUTE:
				return new RSSDistribute();
			case MAIL_DISTRIBUTE:
				return new MAILDistribute();
			default:
				return null;
			
		}
	}

}
