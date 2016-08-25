package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.sysmgrcore.entity.IpControl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.IpControlServiceImpl;
import com.frameworkset.util.ListInfo;

public class IpControlController {
	private IpControlServiceImpl service;
	
	public String index(){
		return "path:index";
	}

	/**
	 * 查找列表
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param appcondition
	 * @param model
	 * @return
	 */
	
	public String queryListIpContorl(@PagerParam(name = PagerParam.SORT, defaultvalue = "bm") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			IpControl appcondition, ModelMap model){
		ListInfo datas=null;
		if(appcondition !=null){
			if(appcondition.getControluser()!=null && !appcondition.getControluser().equals("")){
				appcondition.setControluser("%" +appcondition.getControluser() +"%");
			}
		}
		datas = service.querylist(offset, pagesize, appcondition);
		model.addAttribute("datas", datas);
		 
		return "path:ipcontrol_list";
	}
	
	/**
	 * 删除（包括批量）
	 * @param ids
	 * @param model
	 * @return
	 */
	public @ResponseBody String deletebatch(String ids,ModelMap model) {
		String idKey[]=ids.split(",");
		List<IpControl>beans =new ArrayList<IpControl>();
		IpControl bean=null;
		for(String id:idKey){
			bean=new IpControl();
			bean.setId(id);
			beans.add(bean);
		}
		service.delteIpControl(beans);
		return "success";
	}
	/**
	 * 修改界面
	 * @param id
	 * @param model
	 * @return
	 */
	public String updatePre(String id, ModelMap model) {
		try {
			if(id==null||"".equals(id)){
				model.addAttribute("errmsg", "没有选择需修改台账记录！");
				return "path:updatePre";
			}
			IpControl bean=service.getIpControlById(id);
			model.addAttribute("ipcontrol", bean);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errmsg", e.getMessage());
			return "path:updatePre";
		}

		return "path:updatePre";
	}
	
	/**
	 * 更新
	 * @param bean
	 * @param model
	 * @return
	 */
	public @ResponseBody String update(IpControl bean, ModelMap model) {
		boolean result=service.updateIpControl(bean);
		if(! result){
			return "error";
		}
		return "success";

	}
	/**新增
	 * @param bean
	 * @param model
	 * @return
	 */
	public @ResponseBody String add(IpControl bean, ModelMap model) {
		if(bean!=null){
			bean.setId(java.util.UUID.randomUUID().toString());
			boolean result=service.addIpControl(bean);
			if(! result){
				return "error";
			}
			return "success";
		}else{
			return "error"; 
		}
		
	}
	
	

}
