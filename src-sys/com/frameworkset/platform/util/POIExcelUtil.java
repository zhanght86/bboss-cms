/*
 * @(#)POIExcelUtil.java
 * 
 * Copyright(c)2001-2012 SANY Heavy Industry Co.,Ltd
 * All right reserved.
 * 
 * 这个软件是属于三一重工股份有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一重工股份有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Heavy Industry Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Heavy Industry Co, Ltd.
 */
package com.frameworkset.platform.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.web.multipart.MultipartFile;

import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;

/**
 * 使用POI操作Excel文件
 * 
 * @todo
 * @author tanx
 * @date 2015年1月8日
 * 
 */
public class POIExcelUtil {

    public static final String EXCEL_TYPE_2003_CONTENT_TYPE = "ms-excel";

    public static final String EXCEL_TYPE_2007_CONTENT_TYPE = "openxmlformats-officedocument";

    /**
     * 创建Excel Workbook，使用传入的列定义和数据列表.
     * 
     * @param colDesc
     *            形如："工号:user_id,姓名:user_name,类型:type_name"
     * @param dataList
     * @return
     * @author gw_liaozh
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static XSSFWorkbook createHSSFWorkbook(List<String> titlesList, List<?> dataList)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        // 获取标题的英文名
        List<String> colFieldList = getColumnFieldList(titlesList);

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        XSSFFont font = getBaseFont(wb);
        XSSFCellStyle headCellStyle = getHeadCellStyle(wb, font);

        // 默认使用带时间的日期格式
        CellStyle dateCellStyle = getDateTimeCellStyle(wb);

        // 数据类型限制和校骄1�7�1�7
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        Map<String, Class<?>> fieldTypeMap = new HashMap<String, Class<?>>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        // 添加表头
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < titlesList.size(); i++) {
            XSSFCell cell = titleRow.createCell(i);
            cell.setCellStyle(headCellStyle);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(titlesList.get(i));
        }
        ClassInfo classInfo = null;
        // 添加数据列
        for (int i = 0; i < dataList.size(); i++) {
            Object obj = dataList.get(i);
            if(classInfo == null)
            	classInfo = ClassUtil.getClassInfo(obj.getClass());
            XSSFRow row = sheet.createRow(i + 1);
            for (int j = 0; j < colFieldList.size(); j++) {
                String fieldName = colFieldList.get(j);
                XSSFCell cell = row.createCell(j);
                if (obj == null) {
                    continue;
                }
                PropertieDescription reflexField = classInfo.getPropertyDescriptor(fieldName);
                Object value = reflexField.getValue(obj);
               
                // ClassInfo classInfo = ClassUtil.getClassInfo(obj.getClass());
                // Object value = classInfo.getPropertyDescriptor(fieldName).getValue(obj);
                if (value == null) {
                    continue;
                }
                // 根据类型设置格式
                if (value instanceof Number) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(((Number) value).doubleValue());
                } else if (value instanceof Date || value instanceof Timestamp) {
                    cell.setCellStyle(dateCellStyle);
                    cell.setCellValue(sdf.format((Date) value));
                } else {
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    // cell.setCellStyle(strCellStyle);
                    cell.setCellValue(value.toString());
                }
                fieldTypeMap.put(fieldName, value.getClass());
            }
        }

        // 设置数据类型限制
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

        // 自动调整列宽
        for (int i = 0; i < titlesList.size(); i++) {
            // 对中文处理不准确
            sheet.autoSizeColumn(i);
        }

        return wb;
    }

    /**
     * 获取标题行
     * 
     * @param uploadFileName
     * @return
     *         2015年1月12日
     */
    public static List<String> getTitlesList(InputStream inputStream) throws Exception {
        // 模板是excel2003
        POIFSFileSystem poiFs = new POIFSFileSystem(inputStream);
        HSSFWorkbook wb = new HSSFWorkbook(poiFs);

        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow titleRow = sheet.getRow(0);
        int colNum = titleRow.getLastCellNum();

        // 解析标题行
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < colNum; i++) {
            String title = titleRow.getCell(i).getStringCellValue();
            titleList.add(trimTitle(title));
        }

