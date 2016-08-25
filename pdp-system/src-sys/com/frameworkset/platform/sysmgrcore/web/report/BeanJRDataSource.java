package com.frameworkset.platform.sysmgrcore.web.report;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.ValueObjectUtil;


public class BeanJRDataSource<T> extends JRAbstractBeanDataSource {
	private final String sql;
	private final String dbname;
	private final int pageSize;
	private int pageCount;
	private boolean nextPage;
	private List<T> returnValues;
	private Iterator<T> iterator;
	private Class<T> clazz;
	protected T currentRow;
	protected RowHandler<T> rowhandler;

	public BeanJRDataSource(Class<T> clazz,String dbname,String sql, int pageSize) {
		super(true);
		this.dbname = dbname;
		this.sql = sql;
		this.pageSize = pageSize;
		
		this.clazz = clazz;
		
		pageCount = 0;
		fetchPage();
	}
	
	public BeanJRDataSource(RowHandler<T> rowhandler,Class<T> clazz,String dbname,String sql, int pageSize) {
		super(true);
		this.dbname = dbname;
		this.sql = sql;
		this.pageSize = pageSize;
		
		this.clazz = clazz;
		this.rowhandler = rowhandler;
		pageCount = 0;
		fetchPage();
	}

	protected void fetchPage() {
		if (pageSize <= 0) {
			try {
				if(rowhandler == null)
					returnValues = SQLExecutor.queryListWithDBName(clazz, dbname,sql);
				else
					returnValues = SQLExecutor.queryListWithDBNameByRowHandler(rowhandler,clazz, dbname,sql);
			} catch (Exception e) {
				throw new JRRuntimeException(e);
			}
			nextPage = false;
		}
		else {
			ListInfo listInfo = null;
			try {
				if(rowhandler == null)
					listInfo = SQLExecutor.moreListInfoWithDBName(clazz, dbname, sql, pageCount * pageSize, pageSize);
				else
					listInfo = SQLExecutor.moreListInfoWithDBNameByRowHandler(rowhandler,clazz, dbname, sql, pageCount * pageSize, pageSize);
				nextPage = listInfo.getMoreListInfo().isHasmore();
				returnValues = listInfo.getDatas();
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
		Object value = ValueObjectUtil.getValue(this.currentRow,name);
		return ValueObjectUtil.typeCast(value, clazz);
//		return reader.getValue();
	}

}
