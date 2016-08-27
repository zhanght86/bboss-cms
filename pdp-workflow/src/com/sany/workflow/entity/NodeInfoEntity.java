package com.sany.workflow.entity;

/**
 * 节点信息实体bean
 * 
 * @todo
 * @author tanx
 * @date 2014年7月14日
 * 
 */
public class NodeInfoEntity {

	private long DURATION_NODE;// 处理工时

	private int IS_CONTAIN_HOLIDAY;// 节假日策略

	private int NOTICERATE;// 预警频率

	public long getDURATION_NODE() {
		return DURATION_NODE;
	}

	public void setDURATION_NODE(long dURATION_NODE) {
		DURATION_NODE = dURATION_NODE;
	}

	public int getIS_CONTAIN_HOLIDAY() {
		return IS_CONTAIN_HOLIDAY;
	}

	public void setIS_CONTAIN_HOLIDAY(int iS_CONTAIN_HOLIDAY) {
		IS_CONTAIN_HOLIDAY = iS_CONTAIN_HOLIDAY;
	}

	public int getNOTICERATE() {
		return NOTICERATE;
	}

	public void setNOTICERATE(int nOTICERATE) {
		NOTICERATE = nOTICERATE;
	}

}
