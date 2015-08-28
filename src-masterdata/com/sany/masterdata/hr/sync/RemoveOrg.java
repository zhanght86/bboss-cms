package com.sany.masterdata.hr.sync;

public class RemoveOrg implements java.lang.Comparable<RemoveOrg>{
	private String org_id;
	private String org_tree_level;
	public RemoveOrg() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int compareTo(RemoveOrg o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_tree_level() {
		return org_tree_level;
	}

	public void setOrg_tree_level(String org_tree_level) {
		this.org_tree_level = org_tree_level;
	}

}
