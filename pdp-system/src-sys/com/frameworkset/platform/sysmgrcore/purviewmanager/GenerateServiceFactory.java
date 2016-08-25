package com.frameworkset.platform.sysmgrcore.purviewmanager;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.AutoCreateUserName;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.OrgNumberHiberarchy;

public class GenerateServiceFactory {
	
	public static GenerateService getGenerateService(){
		GenerateService generateService;
		String bussinessCheck_ = ConfigManager.getInstance()
			.getConfigValue("com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateService",
				"com.frameworkset.platform.sysmgrcore.purviewmanager.db.AutoCreateUserName");
		try {
			generateService = (GenerateService)Class.forName(bussinessCheck_).newInstance();
		} catch (InstantiationException e) {
			generateService = new AutoCreateUserName();
		} catch (IllegalAccessException e) {
			generateService = new AutoCreateUserName();
		} catch (ClassNotFoundException e) {
			generateService = new AutoCreateUserName();
		}
		return generateService;
	}
	
	public static OrgNumberGenerateService getOrgNumberGenerateService(){
		OrgNumberGenerateService orgNumberGenerateService;
		String bussinessCheck_ = ConfigManager.getInstance()
			.getConfigValue("com.frameworkset.platform.sysmgrcore.purviewmanager.OrgNumberGenerateService",
				"com.frameworkset.platform.sysmgrcore.purviewmanager.db.OrgNumberHiberarchy");
		try {
			orgNumberGenerateService = (OrgNumberGenerateService)Class.forName(bussinessCheck_).newInstance();
		} catch (InstantiationException e) {
			orgNumberGenerateService = new OrgNumberHiberarchy();
		} catch (IllegalAccessException e) {
			orgNumberGenerateService = new OrgNumberHiberarchy();
		} catch (ClassNotFoundException e) {
			orgNumberGenerateService = new OrgNumberHiberarchy();
		}
		return orgNumberGenerateService;
	}

}
