package com.sany.sap.connection;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

public class SAPUtil {
	static BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("com/sany/sap/connection/sapserver.xml");
	public static SapConnectFactory getSapConnectFactory(String factory)
	{
		return context.getTBeanObject(factory, SapConnectFactory.class);
	}

}
