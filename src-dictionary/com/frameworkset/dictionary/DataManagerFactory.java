package com.frameworkset.dictionary;

import java.io.Serializable;

import com.frameworkset.platform.config.ConfigManager;


public class DataManagerFactory implements Serializable{
	private static DataManager dataManager;
	public static DataManager getDataManager()
	{
		if(dataManager != null)
		{
			return dataManager;
		}
		
		
		String dictionary = ConfigManager.getInstance().getDictionary();
		
		
		try {
			if(dictionary == null || dictionary.equals(""))
			{
				dictionary = "com.frameworkset.platform.dictionary.DictDataProvide";
			}
			dataManager = (DataManager)Class.forName(dictionary).newInstance();
			dataManager.init();
//			CommonDataManager dataManager1 = new CommonDataManager(dataManager);
//			dataManager1.init();
			return dataManager;
		} catch (InstantiationException e1) {
			
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			
			e1.printStackTrace();
		}
		catch (ProfessionDataManagerException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return null;
			
			
		
	}
	

}
