package com.frameworkset.platform.sysmgrcore.web.report;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.ValueObjectUtil;


public class DataInfoJRDataSource extends JRAbstractBeanDataSource {
	
	
	private final int pageSize;
	private int pageCount;
	private boolean nextPage;
	private List<?> returnValues;
	private Iterator<?> iterator;

	protected Object currentRow;
	protected HttpServletRequest request;
	protected Class<? extends DataInfo> dataInfo;

	public DataInfoJRDataSource(Class<? extends DataInfo> dataInfo,int pageSize,HttpServletRequest request) {
		super(true);
		this.pageSize = pageSize;		
		pageCount = 0;
		this.request = request;
		this.dataInfo = dataInfo;
		fetchPage();
	}

	protected void fetchPage() {
		if (pageSize <= 0) {
			try {
				DataInfo _dataInfo = dataInfo.newInstance();
				_dataInfo.initial(null, null, -1, pageSize, true, request);
				returnValues = (List<?>)_dataInfo.getListItems();
			} catch (Exception e) {
				throw new JRRuntimeException(e);
			}
			nextPage = false;
		}
		else {
			ListInfo listInfo = null;
			try {
				DataInfo _dataInfo = dataInfo.newInstance();
				_dataInfo.initial(null, false,  pageCount * pageSize, pageSize, false, request);
				returnValues = (List<?>)_dataInfo.getPageItems();
				nextPage = _dataInfo.getDataResultSize() == pageSize;
			} catch (Exception e) {
				throw new JRRuntimeException(e);
			}
			
			
		}

		++pageCount;

		initIterator();
	}

	public boolean next() {
		if (iterator == null) {
			return false;
		}
		
		boolean hasNext = iterator.hasNext();
		if (!hasNext && nextPage) {
			fetchPage();
			hasNext = iterator != null && iterator.hasNext();
		}
		
		if (hasNext) {
			currentRow = iterator.next();
		}

		return hasNext;
	}

	public void moveFirst() {
		if (pageCount == 1) {
			initIterator();
		}
		else {
			pageCount = 0;
			fetchPage();
		}
	}

	private void initIterator() {
		iterator = returnValues == null ? null : returnValues.iterator();
	}
	
	public Object getFieldValue(JRField field) throws JRException {
		String name = field.getName();
		Class clazz = field.getValueClass();
		Object value = ValueObjectUtil.getValue(currentRow,name);
		return ValueObjectUtil.typeCast(value, clazz);
//		return reader.getValue();
	}
	


}
