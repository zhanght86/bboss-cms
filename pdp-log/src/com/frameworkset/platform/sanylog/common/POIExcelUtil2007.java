/*
 * @(#)POIExcelUtil.java
 * 
 * Copyright(c)2001-2012 SANY Heavy Industry Co.,Ltd
 * All right reserved.
 * 
 * 这个软件是属于三丄1�7�1�7�工股份有限公司机密的和私有信息，不得泄露�1ￄ1�7�1�7�1�7
 * 并且只能由三丄1�7�1�7�工股份有限公司内部员工在得到许可的情况下才允许使用〄1�7�1�7
 * This software is the confidential and proprietary information 
 * of SANY Heavy Industry Co, Ltd. You shall not disclose such 
 * Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with 
 * SANY Heavy Industry Co, Ltd.
 */
package com.frameworkset.platform.sanylog.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.frameworkset.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;;
/**
 * 使用POI读写Excel文件的快捷方泄1�7�1�7
 * 
 * @author gw_liaozh
 */
public class POIExcelUtil2007 {

	/**
	 * 为Sheet的某些列添加数据校验限制.
	 * 
	 * @param sheet
	 * @param dvcMap 键为列描述中的fieldName，如"type_name"
	 * @param colDesc 形如＄1�7�1�7工号:user_id,姓名:user_name,类型:type_name"
	 * @author gw_liaozh
	 */
	/*public static void addDataValidationConstraints(HSSFSheet sheet, Map<String, DataValidationConstraint> dvcMap,
			String colDesc) {//没有用到
		List<String> colFieldList = getColumnFieldList(colDesc);
		HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(sheet);
		//设置数据类型限制
		for (int i = 0; i < colFieldList.size(); i++) {
			String fieldName = colFieldList.get(i);
			DataValidationConstraint constraint = dvcMap.get(fieldName);
			if (constraint == null) {
				continue;
			}
			CellRangeAddressList range = new CellRangeAddressList(1, 65535, i, i);
			sheet.addValidationData(dvHelper.createValidation(constraint, range));
		}
	}*/

	/**
	 * 创建下拉框�1ￄ1�7�择的数据校验限刄1�7�1�7
	 * 
	 * @param sheet
	 * @param dataList
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @author gw_liaozh
	 */
	/*public static DataValidationConstraint createExplicitListConstraint(HSSFSheet sheet, List<?> dataList,
			String fieldName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//没有用到
		HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(sheet);
		List<String> strList = new ArrayList<String>();
		for (Object obj : dataList) {
			Object value = BeanConvertUtil.getProperty(obj, fieldName);
			//ClassInfo classInfo = ClassUtil.getClassInfo(obj.getClass());
			//Object value = classInfo.getPropertyDescriptor(fieldName).getValue(obj);
			if (value != null) {
				strList.add(value.toString());
			}
		}
		return dvHelper.createExplicitListConstraint(strList.toArray(new String[strList.size()]));
	}*/

