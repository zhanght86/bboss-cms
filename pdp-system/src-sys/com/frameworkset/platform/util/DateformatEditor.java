package com.frameworkset.platform.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.annotations.wraper.ColumnWraper;

import com.frameworkset.util.ColumnToFieldEditor;

public class DateformatEditor extends ColumnToFieldEditor {

	public DateformatEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValueFromObject(ColumnWraper columnWraper ,Object fromValue) {
		if(fromValue == null)
			return null;
		if(fromValue instanceof Date)
		{
			DateFormat format =  DataFormatUtil.getSimpleDateFormat(columnWraper.editorparams());
			return format.format((Date)fromValue);
			
		}
		else if(fromValue instanceof BigDecimal)
		{
			DateFormat format =  DataFormatUtil.getSimpleDateFormat(columnWraper.editorparams());
			return format.format(new Date(((BigDecimal)fromValue).longValue()));
			
		}
		else if(fromValue instanceof Long)
		{
			DateFormat format =  DataFormatUtil.getSimpleDateFormat(columnWraper.editorparams());
			return format.format(((Long)fromValue).longValue());
			
		}
		else
		{
			return fromValue.toString();
		}
		
		
	}

	@Override
	public String getValueFromString(ColumnWraper columnWraper ,String fromValue) {
		// TODO Auto-generated method stub
		return fromValue;
	}

}
