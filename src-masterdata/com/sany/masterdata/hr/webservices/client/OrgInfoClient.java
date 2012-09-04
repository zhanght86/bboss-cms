/*
 * @(#)JobInfoClient.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.masterdata.hr.webservices.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.sany.masterdata.hr.entity.WebServiceProperties;
import com.sany.masterdata.hr.entity.orginfo.ArrayOfOrgInfo;
import com.sany.masterdata.hr.entity.orginfo.GetOrgInfoList;
import com.sany.masterdata.hr.entity.orginfo.GetOrgInfoListPortType;
import com.sany.masterdata.hr.entity.orginfo.ObjectFactory;
import com.sany.masterdata.hr.entity.orginfo.Parameter;
import com.sany.masterdata.utils.MDPropertiesUtil;

/**
 * human master organization data web services client
 * @author caix3
 * @since 2012-3-21
 */
public class OrgInfoClient {

    private static final WebServiceProperties PROPERTIES = MDPropertiesUtil.getBean("hdmWebServicesUrlOrg");

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static final QName SERVICE_NAME = new QName(PROPERTIES.getNameSpace(), PROPERTIES.getName());

    private static Logger logger = Logger.getLogger(UserInfoClient.class);
    private GetOrgInfoListPortType port;
    private void init() throws MalformedURLException
    {
    	if(port == null)
    	{
    		URL wsdlURL = null;
       
             // create connection
            wsdlURL = new URL(PROPERTIES.getUrl());
            GetOrgInfoList services = new GetOrgInfoList(wsdlURL, SERVICE_NAME);
            port = services.getGetOrgInfoListHttpPort();
    	}
            
    }
    /**
     * call web services get human master data organization list
     * @param stDate data onchange date start
     * @param enDate data onchange date end
     * @param low page start size
     * @param high page end size
     * @return ArrayOfUserInfo
     */
    public ArrayOfOrgInfo getData(Date stDate, Date enDate, int low, int high) {

        
        ArrayOfOrgInfo response = null;
        try {
            // create connection
        	init();

            // create parameter
            ObjectFactory factory = new ObjectFactory();
            Parameter parameter = factory.createParameter();

            if (stDate != null) {
                parameter.setBigeinDate(factory.createParameterBigeinDate(SDF.format(stDate)));
            }
            if (enDate != null) {
                parameter.setBigeinDate(factory.createParameterBigeinDate(SDF.format(enDate)));
            }
            if (low > 0) {
                parameter.setLow(factory.createParameterLow(low + ""));
            }
            if ((high > low) && (high > 0)) {
                parameter.setHigh(factory.createParameterHigh(high + ""));
            }

            // call web services
            response = port.getOrgList(parameter);

        } catch (Exception e) {
            logger.error("call user info web services error,stDate[" + stDate + "],enDate[" + enDate + "],low[" + low
                    + "],high[" + high + "]", e);
        }

        return response;
    }

    /**
     * get page size
     * @return Integer
     */
    public Integer getPageSize() {
        return PROPERTIES.getPageSize();
    }
}
