package org.frameworkset.token;

import org.frameworkset.security.ecc.ECCHelper;
import org.frameworkset.web.token.DBTokenStore;
import org.frameworkset.web.token.MemToken;
import org.frameworkset.web.token.NullValidateApplication;
import org.frameworkset.web.token.Ticket;
import org.frameworkset.web.token.TokenStore;
import org.frameworkset.web.token.ws.CheckTokenService;
import org.frameworkset.web.token.ws.TicketGetResponse;
import org.frameworkset.web.token.ws.TokenCheckResponse;
import org.frameworkset.web.token.ws.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;

public class DBTokenTest {
	private static DBTokenStore mongodbTokenStore;
	private String account = "yinbp";
	private String worknumber = "10006673";
	private String appid = "test";
	private String secret = "c96a4686-c5dc-4556-9554-eb61846b5180";
//	 String server = "http://10.0.15.223/SanyToken";
	String server = "http://pdp.bbossgroups.com";
	
	
	public void init() throws Exception
	{
		mongodbTokenStore = new DBTokenStore();
		mongodbTokenStore.setECCCoder(ECCHelper.getECCCoder());
		mongodbTokenStore.setValidateApplication(new NullValidateApplication());
		mongodbTokenStore.setTempTokendualtime(TokenStore.DEFAULT_TEMPTOKENLIVETIME);
		mongodbTokenStore.setTicketdualtime(TokenStore.DEFAULT_TICKETTOKENLIVETIME);
		mongodbTokenStore.setDualtokenlivetime(TokenStore.DEFAULT_DUALTOKENLIVETIME);
		Ticket ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		MemToken token = null;//mongodbTokenStore.genDualToken(appid,ticket.getToken(),secret,TokenStore.DEFAULT_DUALTOKENLIVETIME);
		//Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
		token = mongodbTokenStore.genTempToken();
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(null,null,token.getToken()).getResult());
		token = mongodbTokenStore.genAuthTempToken(appid,ticket.getToken(),secret);
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
	}
	@Test
	public void genTemptokenAndValidate() throws Exception
	{		
		
		MemToken token = mongodbTokenStore.genTempToken();
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(null,null,token.getToken()).getResult());
	}
	
