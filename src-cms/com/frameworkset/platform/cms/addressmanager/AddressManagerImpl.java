//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManagerImpl.java
package com.frameworkset.platform.cms.addressmanager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.frameworkset.platform.cms.templatemanager.TemplateStyleInfo;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.ListInfo;


/**
 * 站点管理
 * @author huaihai.ou
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public class AddressManagerImpl implements AddressManager {

	public boolean creatorAddress(Address address) throws AddressManagerException {
		String sql = " insert into td_cms_person_addressbook(user_id,person_name,person_birthday,person_email," +
				"person_mobiletel1,person_mobiletel2,person_qicq,person_web,person_homeaddress," +
				"person_postalcode,person_hometel,person_company,person_coaddress,person_dept," +
				"person_duty,person_worktel,person_fax,person_remark) values('" +
				address.getUserId()+"','"+address.getPersonName()+"',";
		if(null!=address.getPersonBirthday() && !"".equals(address.getPersonBirthday())){
			sql += "to_date('"+address.getPersonBirthday()+"','yyyy-mm-dd'),";
		}else{
			sql += "'',";
		}
		sql += "'"+address.getPersonEmail()+"','"+address.getPersonMobileTel1()+"','"+address.getPersonMobileTel2()
		+"','"+address.getPersonQicq()+"','"+address.getPersonWeb()+"','"+address.getPersonHomeAddress()
		+"','"+address.getPersonPostalCode()+"','"+address.getPersonHomeTel()+"','"+address.getPersonCompany()
		+"','"+address.getPersonCoAddress()+"','"+address.getPersonDept()+"','"+address.getPersonDuty()
		+"','"+address.getPersonWorkTel()+"','"+address.getPersonFax()+"','"+address.getPersonRemark()+"')";
		DBUtil db = new DBUtil();
		try{
			Object o = db.executeInsert(sql);
			if(o!=null) return true ;
		}catch(Exception e){
			throw new AddressManagerException();
		}
		return false;
	}

	public boolean deleteAddress(String[] addressBookIds) throws AddressManagerException {
		DBUtil db = new DBUtil();
		try{
			for(int i=0;i<addressBookIds.length;i++){
				String sql = " delete td_cms_person_addressbook where addressbook_id = "+addressBookIds[i];
				db.executeDelete(sql);
			}
			return true;
		}catch (Exception e){
			throw new AddressManagerException();
		}
	}

	public ListInfo getAddressList(String sql, int offset, int maxItem) throws AddressManagerException {
		DBUtil db = new DBUtil();
		ListInfo listInfo = new ListInfo();
		List tempList = new ArrayList();
		try{	
			db.executeSelect(sql,offset,maxItem);
			
			for(int i=0;i<db.size();i++){
				Address address = new Address();
				address.setAddressBookId(db.getString(i,"ADDRESSBOOK_ID"));
				address.setUserId(db.getString(i,"USER_ID"));
				address.setPersonName(db.getString(i,"PERSON_NAME"));
				address.setPersonBirthday(db.getString(i,"PERSON_BIRTHDAY"));
				address.setPersonEmail(db.getString(i,"PERSON_EMAIL"));
				address.setPersonMobileTel1(db.getString(i,"PERSON_MOBILETEL1"));
				address.setPersonMobileTel2(db.getString(i,"PERSON_MOBILETEL2"));
				address.setPersonQicq(db.getString(i,"PERSON_QICQ"));
				address.setPersonWeb(db.getString(i,"PERSON_WEB"));
				address.setPersonHomeAddress(db.getString(i,"PERSON_HOMEADDRESS"));
				address.setPersonPostalCode(db.getString(i,"PERSON_POSTALCODE"));
				address.setPersonHomeTel(db.getString(i,"PERSON_HOMETEL"));
				address.setPersonCompany(db.getString(i,"PERSON_COMPANY"));
				address.setPersonCoAddress(db.getString(i,"PERSON_COADDRESS"));
				address.setPersonDept(db.getString(i,"PERSON_DEPT"));
				address.setPersonDuty(db.getString(i,"PERSON_DUTY"));
				address.setPersonWorkTel(db.getString(i,"PERSON_WORKTEL"));
				address.setPersonFax(db.getString(i,"PERSON_FAX"));
				address.setPersonRemark(db.getString(i,"PERSON_REMARK"));
				
				tempList.add(address);
			}
			listInfo.setDatas(tempList);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			throw new AddressManagerException();
		}
		return listInfo;
	}

	public boolean updateAddress(Address address) throws AddressManagerException {
		String sql = " update td_cms_person_addressbook set person_name = '"+
						address.getPersonName()+"', person_email = '" +
						address.getPersonEmail()+"', person_mobiletel1 = '"+
						address.getPersonMobileTel1()+"', person_mobiletel2 = '"+
						address.getPersonMobileTel2()+"', person_qicq = '"+
						address.getPersonQicq()+"', person_web = '"+
						address.getPersonWeb()+"', person_homeaddress = '"+
						address.getPersonHomeAddress()+"', person_postalcode = '" +
						address.getPersonPostalCode()+"', person_hometel = '"+
						address.getPersonHomeTel()+"', person_company = '"+
						address.getPersonCompany()+"', person_coaddress = '"+
						address.getPersonCoAddress()+"', person_dept = '"+
						address.getPersonDept()+"', person_duty = '"+
						address.getPersonDuty()+"', person_worktel = '"+
						address.getPersonWorkTel()+"', person_fax = '"+
						address.getPersonFax()+"', person_remark = '"+
						address.getPersonRemark();
		
		if(null!=address.getPersonBirthday() && !"".equals(address.getPersonBirthday())){
			sql += "', person_birthday = to_date('"+address.getPersonBirthday()+"','yyyy-mm-dd')";
		}
		
		sql += " where addressbook_id = "+address.getAddressBookId()+" and user_id = "+address.getUserId();
		DBUtil db = new DBUtil();
		
		try{
			Hashtable[] ht = db.executeUpdate(sql);
			int l = ht.length;
			return true;
		} catch (Exception e){
			throw new AddressManagerException();
		}
	}

	public Address getAddressById(String addressBookId) throws AddressManagerException {
		Address address = new Address();
		
		String sql = " select * from td_cms_person_addressbook where addressbook_id = "+addressBookId;
		DBUtil db = new DBUtil();
		
		try{
			db.executeSelect(sql);
			if(db.size()>0){
				address.setAddressBookId(db.getString(0,"ADDRESSBOOK_ID"));
				address.setUserId(db.getString(0,"USER_ID"));
				address.setPersonName(db.getString(0,"PERSON_NAME"));
				String pb = db.getString(0,"PERSON_BIRTHDAY");
				String[] dates = null;
				if(pb != null && !pb.equals("")){
					dates = pb.split(" ");
				}
				if(dates!=null && dates.length>0)
					address.setPersonBirthday(dates[0]);
				address.setPersonEmail(db.getString(0,"PERSON_EMAIL"));
				address.setPersonMobileTel1(db.getString(0,"PERSON_MOBILETEL1"));
				address.setPersonMobileTel2(db.getString(0,"PERSON_MOBILETEL2"));
				address.setPersonQicq(db.getString(0,"PERSON_QICQ"));
				address.setPersonWeb(db.getString(0,"PERSON_WEB"));
				address.setPersonHomeAddress(db.getString(0,"PERSON_HOMEADDRESS"));
				address.setPersonPostalCode(db.getString(0,"PERSON_POSTALCODE"));
				address.setPersonHomeTel(db.getString(0,"PERSON_HOMETEL"));
				address.setPersonCompany(db.getString(0,"PERSON_COMPANY"));
				address.setPersonCoAddress(db.getString(0,"PERSON_COADDRESS"));
				address.setPersonDept(db.getString(0,"PERSON_DEPT"));
				address.setPersonDuty(db.getString(0,"PERSON_DUTY"));
				address.setPersonWorkTel(db.getString(0,"PERSON_WORKTEL"));
				address.setPersonFax(db.getString(0,"PERSON_FAX"));
				address.setPersonRemark(db.getString(0,"PERSON_REMARK"));
			}else{
				return null;
			}
		} catch (Exception se){
			throw new AddressManagerException();
		}
		
		return address;
	}
	
}
