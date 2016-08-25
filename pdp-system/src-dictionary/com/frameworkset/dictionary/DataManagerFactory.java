package com.frameworkset.dictionary;

import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.platform.config.ConfigManager;


public class DataManagerFactory{
	static
	{
		BaseApplicationContext.addShutdownHook(new Runnable(){
	
			@Override
			public void run() {
				
				destroy();
			}
			
		});
	}
	static void destroy()
	{
		if(dataManager != null)
		{
			dataManager.destory();
			dataManager = null;
		}
	}
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
