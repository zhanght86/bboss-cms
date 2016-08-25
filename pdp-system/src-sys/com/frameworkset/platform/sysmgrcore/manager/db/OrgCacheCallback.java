package com.frameworkset.platform.sysmgrcore.manager.db;

import com.frameworkset.platform.sysmgrcore.entity.Organization;

public interface OrgCacheCallback {

	public void loadsubs(Organization root);
//	public void loadfathers(Organization node);
	public void loadfather(Organization node);

}
