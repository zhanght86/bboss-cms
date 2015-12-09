package com.frameworkset.platform.util;

import java.text.DateFormat;
import java.util.Date;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.EditorInf;
import com.ibm.icu.math.BigDecimal;

public class DateformatEditor implements EditorInf<String> {

	public DateformatEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValueFromObject(Object fromValue) {
		if(fromValue == null)
			return null;
		if(fromValue instanceof Date)
		{
			DateFormat format = DataFormatUtil.getSimpleDateFormat(AccessControl.getAccessControl().getRequest(),"yyyy-MM-dd HH:mm:ss");
			return format.format((Date)fromValue);
			
		}
		else if(fromValue instanceof BigDecimal)
		{
			DateFormat format = DataFormatUtil.getSimpleDateFormat(AccessControl.getAccessControl().getRequest(),"yyyy-MM-dd HH:mm:ss");
			return format.format(new Date(((BigDecimal)fromValue).longValue()));
			
		}
		else if(fromValue instanceof Long)
		{
			DateFormat format = DataFormatUtil.getSimpleDateFormat(AccessControl.getAccessControl().getRequest(),"yyyy-MM-dd HH:mm:ss");
			return format.format(((Long)fromValue).longValue());
			
		}
		else
		{
			return fromValue.toString();
		}
		
		
	}

	@Override
	public String getValueFromString(String fromValue) {
		// TODO Auto-generated method stub
		return fromValue;
	}

}
