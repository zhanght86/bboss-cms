package com.frameworkset.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.frameworkset.platform.config.ConfigManager;

import edu.yale.its.tp.cas.client.filter.CASFilter;

public class CasFilter extends CASFilter {

	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain fc) throws ServletException, IOException {
		boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer", false);
		if(isCasServer){
			super.doFilter(request, response, fc);
		}else{
			fc.doFilter(request, response);
		}
	}

}
