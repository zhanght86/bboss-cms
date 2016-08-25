package com.frameworkset.dictionary.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.frameworkset.dictionary.bean.Item;
import com.frameworkset.dictionary.service.DictManager;
import com.frameworkset.platform.util.POIExcelUtil2007;
/**
 * Description:字典管理控制器
 * @author qingl2
 * @version 2013-07-04
 */
public class DictController {
	private DictManager dictManager;
	/**
	 * Description:工具方法将数组转换为字符串
	 * @author qingl2
	 * @version 2013-07-04
	 */
		public String getExcelColDesc(String []desc) {
			List<String> colList = new ArrayList<String>();
			for(int i =0;i<desc.length;i++){
				colList.add(desc[i]);
			}
			return StringUtils.join(colList, ", ");
		}
		/**
		 * Description:字典内容下载
		 * @author qingl2
		 * @version 2013-07-04
		 */
		public void downloadExcel(String did,HttpServletResponse response){
			  String[] dicDesc = {"名称:dictDataName","真实值:dictDataValue"};
			try{
				List<Item> itemList = dictManager.getDictItemsById(did);  //获得字典内容
				List<String> dictTypeList = dictManager.getDictTypeById(did);
				String dictType = dictTypeList.size()>0?dictTypeList.get(0):"";//获得字典名称
				String colDesc = getExcelColDesc(dicDesc);//生成表头字符串
				XSSFWorkbook wb = POIExcelUtil2007.createHSSFWorkbook(colDesc, itemList);//生成Excel

				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new URLCodec().encode(dictType+"字典清单.xlsx"));
				wb.write(response.getOutputStream());
			}catch(Exception e){
				e.printStackTrace();
				
			}

		}

}
