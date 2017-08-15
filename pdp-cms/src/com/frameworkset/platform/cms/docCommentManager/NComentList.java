package com.frameworkset.platform.cms.docCommentManager;

import java.util.List;

/**
 * <p>
 * Title: NComentList.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-9-6 上午11:16:32
 * @author biaoping.yin
 * @version 1.0.0
 */
public class NComentList {
	private int total;
	private List<DocComment> comments;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<DocComment> getComments() {
		return comments;
	}

	public void setComments(List<DocComment> comments) {
		this.comments = comments;
	}

	
}
