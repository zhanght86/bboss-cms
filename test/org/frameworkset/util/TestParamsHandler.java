package org.frameworkset.util;

import java.util.List;

import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.util.ParamsHandler.Params;
import org.junit.Test;

public class TestParamsHandler {
	
	@Test
	public void testSaveParams(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		Params p = new Params();
		p.addAttribute(paramType, paramid, "name1", 1,0);
		p.addAttribute(paramType, paramid, "name1", 2,1);
		p.addAttribute(paramType, paramid, "name1", 3,2);
		p.addAttribute(paramType, paramid, "name2", true);
		p.addAttribute(paramType, paramid, "name3", "value3");
		p.addAttribute(paramType, paramid, "name4", "value1");
		p.addAttribute(paramType, paramid, "name5", "value1");
		p.addAttribute(paramType, paramid, "name6", 301);
		p.addAttribute(paramType, paramid, "businessId", 112);
		ParamsHandler.getParamsHandler("cim.paramshandler").saveParams(p);
		
	}
	
	@Test
	public void testSavesiteParams(){
		String paramid = "sanyGCMP";
		String paramType = "site";
		Params p = new Params();
		p.addAttribute(paramType, paramid, "docsearchcontext", "http://localhost:8081/SanyPDP");		
		ParamsHandler.getParamsHandler("cms.siteparamshandler").saveParams(p);
		
	}
	
	@Test
	public void testSaveBlobParams(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		Params p = new Params();
		p.addAttribute(paramType, paramid, "name1", 1,0);
		p.addAttribute(paramType, paramid, "name1", 2,1);
		p.addAttribute(paramType, paramid, "name1", 3,2);
		p.addAttribute(paramType, paramid, "name2", true);
		p.addAttribute(paramType, paramid, "name3", "value3");
		p.addAttribute(paramType, paramid, "name4", "value1");
		p.addAttribute(paramType, paramid, "name5", "value1");
		p.addAttribute(paramType, paramid, "name6", 301);
		p.addAttribute(paramType, paramid, "name7", new TestBlob(7));
		p.addAttribute(paramType, paramid, "name8", new TestBlob(81),1);
		p.addAttribute(paramType, paramid, "name8", new TestBlob(82),2);
		ParamsHandler.getParamsHandler("cim.paramshandler").saveParams(p);
	}
	
	@Test
	public void testGetBlobParams(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		Params p = ParamsHandler.getParamsHandler("cim.paramshandler").getParams(paramid, paramType);
		System.out.println("name1 1 = " + p.getAttributeString(1, "name1"));
		System.out.println("name1 0 = " + p.getAttributeString(0, "name1"));
		System.out.println("name1 2 = " + p.getAttributeString(2, "name1"));
		System.out.println("name2 = " + p.getAttributeBoolean("name2"));
		System.out.println("name3 = " + p.getAttributeString("name3"));
		System.out.println("name4 = " + p.getAttributeString("name4"));
		System.out.println("name5 = " + p.getAttributeString("name5"));
		System.out.println("name6 = " + p.getAttributeInteger("name6"));
		TestBlob name7 = (TestBlob)p.getAttributeObject("name7");
		System.out.println(name7);
		TestBlob name81 = (TestBlob)p.getAttributeObject(1,"name8");
		System.out.println(name81);
		TestBlob name82 = (TestBlob)p.getAttributeObject(2,"name8");
		System.out.println(name82);
		
	}
	
	@Test
	public void testGetAppBlobParams(){
		String paramid = "323a023a-8516-47c3-9dbd-afea40c23144";
		String paramType = "app";
		Params p = ParamsHandler.getParamsHandler("cms.siteparamshandler").getParams(paramid, paramType);
		
		System.out.println("asdfasdf = " + p.getAttributeString("asdfasdf"));
		System.out.println("asdf = " + p.getAttributeString("asdf"));
		
		
		
	}
	
	public static class TestBlob implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TestBlob(int i){
			this.i = i;
		}
		private int i = 10;
		
		public String toString(){
			return "TestBlob   " + String.valueOf(i);
		}
		
		
	}
	
	
	@Test
	public void testGetParams(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		Params p = ParamsHandler.getParamsHandler("cim.paramshandler").getParams(paramid, paramType);
		List<Param> datas = p.getParams();
		System.out.println("p = " + p);
		System.out.println(p.toString() == null);
		System.out.println(p.getClass());
		if(p.isEmpty()){
			System.out.println("p is null");
		}else{
			System.out.println("name1 1 = " + p.getAttributeString(1, "name1"));
			System.out.println("name1 0 = " + p.getAttributeString(0, "name1"));
			System.out.println("name1 2 = " + p.getAttributeString(2, "name1"));
			System.out.println("name2 = " + p.getAttributeBoolean("name2"));
			System.out.println("name3 = " + p.getAttributeString("name3"));
			System.out.println("name4 = " + p.getAttributeString("name4"));
			System.out.println("name5 = " + p.getAttributeString("name5"));
			System.out.println("name6 = " + p.getAttributeInteger("name6"));
		}
	}
	
	@Test
	public void testGetParam(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		String name = "test";
		Param p = ParamsHandler.getParamsHandler("cim.paramshandler").getParam(paramid,name, paramType);
		System.out.println("p = " + p);
		System.out.println(p.toString() == null);
		System.out.println(p.getClass());
		System.out.println(p.getValue());
	}
	
	@Test
	public void testDelParams(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		boolean state = ParamsHandler.getParamsHandler("cim.paramshandler").delParams(paramid, paramType);
		System.out.println(state?"删除成功！":"删除失败！");
	}
	/**
	 * 根据名称删除参数
	 */
	@Test
	public void testDelParam(){
		String paramid = "123";
		String paramType = "type.test.kettle";
		String paramName = "xxxx";
		boolean state = ParamsHandler.getParamsHandler("cim.paramshandler").delParam(paramid, paramName,paramType);
		System.out.println(state?"删除成功！":"删除失败！");
	}

}
