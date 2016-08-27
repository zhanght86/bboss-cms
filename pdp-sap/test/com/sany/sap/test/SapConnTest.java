package com.sany.sap.test;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

import com.sany.sap.connection.SapResult;


public class SapConnTest {
	@Test
	public void testCallBAPI_ACC_DOCUMENT_POST()
	{
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("com/sany/sap/test/saptest.xml");
		SFASAPUtil util = context.getTBeanObject("sapoperationutil", SFASAPUtil.class);
		// util.generalLedgerToSAP();
//		//成本中心
//		List<Map<String, Object>> ksh = new ArrayList<Map<String, Object>>();
//		Map map = new HashMap();
//		map.put("KSHIER", "CCTE300001");
//		Map map0 = new HashMap();
//		map0.put("KSHIER", "CD01030000");
//		ksh.add(map);
//		ksh.add(map0);
//		
//		//成本要素
//		List<Map<String, Object>> kstar = new ArrayList<Map<String, Object>>();
//		Map map1 = new HashMap();
//		map1.put("KSTAR", "4101010000");
//		Map map2 = new HashMap();
//		map2.put("KSTAR", "5401020100");
//		Map map3 = new HashMap();
//		map3.put("KSTAR", "5501040100");
//		
//		kstar.add(map1);
//		kstar.add(map2);
//		kstar.add(map3);
//		SapResult r = util.createUser();
//		System.out.println();
		SapResult r = util.createUserUnTX("yinbp2","10006675");
		System.out.println();
//		List sapres = util.getSapFytj(ksh,kstar,"2010-01-01","2012-08-07");
//		util.getSapOracleDateTime();
		

	}

}
