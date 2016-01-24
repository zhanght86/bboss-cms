package com.frameworkset.platform.util;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;

import com.frameworkset.platform.security.event.ACLEventType;

public class EventUtil {

	public EventUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static void sendRESOURCE_ROLE_INFO_CHANGEEvent()
	{
		Event event = new EventImpl("",
				ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
		EventHandle.sendEvent(event);
	}
	
	public static void sendUSER_ROLE_INFO_CHANGEEvent()
	{
		Event event = new EventImpl("",
				ACLEventType.USER_ROLE_INFO_CHANGE);
		EventHandle.sendEvent(event);
	}
	
	public static void sendUSER_ROLE_INFO_CHANGEEvent(String source)
	{
		Event event = new EventImpl(source,
				ACLEventType.USER_ROLE_INFO_CHANGE);
		EventHandle.sendEvent(event);
	}
	public static void sendUSER_INFO_DELETEEvent(String userIds[])
	{
		Event event = new EventImpl(userIds,
				ACLEventType.USER_INFO_DELETE);
		EventHandle.sendEvent(event);
	}

	public static void sendORGUNIT_INFO_DELETEEvent(String orgId) {
		Event event1 = new EventImpl(orgId, ACLEventType.ORGUNIT_INFO_DELETE);
		EventHandle.sendEvent(event1);
	}
	

}
