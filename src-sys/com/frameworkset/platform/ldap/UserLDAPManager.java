package com.frameworkset.platform.ldap;

import java.util.*;

public class UserLDAPManager implements java.io.Serializable {
  public UserLDAPManager() {
  }
  public static void main(String[] args) {


     LdapManager ldap = new LdapManager();

       ArrayList list3 = new ArrayList();
               try{
               list3 = ldap.searchPersonByFilter("(&(personname=*)(headship=*)(|(personid=20050724000000024754)(personid=20051227183103765866)(personid=20051230154605046661)(personid=20051231140109140167)(personid=20051231140429281573)(personid=20051231144553125607)(personid=20060110111148171702)))");

               System.out.println(list3.size());
               }catch(Exception e){
            	   e.printStackTrace();
                }
  }
}
