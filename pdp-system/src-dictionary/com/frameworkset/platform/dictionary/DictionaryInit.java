/**
 * 
 */
package com.frameworkset.platform.dictionary;

import java.util.List;

import javax.servlet.ServletContext;

import com.frameworkset.platform.config.DestroyException;
import com.frameworkset.platform.config.InitException;
import com.frameworkset.platform.config.SystemInit;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.ProfessionDataManagerException;

/**
 * 启动的时候, 把所有的字典类型对象加载到内存
 * <p>Title: DictionaryInit.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-21 14:15:50
 * @author ge.tao
 * @version 1.0
 */
public class DictionaryInit implements SystemInit{

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.config.SystemInit#setContext(javax.servlet.ServletContext)
	 */
	public void setContext(ServletContext context) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.config.SystemInit#init()
	 * 把所有字典类型加载到内存 长时间等待...
	 */
	public void init() throws InitException {
		DictManager dict = new DictManagerImpl();
		try {
			//做数据整理 把t.is_tree is null更新成 is_tree=0;
			String update_sql = "update td_sm_dicttype t set t.is_tree=0   where t.is_tree is null";
			DBUtil db = new DBUtil();
			db.executeUpdate(update_sql);
			//end 
			
			System.out.println("初始化字典数据开始.");
			DataManager dataManager = DataManagerFactory.getDataManager();
			List allDicttype = dict.getChildDicttypeList("");
			for(int i=0;i<allDicttype.size();i++){
				Data dicttype = (Data)allDicttype.get(i);
				if(!dicttype.isCachable())
					continue;
				try {
//					System.out.println("开始加载字典类型"+dicttype.getName());
//					if(dicttype.getDataId().equals("363"))
//						System.out.println("111");
					dataManager.getDataByID(dicttype.getDataId());
//					System.out.println("加载字典类型"+dicttype.getName()+"成功");
				} catch (ProfessionDataManagerException e) {
					// TODO Auto-generated catch block
					System.out.println("加载字典类型"+dicttype.getName()+"出错");
					e.printStackTrace();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			System.out.println("初始化字典数据成功结束.");
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("初始化字典数据失败结束.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("初始化字典数据失败结束.");
		}
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.config.SystemInit#destroy()
	 */
	public void destroy() throws DestroyException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args)
	{
		DictionaryInit init = new DictionaryInit();
		try {
			init.init();
		} catch (InitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
