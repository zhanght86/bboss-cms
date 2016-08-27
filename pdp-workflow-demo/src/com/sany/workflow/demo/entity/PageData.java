package com.sany.workflow.demo.entity;

import java.util.List;

/**
 * @todo 自动补全插件，测试对象实体
 * @author tanx
 * @date 2014年11月10日
 * 
 */
public class PageData {
	private long totalsize;

	public long getTotalsize() {
		return totalsize;
	}

	public void setTotalsize(long totalsize) {
		this.totalsize = totalsize;
	}

	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}

	private List datas;

}
