/**  
* @Title: DeskTopBlackGroundController.java
* @Package com.frameworkset.platform.esb.datareuse.common.action
* @Description: TODO(用一句话描述该文件做什么)
* @Copyright:Copyright (c) 2011
* @Company:三一集团
* @author qian.wang
* @date 2011-9-19 上午10:54:48
* @version V1.0  
*/
package com.frameworkset.platform.esb.datareuse.common.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.esb.datareuse.common.entity.BackgroundImage;
import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopBackGround;
import com.frameworkset.platform.esb.datareuse.common.service.DeskTopMenuShorcutManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;

public class DeskTopBackGroundController {

	DeskTopMenuShorcutManager deskTopMenuShorcutManager;
	
	public DeskTopMenuShorcutManager getDeskTopMenuShorcutManager() {
		return deskTopMenuShorcutManager;
	}

	public void setDeskTopMenuShorcutManager(
			DeskTopMenuShorcutManager deskTopMenuShorcutManager) {
		this.deskTopMenuShorcutManager = deskTopMenuShorcutManager;
	}

	public @ResponseBody String saveblackground(DeskTopBackGround bean,HttpServletRequest request){
		
		AccessControl control = AccessControl.getAccessControl();
		// control.checkAccess(request, response);
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		bean.setCreatdate(new Timestamp(new Date().getTime()));
		bean.setFilename(bean.getFilename().substring(bean.getFilename().indexOf("/")+1,bean.getFilename().length()));
		
		DeskTopBackGround deskTopBlackGround = null;
		try {
			deskTopBlackGround = deskTopMenuShorcutManager.getDesktopBlackGround(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(deskTopBlackGround != null){
				deskTopMenuShorcutManager.updateDesktopBackGround(bean);
				return "ok";
			}else{
				deskTopMenuShorcutManager.insertDesktopBackGround(bean);
				return "ok";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
	}
	
	public @ResponseBody(datatype="json") List<BackgroundImage> getListBlackGround(HttpServletRequest request){
		AccessControl control = AccessControl.getAccessControl();
		// control.checkAccess(request, response);
		DeskTopBackGround bean = new DeskTopBackGround();
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		
		List<BackgroundImage> list = null;
		
		try {
			list = deskTopMenuShorcutManager.getDesktopStyleCustomBackgroundImage(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public String main(){
		
		return "path:custom";
	}
	
	
	/**
	 * 获取用户自定义背景图片列表信息
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param request
	 * @param model
	 * @return
	 */
	public String  getAllListBackGrounds(String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			HttpServletRequest request,
			ModelMap model){
		AccessControl control = AccessControl.getAccessControl();
		DeskTopBackGround bean = new DeskTopBackGround();
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		ListInfo datas = null;
		try {
			datas = deskTopMenuShorcutManager.getListDesktopStyleCustom(sortKey, desc, offset, pagesize, bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("datas", datas);
		return "path:customlist";
	}
	
	public  @ResponseBody String uploadBackGround( MultipartFile file,HttpServletRequest request,
			@RequestParam(name="cn_name")String cn_name
            )
	{
		
		try {
			
//			file.transferTo(newFile);
			AccessControl control = AccessControl.getAccessControl();
			DeskTopBackGround bean = new DeskTopBackGround();
			bean.setUserid(control.getUserID());
			bean.setSubsystem(control.getCurrentSystemID());
			bean.setCn_name(cn_name);
			String str = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String filename = "bj_"+(System.currentTimeMillis() +str);
			bean.setFilename("custom/"+filename);
			bean.setCreatdate(new Timestamp(new Date().getTime()));
			//判断是否存在该图片
//			DeskTopBackGround existDBG = deskTopMenuShorcutManager.getDesktopStyleCustomByFileName(bean);
//			if(existDBG!= null)
//				return "exit";
			bean.setPicture(file);
			deskTopMenuShorcutManager.insertDesktopStyleCustom(bean);
			bean.setPicture(null);
			File newFile = new File(request.getRealPath("/")+"desktop/wallpapers/custom/"+filename);
			deskTopMenuShorcutManager.storeDesktopStyleCustomPictureToDisk(newFile,bean);
			
			
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return "fail:"+e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return "fail:"+e.getMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return "fail:" + e.getMessage();
		}
		return "ok";
	}
	
    public @ResponseBody String deleteDesktopStyleCustom(@RequestParam(name="filename")String filename,
    		HttpServletRequest request){
    	
    	AccessControl control = AccessControl.getAccessControl();
		DeskTopBackGround bean = new DeskTopBackGround();
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		bean.setFilename(filename);
		try {
			deskTopMenuShorcutManager.deleteDesktopStyleCustom(bean);
			File newFile = new File(request.getRealPath("/")+"desktop/wallpapers/"+filename);
			if(newFile.exists()){
				newFile.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return "fail:"+e.getMessage();
		}
		
		return "ok";
    }

	public @ResponseBody String updateDesktopStyleCustom(@RequestParam(name="filename")String filename,
			@RequestParam(name="cn_name")String cn_name){
		AccessControl control = AccessControl.getAccessControl();
		DeskTopBackGround bean = new DeskTopBackGround();
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		bean.setFilename(filename);
		bean.setCn_name(cn_name);
		try {
			deskTopMenuShorcutManager.updateDesktopStyleCustom(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail:"+e.getMessage();
		}
		return "ok";
	}
	
	public @ResponseBody String saveBackGrounds(List<DeskTopBackGround> beans,HttpServletRequest request){
		
		try {
			if(beans.size()>0){}
			deskTopMenuShorcutManager.saveListBackGrounds(beans);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail:"+e.getMessage();
		}
		
		return "ok";
	}
	
}
