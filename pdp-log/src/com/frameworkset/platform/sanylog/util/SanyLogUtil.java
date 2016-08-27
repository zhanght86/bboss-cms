package com.frameworkset.platform.sanylog.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.frameworkset.util.ClassWrapper;

import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.PageList;
import com.frameworkset.platform.sanylog.dictionary.Dictionary;
import com.frameworkset.platform.sanylog.service.FunctionListManager;
import com.frameworkset.platform.sanylog.service.PageListManager;


public class SanyLogUtil {
	private FunctionListManager functionListManager;
	private PageListManager pageListManager;
	
	private String check (List<?> datas,String className,String []values,String []infos)throws Exception{
		StringBuffer messages = new StringBuffer();
		 boolean failed = false;
		for (int i = 0; i < datas.size(); i++) {
			Class<?> clazz = Class.forName(className);
			Object subject = clazz.newInstance();
			subject = datas.get(i);
			ClassWrapper beanWrapper = new ClassWrapper(subject);
			String error = (String)beanWrapper.getPropertyValue("error");
			 //messages = error == null ? new StringBuffer(): new StringBuffer().append(error);
			 if(null!=error||!"".equals(error)){
					 messages.append(error);
			 }
			for(int j =0;j<values.length;j++){
				if("estimateUser".equals(values[j])||"estimateOper".equals(values[j])){
					float value = (Float) beanWrapper.getPropertyValue(values[j]);
							//Float.parseFloat((String)beanWrapper.getPropertyValue(values[j]));
					if (0==value ) {
						messages.append("第" + (i + 1) +"行"+infos[j]+ "为空或0;");
						failed = true;
					}
				}else{
					String value = (String)beanWrapper.getPropertyValue(values[j]);
					if (value == null || "".equals(value)) {
						messages.append("第" + (i + 1) +"行"+infos[j]+ "为空;");
						failed = true;
					}
				}
				
			}
		}
		if(failed)
			
		{    return messages.append("<br/>").toString();
		}
		return null;
	}
	
	
	
