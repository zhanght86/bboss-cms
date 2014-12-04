package org.frameworkset.token;

import org.frameworkset.security.ecc.ECCHelper;
import org.frameworkset.web.token.DBTokenStore;
import org.frameworkset.web.token.MemToken;
import org.frameworkset.web.token.TokenStore;
import org.frameworkset.web.token.ws.CheckTokenService;
import org.frameworkset.web.token.ws.TokenCheckResponse;
import org.frameworkset.web.token.ws.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.sany.application.service.impl.SYSValidationApplication;

public class DBTokenTest {
	private static DBTokenStore mongodbTokenStore;
	private String account = "yinbp";
	private String worknumber = "10006673";
	private String appid = "pdp";
	private String secret = "47430a44-2f5a-4fdb-a5b1-c3544230739f";
	
	
	public void init() throws Exception
	{
		mongodbTokenStore = new DBTokenStore();
		mongodbTokenStore.setECCCoder(ECCHelper.getECCCoder());
		mongodbTokenStore.setValidateApplication(new SYSValidationApplication());
		mongodbTokenStore.setTempTokendualtime(TokenStore.DEFAULT_TEMPTOKENLIVETIME);
		mongodbTokenStore.setTicketdualtime(TokenStore.DEFAULT_TICKETTOKENLIVETIME);
		mongodbTokenStore.setDualtokenlivetime(TokenStore.DEFAULT_DUALTOKENLIVETIME);
		String ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		MemToken token = mongodbTokenStore.genDualToken(appid,ticket,secret,TokenStore.DEFAULT_DUALTOKENLIVETIME);
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
		token = mongodbTokenStore.genTempToken();
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(null,null,token.getToken()).getResult());
		token = mongodbTokenStore.genAuthTempToken(appid,ticket,secret);
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
	}
	@Test
	public void genTemptokenAndValidate() throws Exception
	{		
		
		MemToken token = mongodbTokenStore.genTempToken();
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(null,null,token.getToken()).getResult());
	}
	
	@Test
	public void gendualtokenAndValidate() throws Exception
	{
		String ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		//long start = System.currentTimeMillis();
		MemToken token = mongodbTokenStore.genDualToken(appid,ticket,secret,TokenStore.DEFAULT_DUALTOKENLIVETIME);
		//long end = System.currentTimeMillis();
		//System.out.println(end - start);
		//start = System.currentTimeMillis();
		Assert.assertTrue(TokenStore.token_request_validateresult_ok == mongodbTokenStore.checkToken(appid,secret,token.getToken()).getResult());
		//end = System.currentTimeMillis();
		//System.out.println(end - start);
	}
	
	
	@Test
	public void gentempauthortokenAndValidate() throws Exception
	{
		String ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		MemToken token = mongodbTokenStore.genAuthTempToken(appid,ticket,secret);
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
		String ticket = mongodbTokenStore.genTicket(account, worknumber, appid, secret);
		System.out.println(":\n"+ticket);
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
							s.gendualtokenAndValidate();
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
	    	TokenCheckResponse response = checkTicket(genTicket());
	    }
	    
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

	         String url = "http://pdp.sany.com.cn:8080/hessian?service=tokenService";

	         TokenService tokenService = (TokenService) factory.create(TokenService.class, url);

	         //通过hessian根据账号或者工号获取ticket



	         String ticket = tokenService.genTicket(account, worknumber, appid, secret);

	         return ticket;

	    }

	    

	    public TokenCheckResponse checkTicket(String ticket) throws Exception

	    {

//	         String appid = "tas";
//
//	         String secret = "ED6F601E3ABC7BA35836C56141AF8351";

	         //hessian服务方式申请token

	         HessianProxyFactory factory = new HessianProxyFactory();

	         //String url = "http://localhost:8080/context/hessian?service=tokenService";

	         String url = "http://pdp.sany.com.cn:8080/hessian?service=checktokenService";

	         org.frameworkset.web.token.ws.CheckTokenService  checkTokenService = (CheckTokenService) factory.create(org.frameworkset.web.token.ws.CheckTokenService.class, url);

	         org.frameworkset.web.token.ws.TokenCheckResponse tokenCheckResponse = checkTokenService.checkTicket(appid, secret, ticket);

	         System.out.println(tokenCheckResponse.getResultcode());

	         System.out.println(tokenCheckResponse.getUserAccount());

	         System.out.println(tokenCheckResponse.getWorknumber());

	         return tokenCheckResponse;

	    }

}
