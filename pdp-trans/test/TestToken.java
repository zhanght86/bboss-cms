import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.trans.entity.CmsWxtoken;
import org.frameworkset.trans.service.CmsWxtokenService;
import org.frameworkset.trans.service.CmsWxtokenServiceImpl;
import org.junit.Test;

/**
 * 
 */

/**
 * @author suwei
 * @date 2016年11月12日
 *
 */
public class TestToken {
	@Test
	public void testWX() {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("org/frameworkset/trans/util/bboss-token.xml");

		CmsWxtokenService cmsWxtokenService = context.getTBeanObject("token.cmsWxtokenService", CmsWxtokenServiceImpl.class);
		try {
			CmsWxtoken cmsWxtoken=	cmsWxtokenService.getCmsWxtoken();
			System.out.println(cmsWxtoken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
