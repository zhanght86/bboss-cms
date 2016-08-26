package com.frameworkset.platform.cms.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.platform.cms.bean.FlowInfo;

public class CMSDBFunction {

	public CMSDBFunction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * create or replace function site_flow_id(a_site_id int) return int is
	 * l_parent_workflow char(1); l_parent_id number(10); l_workflow int; begin
	 * select PARENT_WORKFLOW,mainsite_id,flow_id into
	 * l_parent_workflow,l_parent_id,l_workflow from td_cms_site where site_id =
	 * a_site_id;
	 * 
	 * if l_parent_workflow = '0' then return l_workflow; end if;
	 * 
	 * if l_parent_workflow = '1' then return site_flow_id(l_parent_id); end if;
	 * 
	 * return -1; end;
	 * 
	 * @param siteid
	 * @return
	 * @throws Exception
	 */
	public static int getSiteFlowid(int siteid) throws Exception {
		String sql = " select PARENT_WORKFLOW,mainsite_id,flow_id from td_cms_site where site_id = ?";
		Map flowid = SQLExecutor.queryObject(HashMap.class, sql, siteid);
		String l_parent_workflow = (String) flowid.get("PARENT_WORKFLOW");

		BigDecimal l_parent_id = (BigDecimal) flowid.get("mainsite_id".toUpperCase());

		BigDecimal l_workflow = (BigDecimal) flowid.get("flow_id".toUpperCase());

		if (l_parent_workflow != null && l_parent_workflow.equals("0"))
			return l_workflow != null ? l_workflow.intValue() : -1;

		if (l_parent_workflow != null && l_parent_workflow.equals("1"))
			return l_parent_id != null ? getSiteFlowid(l_parent_id.intValue()) : -1;

		return -1;
	}

	/**
	 * create or replace function channel_flow_id(ch_id int) return int is
	 * l_parent_workflow char(1); l_parent_id number(10); l_site_id number(10);
	 * l_workflow int; begin select PARENT_WORKFLOW,parent_id,site_id,workflow
	 * into l_parent_workflow,l_parent_id,l_site_id,l_workflow from
	 * td_cms_channel where channel_id = ch_id;
	 * 
	 * if l_parent_workflow = '0' then return l_workflow; end if;
	 * 
	 * if l_parent_workflow = '1' then if l_parent_id is null then --select
	 * flow_id into l_workflow from td_cms_site where site_id = l_site_id;
	 * --return l_workflow; return site_flow_id(l_site_id); end if;
	 * 
	 * return channel_flow_id(l_parent_id); end if;
	 * 
	 * return -1; end;
	 * 
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public static int getSiteShannelFlowid(int channel) throws Exception {
		String sql = " select PARENT_WORKFLOW,parent_id,site_id,workflow from td_cms_channel where channel_id = ?";
		Map flowid = SQLExecutor.queryObject(HashMap.class, sql, channel);
		String l_parent_workflow = (String) flowid.get("PARENT_WORKFLOW");

		BigDecimal l_parent_id = (BigDecimal) flowid.get("parent_id".toUpperCase());

		BigDecimal l_site_id = (BigDecimal) flowid.get("l_site_id".toUpperCase());
		BigDecimal l_workflow = (BigDecimal) flowid.get("workflow".toUpperCase());
		if (l_parent_workflow != null && l_parent_workflow.equals("0"))
			return l_workflow != null ? l_workflow.intValue() : -1;

		if (l_parent_workflow != null && l_parent_workflow.equals("1")) {
			if (l_parent_id == null)
				return getSiteFlowid(l_site_id.intValue());
			else
				return getSiteShannelFlowid(l_parent_id.intValue());

		}

		return -1;
	}
	
	/**
	 * create or replace function f_channel_flow(ch_id int) return typ_id_name_list
								is
								  l_parent_workflow char(1);
								  l_parent_id number(10);
								  l_site_id number(10);
								  l_workflow int;
								  l_workflow_name varchar(100);
								  ret_list typ_id_name_list := typ_id_name_list(); --返回结果集变量
								begin
								  select PARENT_WORKFLOW,parent_id,site_id,workflow,b.name 
									into l_parent_workflow,l_parent_id,l_site_id,l_workflow,l_workflow_name 
								  from td_cms_channel a left join tb_cms_flow b on a.workflow = b.id
								  where channel_id = ch_id;

								  if l_parent_workflow = '0' then 
									ret_list.extend;
									ret_list(1) := new TYP_ID_NAME(l_workflow,l_workflow_name);

									return ret_list;
								  end if;

								  if l_parent_workflow = '1' then 
									if l_parent_id is null then
									  return f_site_flow(l_site_id);
									end if;

									return f_channel_flow(l_parent_id);
								  end if;

								  return null;
								end;
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public static FlowInfo getSiteShannelFlowInfo(int channel) throws Exception {
		String sql = " select PARENT_WORKFLOW,parent_id,site_id,workflow,b.name "			 
				+ "from td_cms_channel a left join tb_cms_flow b on a.workflow = b.id where channel_id = ?";
		Map flowid = SQLExecutor.queryObject(HashMap.class, sql, channel);
		String l_parent_workflow = (String) flowid.get("PARENT_WORKFLOW");
		String l_workflow_name = (String) flowid.get("name".toUpperCase());
		BigDecimal l_parent_id = (BigDecimal) flowid.get("parent_id".toUpperCase());

		BigDecimal l_site_id = (BigDecimal) flowid.get("l_site_id".toUpperCase());
		BigDecimal l_workflow = (BigDecimal) flowid.get("workflow".toUpperCase());
		if (l_parent_workflow != null && l_parent_workflow.equals("0"))
		{
//			return l_workflow != null ? l_workflow.intValue() : -1;
			FlowInfo flowInfo = new FlowInfo();
			flowInfo.setId(l_workflow.intValue());
			flowInfo.setName(l_workflow_name);
			return flowInfo;
		}

		if (l_parent_workflow != null && l_parent_workflow.equals("1")) {
			if (l_parent_id == null)
				return getSiteFlowInfo(l_site_id.intValue());
			else
				return getSiteShannelFlowInfo(l_parent_id.intValue());

		}
		
		return null;
	}
	
	/**
	 * create or replace function f_site_flow(a_site_id int) return typ_id_name_list
								is
								  l_parent_workflow char(1);
								  l_parent_id number(10);
								  l_workflow int;
								  l_workflow_name varchar(100);
								  ret_list typ_id_name_list := typ_id_name_list(); --返回结果集变量
								begin
								  select PARENT_WORKFLOW,mainsite_id,flow_id,b.name
									into l_parent_workflow,l_parent_id,l_workflow,l_workflow_name 
								  from td_cms_site a left join tb_cms_flow b on a.flow_id=b.id
								  where site_id = a_site_id;

								  if l_parent_workflow = '0' then 
									ret_list.extend;
									ret_list(1) := new TYP_ID_NAME(l_workflow,l_workflow_name);
									return ret_list;
								  end if;

								  if l_parent_workflow = '1' then 
									return f_site_flow(l_parent_id);
								  end if;

								  return null;
								end;
	 * @param siteid
	 * @return
	 * @throws Exception
	 */
	public static FlowInfo getSiteFlowInfo(int siteid) throws Exception {
		 
			String sql = " select PARENT_WORKFLOW,mainsite_id,flow_id,b.name from td_cms_site a left join tb_cms_flow b on a.flow_id=b.id where site_id = ?";
			Map flowid = SQLExecutor.queryObject(HashMap.class, sql, siteid);
			String l_parent_workflow = (String) flowid.get("PARENT_WORKFLOW");

			BigDecimal l_parent_id = (BigDecimal) flowid.get("mainsite_id".toUpperCase());

			BigDecimal l_workflow = (BigDecimal) flowid.get("flow_id".toUpperCase());
			String l_workflow_name = (String) flowid.get("name".toUpperCase());
			if (l_parent_workflow != null && l_parent_workflow.equals("0"))
			{
//				return l_workflow != null ? l_workflow.intValue() : -1;
				FlowInfo flowInfo = new FlowInfo();
				flowInfo.setId(l_workflow.intValue());
				flowInfo.setName(l_workflow_name);
				return flowInfo;
			}
			if (l_parent_workflow != null && l_parent_workflow.equals("1"))
				return getSiteFlowInfo(l_parent_id.intValue());
	 
			return null;
	}
	
