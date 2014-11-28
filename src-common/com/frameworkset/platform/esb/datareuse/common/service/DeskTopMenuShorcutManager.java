package com.frameworkset.platform.esb.datareuse.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.esb.datareuse.common.entity.BackgroundImage;
import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopBackGround;
import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopMenuBean;
import com.frameworkset.platform.esb.datareuse.common.entity.ItemMenuCustom;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class DeskTopMenuShorcutManager {

	private String dbname = null;
	private ConfigSQLExecutor executor = null;
	
	public void delteDeskmenu(String ids, String userid)throws Exception {
		if (StringUtil.isNotEmpty(ids)) {
			String[] arrayIds = ids.split(",");
			
			List<DeskTopMenuBean> list = new ArrayList<DeskTopMenuBean>();
			
			for (int i = 0; i < arrayIds.length;i++) {
				DeskTopMenuBean bean = new DeskTopMenuBean();
				bean.setUserid(userid);
				bean.setMenupath(arrayIds[i]);
				list.add(bean);
			}
			executor.deleteBeans(dbname, "deldeskmenu", list);
		}
		
	}

	public void update(List<DeskTopMenuBean> list, String userid,
			String subsystem) throws Exception {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					DeskTopMenuBean bean = list.get(i);
					bean.setUserid(userid);
					bean.setSubsystem(subsystem);

				}
				
				executor.deleteBeans(dbname, "deldeskmenu", list);
				
				executor.insertBeans(dbname, "insertDESKMENUs", list);
			}
			tm.commit();
		} catch (TransactionException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public List<DeskTopMenuBean> getUserDeskMenus(DeskTopMenuBean bean)
			throws Exception {
		String sql = "getUserDeskMenus";
		return executor.queryListBeanWithDBName(DeskTopMenuBean.class, dbname,
				sql, bean);
	}
	
	public List<DeskTopMenuBean> getUserDeskMenus(String userid,String systemid)
			throws Exception {
		String sql = "getUserDeskMenusByParam";
		return executor.queryListWithDBName(DeskTopMenuBean.class, dbname,
				sql, userid,systemid);
	}

	public Map<String, String> getUserDeskMapMenus(DeskTopMenuBean bean)
			throws Exception {
		// String sql = "SELECT menupath,subsystem,userid FROM TD_SM_DESKMENU
		// WHERE userid=#[userid] and subsystem=#[subsystem]";
		final Map<String, String> datas = new HashMap<String, String>();
		executor.queryBeanWithDBNameByNullRowHandler(new NullRowHandler() {

			@Override
			public void handleRow(Record origine) throws Exception {
				datas.put(origine.getString("menupath"), origine
						.getString("subsystem"));

			}
		}, dbname, "getUserDeskMapMenus", bean);
		return datas;
	}

	public void cleardeskmenu(String userID, String currentSystemID)
			throws Exception {

		String sql = "cleardeskmenu";
		executor.delete(sql, userID, currentSystemID);

	}

	public void updateMenuSort(List<DeskTopMenuBean> beans) throws Exception {
			String sql = "updateUserDeskMapMenusSort";
			executor.updateBeans(dbname,sql, beans);
		
	}
    
	public void deleteMenuSort(List<DeskTopMenuBean> beans) throws Exception {
		String sql = "deleteUserDeskMapMenusSort";
		executor.deleteBeans(dbname, sql, beans);
	}
	
	public void insertMenuSort(List<DeskTopMenuBean> beans) throws Exception {
			String sql = "insertUserDeskMapMenusSort";
			executor.insertBeans(dbname, sql, beans);
		}
	
	public void setExecutor(ConfigSQLExecutor executor) {

		this.executor = executor;
	}
	
	public void insertMenuWinSize(ItemMenuCustom bean)throws Exception{
		String sql = "insertDeskMenuWinSize";
		executor.insertBean(dbname, sql, bean);
		
	}
	public ItemMenuCustom queryMenuWinSize(ItemMenuCustom bean)throws Exception{
		String sql = "getDeskMenuWinSize";
		return executor.queryObjectBeanWithDBName(ItemMenuCustom.class,dbname, sql, bean);
		
	}
	public void updateMenuWinSize(ItemMenuCustom bean)throws Exception{
		String sql = "updateDeskMenuWinSize";
		executor.updateBean(dbname, sql, bean);
		
	}

	public Map<String,DeskTopMenuBean> queryMenuCustom(DeskTopMenuBean bean)throws Exception{
		String sql = "getDeskMenuCustom";
		final Map<String,DeskTopMenuBean> listMap = new HashMap<String,DeskTopMenuBean>();
		executor.queryBeanWithDBNameByNullRowHandler(new NullRowHandler(){

			public void handleRow(Record arg) throws Exception {
				DeskTopMenuBean dtmb = new DeskTopMenuBean();
				dtmb.setWidth(arg.getString("width"));
				dtmb.setHeight(arg.getString("height"));
			    listMap.put(arg.getString("menupath"),dtmb);
			}
		},dbname, sql, bean);
		return listMap;
	}
	
	public void resetDeskMenuCustom(DeskTopMenuBean bean)throws Exception{
		String sql = "resetDeskMenuCustom";
		executor.deleteBean(dbname, sql, bean);
	}
	
	public void deleteAllDeskMenuCustom(String userId,String subsystem)throws Exception{
		String sql = "deleteAllDeskMenuCustom";
		executor.deleteWithDBName(dbname, sql, userId,subsystem);
	}
	
	/**
	 * 获取该用户的自定义桌面背景
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public DeskTopBackGround getDesktopBlackGround(DeskTopBackGround bean)throws Exception{
		String sql = "getDesktopBlackGround";
		return executor.queryObjectBeanWithDBName(DeskTopBackGround.class, dbname, sql, bean);
	}
	
	/**
	 * 该用户新添一个桌面背景应用
	 * @param bean
	 * @throws Exception
	 */
	public void insertDesktopBackGround(DeskTopBackGround bean)throws Exception{
		String sql = "insertDesktopBlackGround";
		executor.insertBean(dbname, sql, bean);
	}
	
	/**
	 * 该用户更换桌面背景
	 * @param bean
	 * @throws Exception
	 */
	public void updateDesktopBackGround(DeskTopBackGround bean)throws Exception{
		String sql = "updateDesktopBlackGround";
		executor.updateBean(dbname, sql, bean);
	}
	
	/**
	 * 该用户删除自定义桌面背景
	 * @param bean
	 * @throws Exception
	 */
	public void deleteDesktopStyleCustom(DeskTopBackGround bean)throws Exception{
		String sql = "deleteDesktopStyleCustom";
		executor.deleteBean(dbname, sql, bean);
	}
	/**
	 * 该用户新加自定义桌面背景图片
	 * @param bean
	 * @throws Exception
	 */
	public void insertDesktopStyleCustom(DeskTopBackGround bean)throws Exception{
		String sql = "insertDesktopStyleCustom";
		executor.insertBean(dbname, sql, bean);
	}
	
	
	public void storeDesktopStyleCustomPictureToDisk(final File file,DeskTopBackGround bean)throws Exception{
		if(file.exists())
			return;
		executor.queryTFieldBean(   
                File.class,   
                new FieldRowHandler<File>() {   

                    @Override  
                    public File handleField(   
                            Record record)   
                            throws Exception   
                    {   

                        // 定义文件对象   
                       
                        // 如果文件已经存在则直接返回f   
                        if (file.exists())   
                            return file;   
                        // 将blob中的文件内容存储到文件中   
                        record.getFile("picture",file);   
                        return file;   
                    }   
                },   
                "storeDesktopStyleCustomPictureToDisk",   
                bean);   

	}
	/**
	 * 获取该用户所有自定义背景的列表
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public List<BackgroundImage> getDesktopStyleCustomBackgroundImage(DeskTopBackGround bean)throws Exception{
		String sql = "getListDesktopStyleCustom";
		List<BackgroundImage> datas =  executor.queryListBeanWithDBNameByRowHandler(new RowHandler<BackgroundImage>(){

			@Override
			public void handleRow(BackgroundImage arg0, Record arg1) throws Exception {
				// TODO Auto-generated method stub
				
				arg0.setText(arg1.getString("cn_name"));
				arg0.setImg(arg1.getString("filename"));
				arg0.setLeaf(true);
				arg0.setIconCls("");
			}}, BackgroundImage.class, dbname, sql, bean);
		return datas;
	}
	
	/**
	 * 分页获取用户自定义的分页背景信息
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public ListInfo getListDesktopStyleCustom(String sortKey,boolean desc,long offset,int pagesize,DeskTopBackGround bean)throws Exception{
		String sql = "getListDesktopStyleCustom";
		
		return executor.queryListInfoBeanWithDBName(DeskTopBackGround.class, dbname, sql, offset, pagesize, bean);
	}
	
	/**
	 * 更新单条背景信息
	 * @param bean
	 * @throws Exception
	 */
	public void updateDesktopStyleCustom(DeskTopBackGround bean)throws Exception{
		String sql = "updateDesktopStyleCustom";
		executor.updateBean(dbname, sql, bean);
		
	}
	/**
	 * 更新多条背景信息
	 * @param beans
	 * @throws Exception
	 */
	public void saveListBackGrounds(List beans)throws Exception{
		String sql = "updateDesktopStyleCustom";
		executor.updateBeans(dbname, sql, beans);
	}
	
	public DeskTopBackGround getDesktopStyleCustomByFileName(DeskTopBackGround bean)throws Exception{
	    String sql = "getDesktopStyleCustomByFileName";
	    return  executor.queryObjectBeanWithDBName(DeskTopBackGround.class, dbname, sql, bean);
	}
}
