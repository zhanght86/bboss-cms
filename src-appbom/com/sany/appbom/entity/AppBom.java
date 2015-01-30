/*
 * @(#)AppBom.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.appbom.entity;

import com.frameworkset.orm.annotation.PrimaryKey;


public class AppBom {
	  @PrimaryKey
	  private String id ;	  
	  private String bm;
	  private String app_name_en;
	  private String app_name;
	  private String apply_domain;
	  private String description;
	  private Integer soft_level;
	  private String supplier;
	  private String start_year;
	  private Integer state;
	  private Integer rd_type;
	  private String version_no;
	  private String domain_url;
	  private String struct_mode;
	  private String soft_language;
	  private String develop_tool;
	  private String db_type;
	  private String department_develop;
	  private String product_manager;
	  private String department_maintain;
	  private String sys_manager;
	  private Integer plan_type;
	  private String evolve_strategy;
	  private String evolve_plan;
	  private String evolve_depart;
	  private Integer manage_scope;
	  private String main_description ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getApp_name_en() {
		return app_name_en;
	}
	public void setApp_name_en(String app_name_en) {
		this.app_name_en = app_name_en;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApply_domain() {
		return apply_domain;
	}
	public void setApply_domain(String apply_domain) {
		this.apply_domain = apply_domain;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSoft_level() {
		return soft_level;
	}
	public void setSoft_level(Integer soft_level) {
		this.soft_level = soft_level;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getStart_year() {
		return start_year;
	}
	public void setStart_year(String start_year) {
		this.start_year = start_year;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getRd_type() {
		return rd_type;
	}
	public void setRd_type(Integer rd_type) {
		this.rd_type = rd_type;
	}
	public String getVersion_no() {
		return version_no;
	}
	public void setVersion_no(String version_no) {
		this.version_no = version_no;
	}
	public String getDomain_url() {
		return domain_url;
	}
	public void setDomain_url(String domain_url) {
		this.domain_url = domain_url;
	}
	public String getStruct_mode() {
		return struct_mode;
	}
	public void setStruct_mode(String struct_mode) {
		this.struct_mode = struct_mode;
	}
	public String getSoft_language() {
		return soft_language;
	}
	public void setSoft_language(String soft_language) {
		this.soft_language = soft_language;
	}
	public String getDevelop_tool() {
		return develop_tool;
	}
	public void setDevelop_tool(String develop_tool) {
		this.develop_tool = develop_tool;
	}
	public String getDb_type() {
		return db_type;
	}
	public void setDb_type(String db_type) {
		this.db_type = db_type;
	}
	public String getDepartment_develop() {
		return department_develop;
	}
	public void setDepartment_develop(String department_develop) {
		this.department_develop = department_develop;
	}
	public String getProduct_manager() {
		return product_manager;
	}
	public void setProduct_manager(String product_manager) {
		this.product_manager = product_manager;
	}

	public String getSys_manager() {
		return sys_manager;
	}
	public void setSys_manager(String sys_manager) {
		this.sys_manager = sys_manager;
	}
	public Integer getPlan_type() {
		return plan_type;
	}
	public void setPlan_type(Integer plan_type) {
		this.plan_type = plan_type;
	}
	public String getEvolve_strategy() {
		return evolve_strategy;
	}
	public void setEvolve_strategy(String evolve_strategy) {
		this.evolve_strategy = evolve_strategy;
	}
	public String getEvolve_plan() {
		return evolve_plan;
	}
	public void setEvolve_plan(String evolve_plan) {
		this.evolve_plan = evolve_plan;
	}
	public String getEvolve_depart() {
		return evolve_depart;
	}
	public void setEvolve_depart(String evolve_depart) {
		this.evolve_depart = evolve_depart;
	}
	public Integer getManage_scope() {
		return manage_scope;
	}
	public void setManage_scope(Integer manage_scope) {
		this.manage_scope = manage_scope;
	}
	public String getMain_description() {
		return main_description;
	}
	public void setMain_description(String main_description) {
		this.main_description = main_description;
	}
	public String getDepartment_maintain() {
		return department_maintain;
	}
	public void setDepartment_maintain(String department_maintain) {
		this.department_maintain = department_maintain;
	}
	
}
