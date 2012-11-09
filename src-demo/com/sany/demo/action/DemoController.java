/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.sany.demo.action;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.demo.entity.Demo;
import com.sany.demo.service.DemoServiceImpl;

/**
 * <p> DemoController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-11-1 下午4:52:33
 * @author biaoping.yin
 * @version 1.0
 */
public class DemoController {
	private DemoServiceImpl demoServiceImpl;
	public String index()
	{
		return "path:index";
	}
	public String addPre()
	{
		return "path:addPre";
	}
	
	public @ResponseBody String addDemo(Demo demo) 
	{
		String msg = null;
		try {
			String uuid = java.util.UUID.randomUUID().toString();
			demo.setId(uuid);	
			demoServiceImpl.addDemo(demo);
			msg = "success";
		} catch (Exception e) {
			msg = StringUtil.formatBRException(e);
		}
		return msg;
		
	}
	public String updatePre(String id,ModelMap model) throws Exception
	{
		Demo demo = this.demoServiceImpl.getDemo(id);
		if(demo == null)
			throw new Exception("id为"+ id + "的记录不存在");
		model.addAttribute("demo", demo);
		return "path:updatePre";
	}
	
	public String viewDemo(String id,ModelMap model) throws Exception
	{
		Demo demo = this.demoServiceImpl.getDemo(id);
		if(demo == null)
			throw new Exception("id为"+ id + "的记录不存在");
		model.addAttribute("demo", demo);
		return "path:viewDemo";
	}
	public @ResponseBody String updateDemo(Demo demo) 
	{
		String msg = null;
		try {
			demoServiceImpl.updateDemo(demo);
			msg = "success";
		} catch (Exception e) {
			msg = StringUtil.formatBRException(e);
		}
		return msg;
		
	}
	
	public @ResponseBody String deleteDemo(String ids) 
	{
		String msg = null;
		try {
			String[] ids_ = ids.split(",");
			demoServiceImpl.deleteDemo(ids_);
			msg = "success";
		} catch (Exception e) {
			msg = StringUtil.formatBRException(e);
		}
		return msg;
		
	}
	
	public String queryDemos(String name,@PagerParam(name = PagerParam.SORT, defaultvalue = "id") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,ModelMap model) 
	{
		String msg = null;
		try {
			ListInfo demos = demoServiceImpl.queryDemos(sortKey,desc,name,offset,pagesize);
			model.addAttribute("demos",demos);
			
		} catch (Exception e) {
			msg = StringUtil.formatBRException(e);
			model.addAttribute("errormsg",msg);
		}
		return "path:queryDemos";
		
	}
	

}