	/**
	 * 创建Excel Workbook，使用传入的列定义和数据列表.
	 * 
	 * @param colDesc 形如＄1�7�1�7工号:user_id,姓名:user_name,类型:type_name"
	 * @param dataList
	 * @return
	 * @author gw_liaozh
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static XSSFWorkbook createHSSFWorkbook(String colDesc, List<?> dataList) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		//解析列描迄1�7�1�7
		//TODO: 支持多层树状结构
		List<String> colTitleList = getColumnTitleList(colDesc);
		List<String> colFieldList = getColumnFieldList(colDesc);

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();

		XSSFFont font = getBaseFont(wb);
		XSSFCellStyle headCellStyle = getHeadCellStyle(wb, font);

		//默认使用带时间的日期格式
		CellStyle dateCellStyle = getDateTimeCellStyle(wb);
		
		//CellStyle strCellStyle = getStringCellStyle(wb);

		//数据类型限制和校骄1�7�1�7
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		Map<String, Class<?>> fieldTypeMap = new HashMap<String, Class<?>>();

		//添加表头
		XSSFRow titleRow = sheet.createRow(0);
		for (int i = 0; i < colTitleList.size(); i++) {
			XSSFCell cell = titleRow.createCell(i);
			cell.setCellStyle(headCellStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(colTitleList.get(i));
		}

		//添加数据刄1�7�1�7
		for (int i = 0; i < dataList.size(); i++) {
			Object obj = dataList.get(i);
			XSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < colFieldList.size(); j++) {
				String fieldName = colFieldList.get(j);
				XSSFCell cell = row.createCell(j);
				if (obj == null) {
					continue;
				}
				Object value = BeanConvertUtil.getProperty(obj, fieldName);
				//ClassInfo classInfo = ClassUtil.getClassInfo(obj.getClass());
				//Object value = classInfo.getPropertyDescriptor(fieldName).getValue(obj);
				if (value == null) {
					continue;
				}
				//根据类型设置格式
				if (value instanceof Number) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(((Number) value).doubleValue());
				} else if (value instanceof Date) {
					cell.setCellStyle(dateCellStyle);
					cell.setCellValue((Date) value);
				} else {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					//cell.setCellStyle(strCellStyle);
					cell.setCellValue(value.toString());
				}
				fieldTypeMap.put(fieldName, value.getClass());
			}
		}

		//设置数据类型限制
		for (int i = 0; i < colFieldList.size(); i++) {
			String fieldName = colFieldList.get(i);
			Class<?> fieldClass = fieldTypeMap.get(fieldName);
			if (fieldClass == null) {
				continue;
			}
			CellRangeAddressList range = new CellRangeAddressList(1, 65535, i, i);
			DataValidationConstraint constraint = null;
			if (Integer.class.isAssignableFrom(fieldClass)) {
				constraint = dvHelper.createIntegerConstraint(DataValidationConstraint.OperatorType.NOT_BETWEEN, "0",
						"-1");
				sheet.addValidationData(dvHelper.createValidation(constraint, range));
			} else if (Number.class.isAssignableFrom(fieldClass)) {
				constraint = dvHelper.createNumericConstraint(DataValidationConstraint.ValidationType.DECIMAL,
						DataValidationConstraint.OperatorType.NOT_BETWEEN, "0", "-1");
				sheet.addValidationData(dvHelper.createValidation(constraint, range));
			} else if (Date.class.isAssignableFrom(fieldClass)) {
				constraint = dvHelper.createDateConstraint(DataValidationConstraint.OperatorType.NOT_BETWEEN,
						"0000-01-02", "0000-01-01", "yyyy-MM-dd");
				sheet.addValidationData(dvHelper.createValidation(constraint, range));
			}
		}

		//自动调整列宽
		for (int i = 0; i < colTitleList.size(); i++) {
			//对中文处理不准确
			//sheet.autoSizeColumn(i);
		}

		return wb;
	}

	private static XSSFFont getBaseFont(XSSFWorkbook wb) {//用到
		XSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		return font;
	}

	private static List<String> getColumnFieldList(String colDesc) {//用到
		List<String> fieldList = new ArrayList<String>();
		for (String col : StringUtils.split(colDesc, ',')) {
			String[] titleFieldArr = StringUtils.split(col, ':');
			fieldList.add(titleFieldArr[1].trim());
		}
		return fieldList;
	}

	private static List<String> getColumnTitleList(String colDesc) {//用到
		List<String> titleList = new ArrayList<String>();
		for (String col : StringUtils.split(colDesc, ',')) {
			String[] titleFieldArr = StringUtils.split(col, ':');
			titleList.add(titleFieldArr[0].trim());
		}
		return titleList;
	}

	private static CellStyle getDateTimeCellStyle(XSSFWorkbook wb) {//用到
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		return cellStyle;
	}

	/*@SuppressWarnings("unused")
	private static CellStyle getStringCellStyle(HSSFWorkbook wb) {//没有用到
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		return cellStyle;
	}*/

	private static XSSFCellStyle getHeadCellStyle(XSSFWorkbook wb, XSSFFont font) {//用到
		XSSFCellStyle headCellStyle = wb.createCellStyle();
		headCellStyle.setFont(font);
		headCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		headCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headCellStyle.setWrapText(false);
		headCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return headCellStyle;
	}

	/**
	 * 解析Excel文件，存成MapList，根据Excel单元格类型转换相应Java类型.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	/*public static List<Map<String, Object>> parseHSSFMapList(MultipartFile file) throws IOException {//没有用到
		POIFSFileSystem poiFs = new POIFSFileSystem(file.getInputStream());
		HSSFWorkbook wb = new HSSFWorkbook(poiFs);
		
		HSSFSheet sheet = wb.getSheetAt(0);
		
		int rowNum = sheet.getLastRowNum();
		HSSFRow titleRow = sheet.getRow(0);
		
		int colNum = titleRow.getLastCellNum();
		
		//解析标题衄1�7�1�7
		List<String> titleList = new ArrayList<String>();
		for (int i = 0; i < colNum; i++) {
			String title = titleRow.getCell(i).getStringCellValue();
			titleList.add(trimTitle(title));
		}
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			for (int j = 0; j < colNum; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell != null) {
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						double d = cell.getNumericCellValue();
						CellStyle style = cell.getCellStyle();
						//日期处理
						if (HSSFDateUtil.isCellDateFormatted(cell)
								|| (style != null && (style.getDataFormat() == 57 || style.getDataFormat() == 58))) {
							map.put(titleList.get(j), HSSFDateUtil.getJavaDate(d));
						} else {
							map.put(titleList.get(j), d);
						}
						break;

					default:
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						map.put(titleList.get(j), row.getCell(j).getStringCellValue());
						break;
					}
				} else {
					map.put(titleList.get(j), null);
				}
			}
			mapList.add(map);
		}

		return mapList;
	}

	*//**
	 * 去掉标题中的换行等字笄1�7�1�7
	 * 
	 * @param title
	 * @return
	 * @author liaozh <Aug 29, 2011>
	 *//*
	private static String trimTitle(String title) {//没有用到
		return StringUtils.replace(title, "\n", "");
	}*/

}
