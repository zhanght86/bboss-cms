package com.frameworkset.platform.cms.searchmanager;

import java.util.List;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchHit;

public class HitResult {
	private long searchTime;
	private List<CMSSearchHit> hits;
	public long getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(long searchTime) {
		this.searchTime = searchTime;
	}
	public List<CMSSearchHit> getHits() {
		return hits;
	}
	public void setHits(List<CMSSearchHit> hits) {
		this.hits = hits;
	}

}
