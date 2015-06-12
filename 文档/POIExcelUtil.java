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
package com.sany.allowance.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import org.apache.poi.ss.util.CellRangeAddress;
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

import com.frameworkset.platform.util.BeanConvertUtil;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;
import com.sany.allowance.entity.AllDataBean;
import com.sany.allowance.entity.ZhiFuBean;

import edu.emory.mathcs.backport.java.util.Arrays;

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

        // 添加数据列
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
     * @param file
     * @return
     * @throws IOException
     */
    public static <T> List<T> parseExcel(MultipartFile uploadFileName, Class<T> beanType) throws Exception {

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

        // 解析英文字段行
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < colNum; i++) {
            String title = titleRow.getCell(i).getStringCellValue();

            // 截取英文字段名如：公司代码(companyId)
            titleList.add(title.substring(title.indexOf("(") + 1, title.indexOf(")")));
        }

        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            // 实例化范型对象
            T retObject = beanType.newInstance();

            for (int j = 0; j < colNum; j++) {

                Cell cell = row.getCell(j);

                // 反射获取字段属性
                PropertieDescription reflexField = classInfo.getPropertyDescriptor(titleList.get(j));

                if (cell != null) {

                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            double d = cell.getNumericCellValue();
                            CellStyle style = cell.getCellStyle();

                            // 日期处理
                            if (HSSFDateUtil.isCellDateFormatted(cell)
                                    || (style != null && (style.getDataFormat() == 57 || style.getDataFormat() == 58))) {
                                reflexField.setValue(
                                        retObject,
                                        ValueObjectUtil.typeCast(HSSFDateUtil.getJavaDate(d),
                                                reflexField.getPropertyType()));

                            } else {
                                reflexField.setValue(retObject,
                                        ValueObjectUtil.typeCast(d, reflexField.getPropertyType()));
                            }
                            break;

                        default:
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);

                            String dd = row.getCell(j).getStringCellValue().trim();
                            if (StringUtil.isNotEmpty(dd)) {
                                reflexField.setValue(retObject,
                                        ValueObjectUtil.typeCast(dd, reflexField.getPropertyType()));
                            }

                            break;
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
        if (file.getContentType().contains(EXCEL_TYPE_2003_CONTENT_TYPE)
                || file.getContentType().contains("octet-stream")) {
            return new HSSFWorkbook(file.getInputStream());
        } else if (file.getContentType().contains(EXCEL_TYPE_2007_CONTENT_TYPE)) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            return null;
        }

    }

    private static XSSFFont getBaseFont(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    private static XSSFCellStyle getHeadCellStyle(XSSFWorkbook wb, XSSFFont font) {
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

    public static XSSFWorkbook dynamicCreateHSSFWorkbook(List<String> titlesList, Object[] obj) throws Exception {

        // 贴息汇总数据
        List<AllDataBean> allDataList = (List<AllDataBean>) obj[0];
        // 贴息汇总历史数据
        List<AllDataBean> hiAllDataList = (List<AllDataBean>) obj[1];
        // 租金支付数据
        List<ZhiFuBean> zhiFuList = (List<ZhiFuBean>) obj[2];
        // 贴息日期
        Date nowDate = (Date) obj[4];

        // 初始日期
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月");
        Date startDate = sdf1.parse(AllowanceConstant.INITIAL_DATE);

        // Calendar cal = Calendar.getInstance();
        // cal.setTime((Date) obj[4]);
        // cal.add(Calendar.MONTH, +1);
        // nowDate = cal.getTime();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        // 默认使用带时间的日期格式
        CellStyle dateCellStyle = getDateTimeCellStyle(wb);

        XSSFCellStyle headCellStyle = wb.createCellStyle(); // 样式对象
        headCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        headCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

        // 固定列长度
        int fixedCol = AllowanceConstant.TIEXIHZTITLENAME_ZN.length;
        int firstCol = fixedCol;// 初始化合并开始列的下标
        int lastCol = firstCol + 3;// 合并4列，合并后的下标

        // 动态列长度-历史数据标题
        int dynamicCol = AllowanceConstant.DYC_TIEXIHZTITLENAME_ZN.length;
        int nextFirstCol = 0;

        // 添加表头
        XSSFRow titleRow = sheet.createRow(0);
        XSSFRow dyctitleRow = sheet.createRow(1);
        for (int i = 0; i < titlesList.size(); i++) {

            // 判断是否到达动态列(动态列需要合并列)
            if (i >= fixedCol) {

                String titlename = titlesList.get(i);

                if (!titlename.contains("贴息计划")) {
                    // 历史数据动态列
                    XSSFCell cell = titleRow.createCell(firstCol);
                    cell.setCellStyle(headCellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(titlename);

                    // 第一行合并动态列
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, firstCol, lastCol));
                    // 第二行不合并，直接取标题
                    for (int j = 0; j < dynamicCol; j++) {

                        XSSFCell cells = dyctitleRow.createCell(firstCol + j);
                        cell.setCellStyle(headCellStyle);
                        cells.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cells.setCellValue(AllowanceConstant.DYC_TIEXIHZTITLENAME_ZN[j]);
                    }

                    firstCol = lastCol + 1;
                    lastCol = firstCol + 3;
                    nextFirstCol = firstCol;

                } else {
                    // 未来数据动态列
                    sheet.addMergedRegion(new CellRangeAddress(0, 1, nextFirstCol, nextFirstCol));
                    XSSFCell cell = titleRow.createCell(nextFirstCol++);
                    cell.setCellStyle(headCellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(titlesList.get(i));
                }

            } else {
                XSSFCell cell = titleRow.createCell(i);
                cell.setCellStyle(headCellStyle);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(titlesList.get(i));
                sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
            }
        }

        // 固定列英文名称
        List<String> entitlesList = Arrays.asList(AllowanceConstant.TIEXIHZTITLENAME_EN);

        // 当月贴息汇总数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < allDataList.size(); i++) {
            AllDataBean allDataBean = allDataList.get(i);
            XSSFRow row = sheet.createRow(i + 2);
            for (int j = 0; j < entitlesList.size(); j++) {
                String fieldName = entitlesList.get(j);
                XSSFCell cell = row.createCell(j);
                if (allDataBean == null) {
                    continue;
                }
                Object value = BeanConvertUtil.getProperty(allDataBean, fieldName);
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
                    cell.setCellValue(value.toString());
                }
                // fieldTypeMap.put(fieldName, value.getClass());
            }
        }

        // 历史汇总数据
        for (int i = 0; i < allDataList.size(); i++) {
            AllDataBean allDataBean = allDataList.get(i);
            XSSFRow row = sheet.getRow(i + 2);

            for (int j = 0; j < hiAllDataList.size(); j++) {
                AllDataBean hiAllDataBean = hiAllDataList.get(j);
                if (allDataBean.getCompanyId() == hiAllDataBean.getCompanyId()
                        && allDataBean.getAgreementId().equals(hiAllDataBean.getAgreementId())
                        && nowDate.after(hiAllDataBean.getAllowanceDate())
                        || nowDate.equals(hiAllDataBean.getAllowanceDate())) {

                    int cellLength = countCellPosition("1", hiAllDataBean.getAllowanceDate(), nowDate, startDate,
                            fixedCol);
                    // 本月到期贴息金额
                    XSSFCell cell = row.createCell(cellLength);
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(hiAllDataBean.getExpireInterestMoney());
                    // 本月申请支付金额
                    cell = row.createCell(cellLength + 1);
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(hiAllDataBean.getCurrMonthPayment());
                    // 执行结果
                    cell = row.createCell(cellLength + 2);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(hiAllDataBean.getExecuteResult());
                    // remarks
                    cell = row.createCell(cellLength + 3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(hiAllDataBean.getRemarks());
                }
            }

        }

        // 贴息计划数据
        for (int i = 0; i < allDataList.size(); i++) {
            AllDataBean allDataBean = allDataList.get(i);
            XSSFRow row = sheet.getRow(i + 2);
            
            if (allDataBean.getAllowancePolicy().equals("分月贴息")) {
                
                for (int j = 0; j < zhiFuList.size(); j++) {
                    ZhiFuBean zhiFuBean = zhiFuList.get(j);
                    if (allDataBean.getCompanyId() == zhiFuBean.getCompanyId()
                            && allDataBean.getAgreementId().equals(zhiFuBean.getAgreementId())
                            && nowDate.before(zhiFuBean.getPaymentYM())) {

                        int cellLength = countCellPosition("0", zhiFuBean.getPaymentYM(), nowDate, startDate, fixedCol);

                        XSSFCell cell = row.createCell(cellLength);
                        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        double waitPayment = zhiFuBean.getCurrInterest() * allDataBean.getAllowanceScale();
                        cell.setCellValue(waitPayment);
                    }
                }
                
            }else {
                
                double sumInterest = 0.0;// 利息总和
                for (ZhiFuBean zhifuBean : zhiFuList) {
                    if (zhifuBean.getAgreementId().equals(allDataBean.getAgreementId())
                            && zhifuBean.getCompanyId() == allDataBean.getCompanyId()) {
                        sumInterest += zhifuBean.getCurrInterest();
                    }
                }
                //贴息计划金额 = 利息总和*贴息比例 /租赁期限(期)
                double waitPayment = (sumInterest * allDataBean.getAllowanceScale() / allDataBean.getHireTerm());
                
                for (int j = 0; j < zhiFuList.size(); j++) {
                    ZhiFuBean zhiFuBean = zhiFuList.get(j);
                    if (allDataBean.getCompanyId() == zhiFuBean.getCompanyId()
                            && allDataBean.getAgreementId().equals(zhiFuBean.getAgreementId())
                            && nowDate.before(zhiFuBean.getPaymentYM())) {

                        int cellLength = countCellPosition("0", zhiFuBean.getPaymentYM(), nowDate, startDate, fixedCol);

                        XSSFCell cell = row.createCell(cellLength);
                        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(waitPayment);
                    }
                }
                
            }

        }

        return wb;

    }

    /**
     * 计算动态列字段位置
     * 
     * @param countType
     *            1计算历史汇总数据字段位置 0计算贴息计划数据字段位置
     * @param endDate
     * @param nowDate
     * @param startDate
     *            默认计算日期
     * @param fixedCol
     *            固定字段位置
     * @return
     * @throws Exception
     *             2015年3月25日
     */
    private static int countCellPosition(String countType, Date endDate, Date nowDate, Date startDate, int fixedCol)
            throws Exception {

        if (countType.equals("1")) {
            // 贴息汇总历史数据计算方式
            int diffMonth = countDiffMonth(startDate, endDate);

            return fixedCol + (diffMonth - 1) * 4;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(nowDate);
            cal.add(Calendar.MONTH, +1);
            nowDate = cal.getTime();
            // 贴息计划计算方式
            int diffMonth = countDiffMonth(startDate, nowDate);
            int tiexiPlanDiffMonth = countDiffMonth(nowDate, endDate) - 1;

            return fixedCol + (diffMonth - 1) * 4 + tiexiPlanDiffMonth;
        }

    }

    /**
     * 计算月份差
     * 
     * @param startDate
     * @param endDate
     * @return
     *         2015年1月27日
     */
    private static int countDiffMonth(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(startDate);
        int startYear = cal.get(Calendar.YEAR);
        int startMonth = cal.get(Calendar.MONTH);

        cal.setTime(endDate);
        int endYear = cal.get(Calendar.YEAR);
        int endMonth = cal.get(Calendar.MONTH);

        if (startYear == endYear) {
            return endMonth - startMonth + 1;
        } else {
            return 12 * (endYear - startYear) + endMonth - startMonth + 1;
        }

    }
}