	@SuppressWarnings("null")
	public List<Map<String, String>> readExcel(String fileFullName,int cellMax)throws Exception{

		InputStream ins = null;
		Workbook workbook = null;
		List<Map<String, String>> dataGroupList =new ArrayList<Map<String, String>> ();
		boolean hasError = false;
		try {
			ins = new FileInputStream(fileFullName);
			workbook = WorkbookFactory.create(ins);
			Sheet sheet = workbook.getSheetAt(0);// 
			Iterator<Row> rit = sheet.iterator();// 
			Integer row = 0;

			while (rit.hasNext()) {
				Map<String, String> lineMap = getDataByRow(rit,cellMax);
				if (row == 0) {//
				} else {
					
					dataGroupList.add(lineMap);
				}
				System.out.println("-------------row---------------"+row);
				row++;
			}
			ins.close();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
		if (hasError) {
			return null;
		}
		return dataGroupList;
		
	}
	public Map<String, String> getDataByRow(Iterator<Row> rit,int cellMax) {
		DecimalFormat decimalFormat = new DecimalFormat("#.####");

		Map<String, String> lineMap;
		Row row;
		Cell cell;
		row = rit.next();
		lineMap = new ConcurrentHashMap<String, String>(); 
		for (int i = 0; i <= cellMax; i++) {
			cell = row.getCell(i);
			String k = ""; 
			if (cell == null) {
				lineMap.put(String.valueOf((i)), k); 
				continue;
			}
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				k = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				k = Byte.toString(cell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				k = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					k = sdf.format(cell.getDateCellValue());
				} else {
					// k = Long.toString((long) cell.getNumericCellValue());
					k = decimalFormat.format(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				k = Boolean.toString(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				k = cell.getCellFormula();
				break;
			default:
				k = "";
			}
			if ((k != null) && (!"".equals(k))) {
            	lineMap.put(String.valueOf((i)), k); 

			} else {
				lineMap.put(String.valueOf((i)), "");
			}

		}
		return lineMap;
	}
	public Object copyMap(Map map,int row,String className,String[] keys,String[] values,String[] infos){
		StringBuffer error = new StringBuffer();
		String rownum = String.valueOf(row);
		
		
		try{
			Class<?> clazz = Class.forName(className);
			Object subject = clazz.newInstance();
			ClassWrapper beanWrapper = new ClassWrapper(subject);
			for(int i = 0;i<keys.length;i++){
				try {
//	此句性能影响比较大   clazz.getMethod("set"+values[i],String.class).invoke(subject,map.get(keys[i]).toString());   
					if("estimateUser".equals(values[i])||"estimateOper".equals(values[i])){
						beanWrapper.setPropertyValue(values[i], Float.parseFloat(map.get(keys[i]).toString()));
					}else{
						beanWrapper.setPropertyValue(values[i], map.get(keys[i]).toString());
					}
					
				} catch (Exception e) {
					String mes = e.getMessage();
					if(null == mes ||"".equals(mes)){
						error.append(infos[i]+"数据异常;");
					}else{
						error.append(mes).append(";");
					}
					
				}
				
			}
			if(null != error.toString() && !"".equals(error.toString())){
//		此句性能影响比较大   	clazz.getMethod("setError",String.class).invoke(clazz.newInstance(),"第"+rownum+"行"+error.toString()+"<br/>");
				beanWrapper.setPropertyValue("error","第"+rownum+"行"+error.toString()+"<br/>");
			}
			return subject;
		}catch(Exception e){
			return null;
		}
	}
	public String getExcelColDesc(String []desc) {
		List<String> colList = new ArrayList<String>();
		for(int i =0;i<desc.length;i++){
			colList.add(desc[i]);
		}
		return StringUtils.join(colList, ", ");
	}
	
	public Map batchImportFunctionList(String filePath, String fileName,String fileNameOrig) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		List<FunctionList> dataGroupList = new ArrayList<FunctionList>();
		try {
			StringBuffer error = new StringBuffer();
			List<Map<String,String>> excelList = readExcel(filePath + fileName,Dictionary.FunctionListExcelNum);
			for(int j =0;j<excelList.size();j++){
				FunctionList subject = (FunctionList)copyMap(excelList.get(j),j,Dictionary.FunctionList,Dictionary.FunctionListExcelKeys,Dictionary.FunctionListExcelValues,Dictionary.FunctionListExcelInfos);
				if(null!=subject.getError()&&!"".equals(subject.getError())){
					error.append(subject.getError());
					
				}
				dataGroupList.add(subject);
			}
            
			if (dataGroupList .size()==0) {
				ret.put("msg", "读取EXCEL为空");
				return ret;
			}
			
			if(null!=error.toString()&&!"".equals(error.toString())){
				ret.put("msg", "读取EXCEL报错"+error.toString());
				return ret;
			}
			String errorMessage = check(dataGroupList,Dictionary.FunctionList,Dictionary.SubjectExcelNotNullValues,Dictionary.SubjectExcelNotNullInfos);
			if(null!=errorMessage && !"".equals(errorMessage)){
				ret.put("msg", "导入失败："+errorMessage);
				return ret;
			}else{//检查导入的数据里面是否有重复的数据
					String duplicateMessage = checkDuplicate(dataGroupList);
					 if(null ==duplicateMessage||"".equals(duplicateMessage)){
						 //获取数据库中所有科目的代码
						 List<FunctionList> functionListInDB = functionListManager.getFunctionListInDB();
						 //如果数据库中为空，则直接插入EXCEL的值
						 if(null ==functionListInDB||functionListInDB.size()==0){
							 functionListManager.batchInsertFunctionList(dataGroupList,functionListInDB);
							 ret.put("msg", "导入成功");
							 return ret;
						 }else{//如果不为空，则检查EXCEL中的数据与数据库中的科目是否有重复的情况
							 String duplicateMessage4DB =  checkDuplicateInDB(dataGroupList,functionListInDB);
							 if(null == duplicateMessage4DB||"".equals(duplicateMessage4DB)){
								 functionListManager.batchInsertFunctionList(dataGroupList,functionListInDB);
								 ret.put("msg", "导入成功");
								 return ret;
							    }else{
							    	ret.put("msg", "导入失败："+duplicateMessage4DB);
									 return ret;
							    }
						 }
						    
					 }else{
						 ret.put("msg", "导入失败："+duplicateMessage);
						 return ret;
					 }
				
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	/*public Log setLog(String type,String tableName ,String detailInfo){
		Log log = new Log();
		log.setId(UUID.randomUUID().toString());
		log.setOperator(AccessControl.getAccessControl().getUserAccount());
		log.setDetailInfo(detailInfo);
		log.setTableName(tableName);
		log.setType(type);
		return log;
	};*/
	public String checkDuplicateInDB(List<FunctionList> excelDatas,List<FunctionList> DBDatas){
		String appName = null;
		String functionCode = null;
		 boolean duplicate =false;
		 for(int i=0;i<excelDatas.size();i++){
			 String appNameTemp = excelDatas.get(i).getAppName();
				String functionCodeTemp = excelDatas.get(i).getFunctionCode();
			 for(int j=0;j<DBDatas.size();j++){
				 if(appNameTemp.equals(DBDatas.get(j).getAppName())&&functionCodeTemp.equals(DBDatas.get(j).getFunctionCode())){
						appName = appNameTemp;
						functionCode = functionCodeTemp;
						duplicate = true;
						break;
					}
			 } 
			 if(duplicate){
					break;
				}
		 }
		 if(duplicate){
				return "EXCEL中项目名称为:"+appName+",功能编号为:"+functionCode+"的记录与数据库中的记录重复";
			}
			return "";
	}
	//检查导入的数据里面是否有重复的科目
	public String checkDuplicate(List<FunctionList> datas){
		String appName = null;
		String functionCode = null;
		boolean duplicate = false;
		for(int i =0;i<datas.size()-1;i++){
			String appNameTemp = datas.get(i).getAppName();
			String functionCodeTemp = datas.get(i).getFunctionCode();
			for(int j=i+1;j<datas.size();j++){
				if(appNameTemp.equals(datas.get(j).getAppName())&&functionCodeTemp.equals(datas.get(j).getFunctionCode())){
					appName = appNameTemp;
					functionCode = functionCodeTemp;
					duplicate = true;
					break;
				}
			}
			if(duplicate){
				break;
			}
		}
		if(duplicate){
			return "EXCEL中项目名称为:"+appName+",功能编号为:"+functionCode+"的记录重复";
		}
		return "";
	}
	public Map batchImportPageList(String filePath, String fileName,String fileNameOrig,String appId) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		List<PageList> dataGroupList = new ArrayList<PageList>();
		try {
			StringBuffer error = new StringBuffer();
			List<Map<String,String>> excelList = readExcel(filePath + fileName,Dictionary.PageListExcelNum);
			for(int j =0;j<excelList.size();j++){
				PageList subject = (PageList)copyMap(excelList.get(j),j,Dictionary.PageList,Dictionary.PageListExcelKeys,Dictionary.PageListExcelValues,Dictionary.PageListExcelInfos);
				if(null!=subject.getError()&&!"".equals(subject.getError())){
					error.append(subject.getError());
					
				}
				dataGroupList.add(subject);
			}
            
			if (dataGroupList .size()==0) {
				ret.put("msg", "读取EXCEL为空");
				return ret;
			}
			
			if(null!=error.toString()&&!"".equals(error.toString())){
				ret.put("msg", "读取EXCEL报错"+error.toString());
				return ret;
			}
			String errorMessage = check(dataGroupList,Dictionary.PageList,Dictionary.PageListSubjectExcelNotNullValues,Dictionary.PageListSubjectExcelNotNullInfos);
			if(null!=errorMessage && !"".equals(errorMessage)){
				ret.put("msg", "导入失败："+errorMessage);
				return ret;
			}else{
				pageListManager.updateBatchPageList(dataGroupList,appId);
				ret.put("msg", "导入成功!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
		

}