	/**
	 * create or replace procedure recordpubrelation_proc (pubobject in varchar2,
												  refobject in varchar2, 
												  pubtype in  number,
												  pubsite in varchar2,
												  reftype in number,
												  refsite in varchar2) AS num NUMBER;
								 BEGIN
									select count(PUBLISHOBJECT) into num from td_cms_pubobject_relation where 
								  PUBLISHOBJECT=pubobject and REFERENCEOBJECT=refobject and PUBLISHTYPE=pubtype 
								  and PUBLISH_SITE=pubsite and REFERENCETYPE=reftype and REFERENCE_SITE=refsite;
								  DBMS_OUTPUT.PUT_LINE('Value is not exact because pi is 
								irrational.');

								  if(num = 0) then 
								   insert into td_cms_pubobject_relation
								   (PUBLISHOBJECT,REFERENCEOBJECT,PUBLISHTYPE,PUBLISH_SITE,REFERENCETYPE,REFERENCE_SITE)
								   values( pubobject,refobject,pubtype,pubsite,reftype,refsite );
								   commit;
								  end if;


								 END;
	 * @param pubobject
	 * @param refobject
	 * @param pubtype
	 * @param pubsite
	 * @param reftype
	 * @param refsite
	 * @throws Exception
	 */
	public static void recordpubrelation_proc(String pubobject,
			String refobject, 
			int pubtype,
			String pubsite,
			int reftype,
			String refsite ) throws Exception {
		 
		String sql = "select count(PUBLISHOBJECT) as num from td_cms_pubobject_relation "
				+ "where PUBLISHOBJECT=? and REFERENCEOBJECT=? and PUBLISHTYPE=? and PUBLISH_SITE=? "
				+ "and REFERENCETYPE=? and REFERENCE_SITE=?";
		int num = SQLExecutor.queryObject(int.class, sql, pubobject,
				  refobject, 
				  pubtype,
				  pubsite,
				  reftype,
				  refsite);
		if(num == 0)
		{
			sql = "insert into td_cms_pubobject_relation(PUBLISHOBJECT,REFERENCEOBJECT,PUBLISHTYPE,PUBLISH_SITE,REFERENCETYPE,REFERENCE_SITE) "
					+ "values( ?,?,?,?,?,?)";
			SQLExecutor.insert(sql, pubobject,
					  refobject, 
					  pubtype,
					  pubsite,
					  reftype,
					  refsite);
		}  
		  
}

	public static void main(String[] args) throws Exception {
		CMSDBFunction.getSiteFlowid(1);
	}

}