        return titleList;
    }

    /**
     * 获取标题英文字段
     * 
     * @param colDesc
     * @return
     *         2015年1月12日
     */
    private static List<String> getColumnFieldList(List<String> titleList) {
        List<String> colFieldList = new ArrayList<String>();
        for (int i = 0; i < titleList.size(); i++) {
            String title = titleList.get(i);

            // 截取英文字段名如：公司代码(companyId)
            colFieldList.add(title.substring(title.indexOf("(") + 1, title.indexOf(")")));
        }
        return colFieldList;
    }

    /**
     * 解析Excel文件，存成MapList，根据Excel单元格类型转换相应Java类型.
     * 
     * @param uploadFileName
     *            上传文件
     * @param titleList
     *            列字段对应数据库字段名称
     * @param beanType
     *            解析类型
     * @return
     * @throws Exception
     *             2015年7月23日
     */
    public static <T> List<T> parseExcel(MultipartFile uploadFileName, List<String> titleList, Class<T> beanType)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        // 根据文件类型获取Workbook
        Workbook wb = getWorkbookByFileContentType(uploadFileName);

        // 定义对象集合（范型）
        List<T> datas = new ArrayList<T>();

        // 反射获取对象属性字段信息
        ClassInfo classInfo = ClassUtil.getClassInfo(beanType);

        // 获取第一个表格
        Sheet sheet = (Sheet) wb.getSheetAt(0);

        // 获取英文字段行
        int rowNum = sheet.getLastRowNum();
        Row titleRow = sheet.getRow(0);
        int colNum = titleRow.getLastCellNum();

        for (int i = 2; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue;
            }

            // 实例化范型对象
            T retObject = beanType.newInstance();

            for (int j = 0; j < colNum; j++) {

                Cell cell = row.getCell(j);

                // 反射获取字段属性
                PropertieDescription reflexField = classInfo.getPropertyDescriptor(titleList.get(j));
                if(reflexField == null)
                	continue;
                if (cell != null) {

                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    String dd = row.getCell(j).getStringCellValue().trim();

                    if (StringUtil.isNotEmpty(dd)) {

                        // 已后台实体bean类型为准（以Excel来转换有问题）
                        if (reflexField.getPropertyType().getName().equals("java.sql.Date")) {
                            // 日期类型
                            Date date = sdf.parse(dd);
                            reflexField.setValue(retObject, new java.sql.Date(date.getTime()));
                        } else {

                            reflexField
                                    .setValue(retObject, ValueObjectUtil.typeCast(dd, reflexField.getPropertyType()));
                        }

                    }
                }

            }

            datas.add(retObject);
        }

        return datas;
    }
    
    
    /**
     * 解析Excel文件，存成MapList，根据Excel单元格类型转换相应Java类型.
     * 
     * @param uploadFileName
     *            上传文件
     * @param titleList
     *            列字段对应数据库字段名称
     * @param beanType
     *            解析类型
     * @return
     * @throws Exception
     *             2015年7月23日
     */
    public static <T> List<T> parseExcel(MultipartFile uploadFileName, int titlerow,int datarow, Class<T> beanType)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        // 根据文件类型获取Workbook
        Workbook wb = getWorkbookByFileContentType(uploadFileName);

        // 定义对象集合（范型）
        List<T> datas = new ArrayList<T>();

        // 反射获取对象属性字段信息
        ClassInfo classInfo = ClassUtil.getClassInfo(beanType);
        PropertieDescription rowidField = classInfo.getPropertyDescriptor("rowid");
        // 获取第一个表格
        Sheet sheet = (Sheet) wb.getSheetAt(0);

        // 获取英文字段行
        int rowNum = sheet.getLastRowNum();
        Row titleRow = sheet.getRow(titlerow);
        
        int colNum = titleRow.getLastCellNum();
        String[] titles = new String[colNum];
        for(int i = 0; i < colNum; i ++)
        {
        	titles[i] = titleRow.getCell(i).getStringCellValue().trim();
        }
        
        for (int i = datarow; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue;
            }
            
            

            // 实例化范型对象
            T retObject = beanType.newInstance();
            if(rowidField != null)
            	rowidField.setValue(retObject, i);
            for (int j = 0; j < colNum; j++) {

                Cell cell = row.getCell(j);

                // 反射获取字段属性
                PropertieDescription reflexField = classInfo.getPropertyDescriptor(titles[j]);
                if(reflexField == null)
                	continue;
                if (cell != null) {

                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    String dd = row.getCell(j).getStringCellValue().trim();

                    if (StringUtil.isNotEmpty(dd)) {

                        // 已后台实体bean类型为准（以Excel来转换有问题）
                        if (reflexField.getPropertyType().getName().equals("java.sql.Date")) {
                            // 日期类型
                            Date date = sdf.parse(dd);
                            reflexField.setValue(retObject, new java.sql.Date(date.getTime()));
                        } else {

                            reflexField
                                    .setValue(retObject, ValueObjectUtil.typeCast(dd, reflexField.getPropertyType()));
                        }

                    }
                }

            }

            datas.add(retObject);
        }

        return datas;
    }

    /**
     * 去掉标题中的换行等字符.
     * 
     * @param title
     * @return
     * @author liaozh <Aug 29, 2011>
     */
    private static String trimTitle(String title) {
        return StringUtils.replace(title, "\n", "");
    }

    /**
     * 根据文件类型获取Workbook
     * 
     * @param file
     *            MultipartFile
     * @return Workbook
     * @throws Exception
     */
    public static Workbook getWorkbookByFileContentType(MultipartFile file) throws Exception {
    	if(file.getOriginalFilename().endsWith("xlsx"))
    	{
    		return new XSSFWorkbook(file.getInputStream());
    	}
    	else if (file.getContentType().contains(EXCEL_TYPE_2003_CONTENT_TYPE)
                || file.getContentType().contains("octet-stream")) {
            return new HSSFWorkbook(file.getInputStream());
        } else if (file.getContentType().contains(EXCEL_TYPE_2007_CONTENT_TYPE)) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            return null;
        }

    }

    public static XSSFFont getBaseFont(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    public static XSSFCellStyle getHeadCellStyle(XSSFWorkbook wb, XSSFFont font) {
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

    private static CellStyle getDateTimeCellStyle(XSSFWorkbook wb) {// 用到
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        return cellStyle;
    }

}
