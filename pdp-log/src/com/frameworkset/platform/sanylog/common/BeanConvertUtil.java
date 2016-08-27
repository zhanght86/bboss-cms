/*
 * @(#)BeanConvertUtil.java
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
package com.frameworkset.platform.sanylog.common;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;

/**
 * BeanConvertUtil.java
 * 
 * Bean转换快捷工具.
 * 
 * @author          gw_liaozh
 * @company         SANY Heavy Industry Co, Ltd
 * @creation date   2012-7-30
 * @version         $Revision: 3 $
 */
public class BeanConvertUtil {

  /**
   * 把Map转换成Bean，根据字段映射.
   * 
   * @param clazz
   * @param dataMap 数据Map
   * @param fieldMap 键为Map的键名，值为fieldName
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException 
   */
  public static <T> T convert(Class<T> clazz, Map<String, Object> dataMap, Map<String, String> fieldMap) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException {
    T obj = clazz.newInstance();
    ClassInfo classInfo = ClassUtil.getClassInfo(clazz);
    for (String mapKey : fieldMap.keySet()) {
      Object value = dataMap.get(mapKey);
      String fieldName = fieldMap.get(mapKey);
      if (value != null) {
        //classInfo.getDeclaredField(fieldName).set(obj, value);
        PropertieDescription pd = classInfo.getPropertyDescriptor(fieldName);
        //做类型转换
        Class<?> pclazz = pd.getPropertyType();

        if (Timestamp.class.isAssignableFrom(pclazz)) {
          if (value instanceof Date) {
            pd.setValue(obj, new Timestamp(((Date) value).getTime()));
          }
        } else if (Date.class.isAssignableFrom(pclazz)) {
          //日期没有使用String的构造方法
          if (value instanceof Date) {
            pd.setValue(obj, value);
          }
        } else {
          Object targetValue = pclazz.getConstructor(String.class).newInstance(value.toString());
          if (String.class.isAssignableFrom(pclazz) && value instanceof Number) {
            targetValue = new DecimalFormat("0").format(value);
          }
          pd.setValue(obj, targetValue);
        }
        //PropertyUtils.setProperty(obj, fieldName, value);
      }
    }
    return obj;
  }

  /**
   * 获取属性值，支持Bean和Map两种类型.
   * 
   * @param obj
   * @param fieldName
   * @return
   * @throws InvocationTargetException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  public static Object getProperty(Object obj, String fieldName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    if (obj == null) {
      throw new IllegalArgumentException("obj is null");
    }
    if (obj instanceof Map) {
      return ((Map<?, ?>) obj).get(fieldName);
    } else {
      ClassInfo classInfo = ClassUtil.getClassInfo(obj.getClass());
      return classInfo.getPropertyDescriptor(fieldName).getValue(obj);
    }
  }

  /**
   * 把List映射成Map，根据指定字段.
   * 
   * @param beanList
   * @param fieldName
   * @param clazz
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static <T> Map<String, T> map(List<T> beanList, String fieldName, Class<T> clazz) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    Map<String, T> map = new HashMap<String, T>();
    ClassInfo classInfo = ClassUtil.getClassInfo(clazz);
    for (T obj : beanList) {
      Object value = classInfo.getPropertyDescriptor(fieldName).getValue(obj);
      if (value != null) {
        map.put(value.toString(), obj);
      }
    }
    return map;
  }

  /**
   * 把映射描述字符串映射成Map.
   * 
   * @param fieldDesc 形如："工号:user_id,姓名:user_name,类型:type_name"
   * @return
   */
  public static Map<String, String> map(String fieldDesc) {
    Map<String, String> map = new HashMap<String, String>();
    String[] fieldArr = StringUtils.split(fieldDesc, ',');
    for (String field : fieldArr) {
      String[] titleFieldArr = StringUtils.split(field, ':');
      map.put(titleFieldArr[0].trim(), titleFieldArr[1].trim());
    }
    return map;
  }

}
