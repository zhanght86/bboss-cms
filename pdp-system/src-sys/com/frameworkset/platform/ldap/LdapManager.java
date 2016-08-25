/* 功能: ldap服务器数据操作接口
 * @author wanghh
*/
package com.frameworkset.platform.ldap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LdapManager implements LdapDao {
  //定义内部使用变量
   private String userid = "";
   private String url = "";
   private String pathbase = ""; //
   private String psw = "";

   private DirContext ctx = null;

   private LdapDatatrans ldapDatatrans;



  public LdapManager() {
     try{
        init();
     }catch(Exception e){
       e.printStackTrace();
       System.out.println("取初始化文件出错！");
     }
  }

  //取初始化参数 从配置文件里读信息
  private  void init() throws Exception {

//    this.pathbase = PropertiesReader.read("jdbc","Ldap.pathbase");  //此处定义为树的根
//    this.url = PropertiesReader.read("jdbc","Ldap.url");   //连接串
//    this.userid = PropertiesReader.read("jdbc","Ldap.userid");  //目录管理用户名
//    this.psw = PropertiesReader.read("jdbc","Ldap.psw");//密码
    this.pathbase = "dc=yourco,dc=com";
    this.url = "172.16.168.10";
    this.userid = "cn=root";
    this.psw = "root";


    Hashtable env = new Hashtable(5, 0.75f);

    env.put(Context.INITIAL_CONTEXT_FACTORY, Env.INITCTX);

    /* Specify host and port to use for directory service */
    env.put(Context.PROVIDER_URL, this.url);
    env.put(Context.SECURITY_AUTHENTICATION, Env.MY_AUTHENTICATION);//
    env.put(Context.SECURITY_PRINCIPAL,this.userid);//载入登陆帐户和登录密码
    env.put(Context.SECURITY_CREDENTIALS, this.psw);

    ctx = new InitialDirContext(env);
    ldapDatatrans = new LdapDatatrans();

  }

  public static void main(String[] args) throws NamingException {
      String pathbase = "dc=yourco,dc=com";
      String url = "172.16.168.10";
      String userid = "cn=root";
      String spsw = "root";


      Hashtable env = new Hashtable(5, 0.75f);

      env.put(Context.INITIAL_CONTEXT_FACTORY, Env.INITCTX);

      /* Specify host and port to use for directory service */
      env.put(Context.PROVIDER_URL, url);
      env.put(Context.SECURITY_AUTHENTICATION, Env.MY_AUTHENTICATION);//
      env.put(Context.SECURITY_PRINCIPAL,userid);//载入登陆帐户和登录密码
      env.put(Context.SECURITY_CREDENTIALS, spsw);

      DirContext ctx = new InitialDirContext(env);
//      ldapDatatrans = new LdapDatatrans();

          //设置查询范围的权限 此处为入口以下的子树
                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

                //此处查许所有对象类别为person的条目 即入口以下所有用户
                NamingEnumeration results
                    = ctx.search("cn=users,"+pathbase,"uid=wpsadmin", constraints);

  }


//插入一个用户帐户 如果字段是日期型的 必须转换成String型 其格式约定为: yyyy-mm-dd hh24:mi:ss(日期和时间有空格)
  public void insertUser(AuthUser authUser) throws Exception {

    //添加一个用户条目时 objectclass 和 uid两个属性时必须的

     BasicAttributes attrs = new BasicAttributes(); //条目的属性列表
     attrs = ldapDatatrans.BeanToAttrs(authUser);

     BasicAttribute objclassSet = new BasicAttribute("objectclass"); //具有多个value的属性类别
     objclassSet.add("top");
     objclassSet.add("authuser");  //自定义objectclass
     attrs.put(objclassSet);

     //"id=" + account.getid() +"," + this.pathbase构成了树型目录里面唯一标志(usrs时自定义的)
     // dc=sinosoft.com为目录根节点 ou=user为根节点下的第一层组织节点 uid为用户帐户标识
     ctx.createSubcontext("uid=" + authUser.getUid() +",ou=authuser," + this.pathbase, attrs);
}


   //删除一个用户帐户
   public void deleteUser(String id) throws Exception {

    //"id=" + id +",ou=users," +this.pathbase构成了树型目录里面唯一标志(usrs时自定义的)
      ctx.destroySubcontext("uid=" + id +",ou=authuser," +this.pathbase);
      System.out.println("用户号为"+ id  + "的用户删除成功!");
  }


   //以用户id号查询一个用户信息 返回一个ARRAYLIST 其容纳对象为 UserAccount 类型
   public ArrayList searchUser(String id) throws Exception {

     ArrayList list = new ArrayList();



       //设置查询范围的权限 此处为入口以下的子树
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

            //此处查许所有对象类别为person的条目 即入口以下所有用户
            NamingEnumeration results
                = ctx.search("ou=authuser,"+this.pathbase,"uid="+id, constraints);

          /*循环输出每个条目的每个属性名和值*/

            while (results != null && results.hasMore()) {
                SearchResult si = (SearchResult)results.next();

                AuthUser authUser = new AuthUser();
                Attributes attrs = si.getAttributes();
                ldapDatatrans.AttrsToBean(attrs,authUser);

                list.add(authUser);
            }
   		return list;
    }


    /*功能: 以过滤条件 检索多条记录 结果存为UserAccount类型的列表
     * 过滤条件如:  (id=abc)
     *             与 (&(id=abc)(username<=abc)(psw>=abc))
     *             或 (|(id=abc)(username=abc)(psw=abc))
     *             非 (!(id<abc)
     *       最后的条件表达式在最外层加一个小括号
     */
    public ArrayList searchUserByFilter(String filter) throws Exception {

      ArrayList list = new ArrayList();



        //设置查询范围的权限 此处为入口以下的子树
             SearchControls constraints = new SearchControls();
             constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

             //此处查许所有对象类别为users的条目 即入口以下所有用户
             NamingEnumeration results
                 = ctx.search("ou=authuser,"+this.pathbase,filter, constraints);

           /*循环输出每个条目的每个属性名和值*/

             while (results != null && results.hasMore()) {
                 SearchResult si = (SearchResult)results.next();

                 AuthUser authUser = new AuthUser();
                 Attributes attrs = si.getAttributes();
                 ldapDatatrans.AttrsToBean(attrs,authUser);

                 list.add(authUser);
             }
             return list;
     }



 //查询所有的用户帐户 结果存为ARRAYLIST数组 列表容纳对象为 UserAccount 类型
 public ArrayList searchAllUser() throws Exception {

	 ArrayList list = new ArrayList();
     	//设置查询范围的权限 此处为入口以下的子树
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //此处查许所有对象类别为users的条目 即入口以下所有用户
        NamingEnumeration results
            = ctx.search("ou=authuser,"+this.pathbase,"objectclass=authuser", constraints);

	       while (results != null && results.hasMore()) {
	            SearchResult si = (SearchResult)results.next();

	            AuthUser authUser = new AuthUser();
	            Attributes attrs = si.getAttributes();
	            ldapDatatrans.AttrsToBean(attrs,authUser);

	            list.add(authUser);
	        }
	       return list;
      }



     /**
       * 修改一个条目的属性值
       * @param id 要修改的用户帐户的id
       * @param list 其所容纳对象类型为AttrBean	 属性名和属性值对 用户要先行构造
       *
       */


   public void updateUser(String id,ArrayList list) throws Exception {

	   	String attrid =""; //属性id
	   	String attrvalue = "";//新的属性值
	    ModificationItem modificationItem[] = new ModificationItem[list.size()];
	    for(int i=0; i<list.size(); i++){
	       attrid = ((AttrBean)list.get(i)).getAttrName().toLowerCase();
	       attrvalue = ((AttrBean)list.get(i)).getNewValue();

	       modificationItem[i] =
	          new ModificationItem(
	          DirContext.REPLACE_ATTRIBUTE,
	          new BasicAttribute(attrid, attrvalue)); //所修改的属性
	      //"id=" + id + "," + this.pathbase是这个帐户条目的entrydn(入口)
	    }
	      try{
	      ctx.modifyAttributes("uid=" + id + ",ou=authuser," + this.pathbase, modificationItem); //执行修改操作
	      }catch(NamingException e){
	    	  System.out.println("没有这条记录 或者属性名有误");
	    	  return;
	      }

   }

   //传入一个帐号对象 执行修改动作
   public void updateUser(AuthUser authUser) throws Exception {


       ModificationItem[] modificationItem = ldapDatatrans.createModificationItems(authUser);

	     try{
	      ctx.modifyAttributes("uid=" + authUser.getUid() + ",ou=authuser," + this.pathbase, modificationItem);
	     }catch(NamingException e){
	   	  System.out.println("修改失败 没有这条记录 或者属性名有误"+authUser.getUsername());
	   	  return;
	     }
	     System.out.println("==LDAP== 修改人员: "+authUser.getUsername()+ " 成功!");
}

    /**
       * 功能: 插入一个人员信息  同时插入一个帐号信息
       * 如果字段是日期型的 必须转换成String型 其格式约定为: yyyy-mm-dd hh24:mi:ss(日期和时间有空格)
       *
       * @param authPerson 类型为AuthPerson的对象 待插入数据
       * @param authUser   类型为AuthUser的对象 待插入数据
       */

  public void insertPerson(AuthPerson authPerson,AuthUser authUser) throws Exception {

    //添加一个人员条目时 objectclass 和 persionid两个属性时必须的

     BasicAttributes attrs = new BasicAttributes(); //条目的属性列表
     attrs = ldapDatatrans.BeanToAttrs(authPerson);
     BasicAttribute objclassSet = new BasicAttribute("objectclass"); //具有多个value的属性类别
     objclassSet.add("top");
     objclassSet.add("authperson");  //自定义objectclass
     attrs.put(objclassSet);

       ctx.createSubcontext("personid=" + authPerson.getPersonid() +",ou=authperson," + this.pathbase, attrs);
       System.out.println(authPerson.getPersonname()  + "插入成功!");

       //插入人员的时候必须同时插入用户帐号信息
       insertUser(authUser);


}

  /**
   * 功能: 插入一个人员信息 不管帐号信息 在程序调用时由程序员自行进行关联插入
   * 如果字段是日期型的 必须转换成String型 其格式约定为: yyyy-mm-dd hh24:mi:ss(日期和时间有空格)
   *
   * @param person 类型为PersonBean的对象 待插入数据
   */

public void insertPerson(AuthPerson authPerson) throws Exception {

//	添加一个人员条目时 objectclass 和 persionid两个属性时必须的
	BasicAttributes attrs = new BasicAttributes(); //条目的属性列表
    attrs = ldapDatatrans.BeanToAttrs(authPerson);
    BasicAttribute objclassSet = new BasicAttribute("objectclass"); //具有多个value的属性类别
    objclassSet.add("top");
    objclassSet.add("authperson");  //自定义objectclass
    attrs.put(objclassSet);

      ctx.createSubcontext("personid=" + authPerson.getPersonid() +",ou=authperson," + this.pathbase, attrs);
      System.out.println(authPerson.getPersonname()  + "插入成功!");

}


     //删除一个人员信息
   public void deletePerson(String id) throws Exception {
	   //XmlRoleManager xmlRole = new XmlRoleManager();
	   //通过人员id查出帐号accountid
	   String userid = "";
	   ArrayList list = this.searchUserByFilter(("personid="+id));

	   if(list.size()>0)
		   userid = ((AuthUser)(list.get(0))).getUid();
	   if(userid==null) userid="";



	   //"id=" + id + this.pathbase构成了树型目录里面唯一标志(usrs时自定义的)
	  ctx.destroySubcontext("personid=" + id +",ou=authperson," +this.pathbase);

      //删除此人员对应的帐号信息
	  if(!(userid.equals(""))){
		  deleteUser(userid);
	  }

       //删除人员对应的角色
	//  	xmlRole.deleteRole(id);
   }

   //根据机构id(orgID)删除这个机构下的所有人员
   public void deletePersonByOrgID(String orgID) throws Exception {
	  // XmlRoleManager xmlRole = new XmlRoleManager();
	   //删除机构对应的角色
	   //xmlRole.deleteRole(orgID);
	   //查出所有的personid
	   ArrayList personList = new ArrayList();
	   personList = searchPersonByFilter("(organid="+orgID+")");
       for(int i=0; i<personList.size();i++){
    	   String personID = ((AuthPerson)personList.get(i)).getPersonid();
    	   if (personID!=null&&(!personID.equals("")))
    	       deletePerson(personID);

       }
   }


   //以用户id号查询一个用户信息 返回一个ARRAYLIST 其所含对象类型为PersonBean
   public ArrayList searchPerson(String id) throws Exception {

	   ArrayList list = new ArrayList();
     	//设置查询范围的权限 此处为入口以下的子树
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

            //此处查许所有对象类别为person的条目 即入口以下所有用户
            NamingEnumeration results
                = ctx.search("ou=authperson,"+this.pathbase,"personid="+id, constraints);

          /*循环输出每个条目的每个属性名和值*/

            while (results != null && results.hasMore()) {
                SearchResult si = (SearchResult)results.next();

                AuthPerson authPerson = new AuthPerson();
                Attributes attrs = si.getAttributes();
                ldapDatatrans.AttrsToBean(attrs,authPerson);
                list.add(authPerson);

            }
            return list;
    }

    /*功能: 以过滤条件 检索多条记录 结果存为PersonBean类型的列表
     * 过滤条件如:  (uid=abc)
     *             与 (&(id=abc)(username<=abc)(psw>=abc))
     *             或 (|(id=abc)(username=abc)(psw=abc))
     *             非 (!(id<abc)) 非操作符后面只能跟一个表达式
     *             与 (id=*abc*) 表示模糊查询
     *       最后的条件表达式在最外层加一个小括号
     */
    public ArrayList searchPersonByFilter(String filter) throws Exception {

      ArrayList list = new ArrayList();



        //设置查询范围的权限 此处为入口以下的子树
             SearchControls constraints = new SearchControls();
             constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

             //此处查许所有对象类别为users的条目 即入口以下所有用户
             NamingEnumeration results
                 = ctx.search("ou=authperson,"+this.pathbase,filter, constraints);

             /*循环输出每个条目的每个属性名和值*/

             while (results != null && results.hasMore()) {
                 SearchResult si = (SearchResult)results.next();

                 AuthPerson authPerson = new AuthPerson();
                 Attributes attrs = si.getAttributes();
                 ldapDatatrans.AttrsToBean(attrs,authPerson);
                 list.add(authPerson);
             }
             return list;

     }



 //查询所有的人员信息 结果存为ARRAYLIST数组
 public ArrayList searchAllPerson() throws Exception {

     ArrayList list = new ArrayList();



   //设置查询范围的权限 此处为入口以下的子树
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //此处查许所有对象类别为persons的条目 即入口以下所有人员信息
        NamingEnumeration results
            = ctx.search(this.pathbase,"objectclass=authperson", constraints);

      /*循环输出每个条目的每个属性名和值*/

        while (results != null && results.hasMore()) {
            SearchResult si = (SearchResult)results.next();

            AuthPerson authPerson = new AuthPerson();
            Attributes attrs = si.getAttributes();
            ldapDatatrans.AttrsToBean(attrs,authPerson);
            list.add(authPerson);
        }
        return list;

 	}

 /**
  * 修改一个条目的属性值
  * @param  经过修改后的人员对象
  *
  */


public void updatePerson(AuthPerson authPerson) throws Exception {


       ModificationItem[] modificationItem = ldapDatatrans.createModificationItems(authPerson);

	     try{
	      ctx.modifyAttributes("personid=" + authPerson.getPersonid() + ",ou=authperson," + this.pathbase, modificationItem);
	     }catch(NamingException e){
	   	  System.out.println("修改失败 没有这条记录 或者属性名有误"+authPerson.getPersonid());
	   	  return;
	     }
	     System.out.println("==LDAP== 修改人员: "+authPerson.getPersonname()+ " 成功!");
}

     /**
       * 修改一个条目的属性值
       * @param id 要修改的人员id
       * @param list 其所容纳对象类型为AttrBean	 属性名和属性值对 用户要先行构造
       *
       */


   public void updatePerson(String id,ArrayList list) throws Exception {
            String attrid =""; //属性id
            String attrvalue = "";//新的属性值

            ModificationItem modificationItem[] = new ModificationItem[list.size()];
    	    for(int i=0; i<list.size(); i++){
    	       attrid = ((AttrBean)list.get(i)).getAttrName().toLowerCase();
    	       attrvalue = ((AttrBean)list.get(i)).getNewValue();

    	       modificationItem[i] =
    	          new ModificationItem(
    	          DirContext.REPLACE_ATTRIBUTE,
    	          new BasicAttribute(attrid, attrvalue)); //所修改的属性
    	      //"id=" + id + "," + this.pathbase是这个帐户条目的entrydn(入口)
    	    }

		     try{
		      ctx.modifyAttributes("personid=" + id + ",ou=authperson," + this.pathbase, modificationItem);
		     }catch(NamingException e){
		   	  System.out.println("修改失败 没有这条记录 或者属性名有误"+id);
		   	  return;
		     }
	}


   /**
     * 返回所有的角色 结果存为ArrayList.
     * @param
     * @return  ArrayList 其容纳对象为AuthRole
     */

 public ArrayList searchAllAuthGroup() throws Exception {

     ArrayList list = new ArrayList();


     //设置查询范围的权限 此处为入口以下的子树
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //此处查许所有对象类别为roles的条目 即入口以下所有用户
        NamingEnumeration results   //roletype=0表示是本系统的角色而不是onceportal的角色
            = ctx.search("ou=authrole,"+this.pathbase,"(objectclass=authrole)", constraints);

        /*循环输出每个条目的每个属性名和值*/

        while (results != null && results.hasMore()) {
            SearchResult si = (SearchResult)results.next();

            AuthRole authRole = new AuthRole();
            Attributes attrs = si.getAttributes();
            ldapDatatrans.AttrsToBean(attrs,authRole);
            list.add(authRole);
        }
        return list;
      }


      /**
       * 向ldap服务器插入一个角色
       * @param authRole 权限组对象
       * @return
       */
     public void insertAuthGroup(AuthRole authRole) throws Exception {

        //添加一个用户条目时 objectclass 和 id两个属性时必须的
    	 BasicAttributes attrs = new BasicAttributes(); //条目的属性列表
    	    attrs = ldapDatatrans.BeanToAttrs(authRole);
    	    BasicAttribute objclassSet = new BasicAttribute("objectclass"); //具有多个value的属性类别
    	    objclassSet.add("top");
    	    objclassSet.add("authrole");  //自定义objectclass
    	    attrs.put(objclassSet);


         ctx.createSubcontext("roleid=" +authRole.getRoleid()+",ou=authrole," + this.pathbase, attrs);
         //插入xml文件中的角色
         //XmlRoleManager xmlRole = new XmlRoleManager();
         //xmlRole.addRole(authRole.getRoleid(),authRole.getRolename());

         System.out.println("角色" + authRole.getRolename() + "插入成功!");
     }


      /**
       * 向ldap服务器更新一个角色 id是不能更新的
       * @param authGroup 权限组对象
       * @param list 其所容纳对象类型为AttrBean	 属性名和属性值对 用户要先行构造
       * @return
       */
      public void updateAuthGroup(String id,ArrayList list) throws Exception {

    		String attrid =""; //属性id
    	   	String attrvalue = "";//新的属性值
    	    ModificationItem modificationItem[] = new ModificationItem[list.size()];
    	    for(int i=0; i<list.size(); i++){
    	       attrid = ((AttrBean)list.get(i)).getAttrName().toLowerCase();
    	       attrvalue = ((AttrBean)list.get(i)).getNewValue();

    	       modificationItem[i] =
    	          new ModificationItem(
    	          DirContext.REPLACE_ATTRIBUTE,
    	          new BasicAttribute(attrid, attrvalue)); //所修改的属性
    	      //"id=" + id + "," + this.pathbase是这个帐户条目的entrydn(入口)
    	    }
        	  try{
        		  ctx.modifyAttributes("roleid=" + id + ",ou=authrole," + this.pathbase, modificationItem); //执行修改操作
        	  }catch(NamingException e){
        		  System.out.println("没有这条记录 或者属性名有误");
        		  return;
        	  }
      }

      //传入一个角色对象 修改此角色
      public void updateAuthGroup(AuthRole authRole) throws Exception {


          ModificationItem[] modificationItem = ldapDatatrans.createModificationItems(authRole);

   	     try{
   	      ctx.modifyAttributes("roleid=" + authRole.getRoleid() + ",ou=authrole," + this.pathbase, modificationItem);
   	     }catch(NamingException e){
   	   	  System.out.println("修改失败 没有这条记录 或者属性名有误"+authRole.getRoleid());
   	   	  return;
   	     }
   	     System.out.println("==LDAP== 修改角色: "+authRole.getRolename()+ " 成功!");
   }

      /**
       * 返回一个的角色 ARRAYLIST
       * @param  id 角色id(唯一标识)
       * @return  返回一个ARRAYLIST 其所含对象类型为AuthRole
       */
     public ArrayList searchAuthGroup(String id) throws Exception {

    	 ArrayList list = new ArrayList();
    	 //设置查询范围的权限 此处为入口以下的子树
              SearchControls constraints = new SearchControls();
              constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

              //此处查许所有对象类别为person的条目 即入口以下所有用户
              NamingEnumeration results
                  = ctx.search("ou=authrole,"+this.pathbase,"roleid="+id, constraints);

            /*循环输出每个条目的每个属性名和值*/

              while (results != null && results.hasMore()) {
                  SearchResult si = (SearchResult)results.next();

                  AuthRole authRole = new AuthRole();
                  Attributes attrs = si.getAttributes();
                  ldapDatatrans.AttrsToBean(attrs,authRole);

                  list.add(authRole);
              }
              return list;
     }

     /**
      * 功能: 更具一个角色id列 返回本系列角色及其下面的子角色列表.
      * 返回一个的角色 ARRAYLIST
      * @param  strAuthGroupID 角色id列表 00000000000000000001<>,20050926000000031060<>,20051009000000031158<>,
      * @return  返回一个ARRAYLIST 其所含对象类型为AuthRole
      */
    public ArrayList searchChildAuthGroup(String strAuthGroupID) throws Exception {

   	 ArrayList list = new ArrayList();



        //设置查询范围的权限 此处为入口以下的子树
             SearchControls constraints = new SearchControls();
             constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

             //首先取得有那些角色id信息
             String[] parentIDList = strAuthGroupID.split("<>,");  //取得父角色id数组
             //对构造的数组进行排序
             ArrayList sortedList = new ArrayList();
             for(int i=0; i<parentIDList.length; i++){
            	 sortedList.add(parentIDList[i]);
             }
             Collections.sort(sortedList);

             String roleIds = "" ; //最终所有的角色id
             //循环每个id
             for(int i= 0; i<sortedList.size();i++){
            	 String parentID = sortedList.get(i).toString();
            	 LdapBean ldapBean = new LdapBean();
            	 ldapBean.childIDList = new ArrayList();
            	 ldapBean.searchChildID(parentID);
            	 for(int j=0; j<ldapBean.childIDList.size(); j++){
            		 String tempID = (ldapBean.childIDList.get(j)).toString();
            		 if(roleIds.indexOf(tempID)<0){
            			 roleIds += tempID+"<>,";
            		 }
            	 }

             }
             String[] strAllID = roleIds.split("<>,");
             for(int j=0; j<strAllID.length;j++){
            	 String strID = strAllID[j];
            	 ArrayList tempList = this.searchAuthGroup(strID);
            	 if(tempList.size()>0){
            		 list.add((AuthRole)tempList.get(0));
            	 }
             }
           return   list;
    }




  /**
    * 删除一个的角色
    * @param  id: 角色id
    * @return  Role对象
    */
    public void deleteAuthGroup(String id) throws Exception {


       String parentId = "";
       //取得要删除节点的parentid
       ArrayList lsTemp = new ArrayList();
       lsTemp = this.searchAuthGroup(id);
       if((lsTemp!=null)&&(lsTemp.size()>0)){
    	   parentId = ((AuthRole)lsTemp.get(0)).getRolesireid();
       }
       else
       {
    	   System.out.println("没有这个角色信息:"+id);
    	   return;
       }

       //修改所有以此为父节点的记录为根节点的子角色
       ArrayList lsAuth = new ArrayList();
       lsAuth = this.searchAuthGroupByFilter("(rolesireid="+id+")");
       for(int i=0; i<lsAuth.size();i++){
    	  AuthRole authRole = (AuthRole)(lsAuth.get(i));
    	  //设置修改选项(name)
    	  ArrayList lsUpdate = new ArrayList();
    	  lsUpdate.add(new AttrBean("rolesireid",parentId));
    	  this.updateAuthGroup(authRole.getRoleid(),lsUpdate);

       }

       //删除这条记录
       ctx.destroySubcontext("roleid=" + id +",ou=authrole," +this.pathbase);

       //更新帐号信息里面的角色串
       ArrayList list = new ArrayList();
       list = this.searchAllUser();

       for(int i=0;  i<list.size();  i++){
         //多个角色id用 "<>," 分隔 如: 90000000000000000000<>,20050808000000025348<>,
         String uid; //帐号id
         String roleString = "";
         uid = ((AuthUser)list.get(i)).getUid();
         roleString = ((AuthUser)list.get(i)).getRoleid();
         //判断是否有这个角色
         if(roleString.indexOf(id+"<>,",0) >= 0){
           roleString = roleString.replaceAll(id+"<>,","");
           ArrayList atts = new ArrayList();
           atts.add(new AttrBean("roleid",roleString));
           this.updateUser(uid,atts);
         }
       }
       //删除xml文件里面的角色信息
       //XmlRoleManager roleManager = new XmlRoleManager();
       //roleManager.deleteRole(id);

       System.out.println("角色号为"+ id  + "的角色删除成功!");

     }
    /*功能: 以过滤条件 检索多条记录 结果存为AuthRole类型的列表
     * 过滤条件如:  (id=abc)
     *             与 (&(id=abc)(username<=abc)(psw>=abc))
     *             或 (|(id=abc)(username=abc)(psw=abc))
     *             非 (!(id<abc)
     *       最后的条件表达式在最外层加一个小括号
     */
    	public ArrayList searchAuthGroupByFilter(String filter) throws Exception {

    		ArrayList list = new ArrayList();
      		//设置查询范围的权限 此处为入口以下的子树
             SearchControls constraints = new SearchControls();
             constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
             //此处查许所有对象类别为roles的条目 即入口以下所有用户 (objectclass=authrole)
             NamingEnumeration results
                 = ctx.search("ou=authrole,"+this.pathbase,filter, constraints);
             /*循环输出每个条目的每个属性名和值*/

             while (results != null && results.hasMore()) {
                 SearchResult si = (SearchResult)results.next();

                 AuthRole authRole = new AuthRole();
                 Attributes attrs = si.getAttributes();
                 ldapDatatrans.AttrsToBean(attrs,authRole);
                 list.add(authRole);
             }
             return list;
    	}
	}
