package com.frameworkset.platform.sysmgrcore.authorization;

import java.io.Serializable;

import org.frameworkset.event.Notifiable;
import org.frameworkset.event.NotifiableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class ACLNotifiableFactory extends NotifiableFactory implements Serializable{
    private static Logger log = LoggerFactory.getLogger(ACLNotifiableFactory.class);
    private static NotifiableFactory instance;

    public static void main(String[] args) {
        ACLNotifiableFactory aclnotifiablefactory = new ACLNotifiableFactory();
    }

    public Notifiable getNotifiable(int type) {
//        try
//        {
//            switch (type) {
//            case NotifiableType.USER_NOTIFIABLE:
//                return SecurityDatabase.getUserManager();
//            case NotifiableType.GROUP_NOTIFIABLE:
//                return SecurityDatabase.getGroupManager();
//            case NotifiableType.ROLE_NOTIFIABLE:
//                return SecurityDatabase.getRoleManager();
//            case NotifiableType.RESOURCE_NOTIFIABLE:
//                return SecurityDatabase.getResourceManager();
////            case NotifiableType.PERMISSION_NOTIFIABLE:
////                return SecurityDatabase.getPermissionManager();
//            case NotifiableType.ORGUNIT_NOTIFIABLE:
//                return SecurityDatabase.getOrgManager();
//            default:
//                log.debug(this.getClass().getName() +  ".getNotifiable notifiable type error:no defined type '" +
//                                 type + "'");
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
          return NotifiableFactory.getNotifiable();
      }

      public ACLNotifiableFactory()
      {

      }

//      public static NotifiableFactory getInstance()
//      {
//          if(instance == null)
//          {
//              instance = new ACLNotifiableFactory();
//          }
//          return instance;
//      }


}