//	@Test
//	public void gendualtokenAndValidate() throws Exception
//	{
//		Ticket ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
//		//long start = System.currentTimeMillis();
//		MemToken token = mongodbTokenStore.genDualToken(appid,ticket.getToken(),secret,TokenStore.DEFAULT_DUALTOKENLIVETIME);
//		//long end = System.currentTimeMillis();
//		//System.out.println(end - start);
//		//start = System.currentTimeMillis();
//		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
//		//end = System.currentTimeMillis();
//		//System.out.println(end - start);
//	}
	
	
	@Test
	public void gentempauthortokenAndValidate() throws Exception
	{
		Ticket ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		MemToken token = mongodbTokenStore.genAuthTempToken(appid,ticket.getToken(),secret);
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
	}
	@Test
	public void livecheck() throws Exception
	{
		mongodbTokenStore.livecheck();
	}
	@Test
	public void testticket() throws Exception
	{
		Ticket ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		System.out.println(":\n"+ticket.getToken());
	}
	
	public static void main(String[] args) throws Exception
	{
		final DBTokenTest s = new DBTokenTest();
		s.init();
		for(int i = 0; i < 10; i ++)
		{
			Thread t = new Thread(){
				public void run()
				{
					while(true)
					{
						try {
							long start = System.currentTimeMillis();
//							mongodbTokenStore.requestStart();
//							s.gendualtokenAndValidate();
							 s.gentempauthortokenAndValidate();
							s.genTemptokenAndValidate();
							
							long end = System.currentTimeMillis();
							System.out.println("耗时:"+(end -start));
							sleep(1000);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally
						{
//							mongodbTokenStore.requestDone();
						}
						
					}
				}
			};
			t.start();
			
					
		}
	}
	
	 @org.junit.Test
	    public void testServiceticket() throws Exception
	    {
		    String ticket = genTicket();
		    System.out.println(ticket);
	    	TokenCheckResponse response = checkTicket(ticket);
	    	
//	    	refreshTicket(ticket) ;
//	    	destroyTicket(ticket);
	    	checkTicket(ticket);
//	    	refreshTicket(ticket) ;
	    }
	 
	 @org.junit.Test
	    public void testHRMticket() throws Exception
	    {
		    String ticket ="656a868f-2ee5-4d15-af84-8009b8d5cf85";
	    	TokenCheckResponse response = checkTicket(ticket);
//	    	refreshTicket(ticket) ;
//	    	destroyTicket(ticket);
//	    	checkTicket(ticket);
//	    	refreshTicket(ticket) ;
	    }
	    //656a868f-2ee5-4d15-af84-8009b8d5cf85
	    public String genTicket() throws Exception

	    {

//	         String appid = "pdp";
//
//	         String secret = "ED6F601E3ABC7BA35836C56141AF8351";
//
//	         String account = "marc";//如果使用工号则loginType为2，否则为1
//
//	         String worknumber = "10006857";

	         //hessian服务方式申请token

	         HessianProxyFactory factory = new HessianProxyFactory();

	         //String url = "http://localhost:8080/context/hessian?service=tokenService";

	         String url = server + "/hessian?service=tokenService";

	         TokenService tokenService = (TokenService) factory.create(TokenService.class, url);

	         //通过hessian根据账号或者工号获取ticket



	         String ticket = tokenService.genTicket(account, worknumber, appid, secret);

	         return ticket;

	    }
	    
	    @Test
	    public void gentmpandchecktmpTicket() throws Exception

	    {

//	         String appid = "pdp";
//
//	         String secret = "ED6F601E3ABC7BA35836C56141AF8351";
//
//	         String account = "marc";//如果使用工号则loginType为2，否则为1
//
//	         String worknumber = "10006857";

	         //hessian服务方式申请token

	         HessianProxyFactory factory = new HessianProxyFactory();

	         //String url = "http://localhost:8080/context/hessian?service=tokenService";

	         String url = server + "/hessian?service=v2tokenService";

	         org.frameworkset.web.token.ws.v2.TokenService tokenService = (org.frameworkset.web.token.ws.v2.TokenService) factory.create(org.frameworkset.web.token.ws.v2.TokenService.class, url);

	         //通过hessian根据账号或者工号获取ticket



	         org.frameworkset.web.token.ws.v2.TicketGetResponse ticket = tokenService.getTempTicket(account, worknumber, appid, secret);

	         url = server + "/hessian?service=v2checktokenService";

	         org.frameworkset.web.token.ws.v2.CheckTokenService checkTokenService = (org.frameworkset.web.token.ws.v2.CheckTokenService) factory.create(org.frameworkset.web.token.ws.v2.CheckTokenService.class, url);

	         org.frameworkset.web.token.ws.v2.TokenCheckResponse tokenCheckResponse = checkTokenService.checkTicket(appid, secret, ticket.getTicket());

	         System.out.println(tokenCheckResponse.getResultcode());

	         System.out.println(tokenCheckResponse.getUserAccount());

	         System.out.println(tokenCheckResponse.getWorknumber());
	         
	         tokenCheckResponse = checkTokenService.checkTicket(appid, secret, ticket.getTicket());

	         System.out.println(tokenCheckResponse.getResultcode());

	         System.out.println(tokenCheckResponse.getUserAccount());

	         System.out.println(tokenCheckResponse.getWorknumber());

	    }


	    

	    public TokenCheckResponse checkTicket(String ticket) throws Exception

	    {

//	         String appid = "tas";
//
//	         String secret = "ED6F601E3ABC7BA35836C56141AF8351";

	         //hessian服务方式申请token

	         HessianProxyFactory factory = new HessianProxyFactory();

	         //String url = "http://localhost:8080/context/hessian?service=tokenService";

	         String url = server + "/hessian?service=checktokenService";

	         org.frameworkset.web.token.ws.CheckTokenService  checkTokenService = (CheckTokenService) factory.create(org.frameworkset.web.token.ws.CheckTokenService.class, url);

	         org.frameworkset.web.token.ws.TokenCheckResponse tokenCheckResponse = checkTokenService.checkTicket(appid, secret, ticket);

	         System.out.println(tokenCheckResponse.getResultcode());

	         System.out.println(tokenCheckResponse.getUserAccount());

	         System.out.println(tokenCheckResponse.getWorknumber());

	         return tokenCheckResponse;

	    }
	    
	    
	    public void refreshTicket(String ticket) throws Exception

	    {

	    	  HessianProxyFactory factory = new HessianProxyFactory();

		         //String url = "http://localhost:8080/context/hessian?service=tokenService";

		         String url = server + "/hessian?service=tokenService";

		         TokenService tokenService = (TokenService) factory.create(TokenService.class, url);

		         //通过hessian根据账号或者工号获取ticket



	         TicketGetResponse tokenCheckResponse = tokenService.refreshTicket(ticket,appid, secret );

	         System.out.println("refresh result:"+tokenCheckResponse.getResultcode());


	    }
	    
	    public void destroyTicket(String ticket) throws Exception

	    {

	    	  HessianProxyFactory factory = new HessianProxyFactory();

		         //String url = "http://localhost:8080/context/hessian?service=tokenService";

		         String url = server + "/hessian?service=tokenService";

		         TokenService tokenService = (TokenService) factory.create(TokenService.class, url);

		         //通过hessian根据账号或者工号获取ticket



	         TicketGetResponse tokenCheckResponse = tokenService.destroyTicket(ticket,appid, secret );

	         System.out.println("destroyTicket:"+tokenCheckResponse.getResultcode());


	    }

}
