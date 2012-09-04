package com.frameworkset.platform.sysmgrcore.purviewmanager.db;

import java.sql.SQLException;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.purviewmanager.OrgNumberGenerateService;
import com.frameworkset.common.poolman.DBUtil;

public class OrgNumberHiberarchy implements OrgNumberGenerateService {

	public boolean enableOrgNumberGenerate() {
		return ConfigManager.getInstance().getConfigBooleanValue("orgNumberHiberarchy",true);
	}

	public String generateOrgNumber(Organization parentOrg) {
		String curOrgNumber = parentOrg.getOrgnumber();
		int orgNumberLen = getOrgNumberLen();
		int curOrgNumberLength = curOrgNumber.length() + orgNumberLen;
		StringBuffer sql = new StringBuffer()
			.append("select max(to_number(orgnumber)) as maxorgnumber from td_sm_organization where parent_id = '")
			.append(parentOrg.getOrgId()).append("'");
		String maxorgnumber = "";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			maxorgnumber = db.getString(0, "maxorgnumber");
			if(maxorgnumber.length() >= curOrgNumberLength){
				maxorgnumber = maxorgnumber.substring(0, curOrgNumberLength);
				maxorgnumber = String.valueOf(Long.valueOf(maxorgnumber).longValue() + 1);
			}
			//子机构最大orgnumber为空时 默认指定值
			if("".equals(maxorgnumber)){
				String def = "";
				for(int i = 1; i < orgNumberLen; i++){
					def += "0";
				}
				maxorgnumber = curOrgNumber + def + "1";
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxorgnumber;
	}

	public int getOrgNumberLen() {
		return ConfigManager.getInstance().getConfigIntValue("hiberarchy", 2);
	}

}
