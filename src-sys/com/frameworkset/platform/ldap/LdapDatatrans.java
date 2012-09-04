package com.frameworkset.platform.ldap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

public class LdapDatatrans implements java.io.Serializable {

	//ldap里面的所有属性和对象里面的所有属性必须是小写的
	public BasicAttributes BeanToAttrs(Object object) throws Exception{
		BasicAttributes attrs = new BasicAttributes(); //条目的属性列表
		Method[] ms = object.getClass().getMethods();
	     for(int j = 0; j<ms.length; j++){

				String funname = ms[j].getName();
				if(funname.equalsIgnoreCase("getClass")){
					continue;
				}

				if(funname.toUpperCase().indexOf("GET")==0&&ms[j].getParameterTypes().length==0){  //B表示是get方法

					//取得属性名
					String fieldname = ms[j].getName().substring(3).toLowerCase();
					Method method =ms[j];

					//取得属性的值
					Object obj = method.invoke(object,null);
					if(obj==null) obj=String.valueOf("");
					attrs.put(fieldname,obj.toString());
				}
			}

		return attrs;
	}

	public void AttrsToBean(Attributes attrs,Object object) throws Exception{

		 for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements();) {

			 Attribute attr = (Attribute)ae.next();

			 //取得属性id和属性值 这里只取第一个值 因为用户信息不存在一个类型对应多个值的概念
			 String attrId = attr.getID().toLowerCase();
			 String attrValue = attr.getAll().nextElement().toString();

			 if(attrId.equals("objectclass")){
				 continue;
			 }

			 //取得方法名
			 String fieldname = attrId;
			 String funname = "set"+fieldname.substring(0, 1).toUpperCase()
				+ fieldname.substring(1, fieldname.length());

			 //取得set方法的参数类型
			 Field field = object.getClass().getDeclaredField(fieldname);
			 Class[] ptypes = new Class[1];
			 // ptypes[0] = srcmap.get(fieldname).getClass();
			 ptypes[0] = field.getType();

			 Method method = object.getClass().getMethod(funname,ptypes);

			 //执行set方法
			 Object[] args = new Object[1];
			 args[0] = attrValue;
			 method.invoke(object, args);
		}

	}


	//根据一个已经修改后的对象创建ModificationItem[] 用于update操作
	public ModificationItem[] createModificationItems(Object srcobj) throws Exception{

		Method[] ms = srcobj.getClass().getMethods();
		ArrayList list = new ArrayList();

		// ModificationItem[] modificationItem = new ModificationItem[];
	     for(int j = 0; j<ms.length; j++){

				String funname = ms[j].getName();
				if(funname.equalsIgnoreCase("getClass")){
					continue;
				}


				if(funname.toUpperCase().indexOf("GET")==0&&ms[j].getParameterTypes().length==0){  //B表示是get方法

					//取得属性名
					String fieldname = ms[j].getName().substring(3).toLowerCase();
					Method method =ms[j];

					//取得属性的值
					Object obj = method.invoke(srcobj,null);
					if(obj==null) obj=String.valueOf("");
					ModificationItem modificationItem =
		    	          new ModificationItem(
		    	          DirContext.REPLACE_ATTRIBUTE,
		    	          new BasicAttribute(fieldname, obj.toString()));
					list.add(modificationItem);
				}
			}
	     ModificationItem[] modificationItem = new ModificationItem[list.size()];
	     for(int i=0; i< list.size(); i++){
	    	 modificationItem[i] = (ModificationItem)list.get(i);
	     }
	     return modificationItem;

	}

}
