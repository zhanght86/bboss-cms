package com.sany.session.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.security.session.impl.SessionHelper;
import org.frameworkset.security.session.statics.SessionAPP;
import org.frameworkset.security.session.statics.SessionInfo;

import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.util.ListInfo;
import com.sany.session.entity.SessionCondition;
import com.sany.session.entity.SessionInfoBean;
import com.sany.session.service.SessionManagerService;

public class SessionManagerServiceImpl implements SessionManagerService {

	@Override
	public ListInfo querySessionDataForPage(SessionCondition condition,
			int offset, int pagesize) {

		ListInfo list = new ListInfo();
		
		try {

			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("appKey", condition.getAppkey());
			queryParams.put("sessionid", condition.getSessionid());
			queryParams
					.put("createtime_start", condition.getCreatetime_start());
			queryParams.put("createtime_end", condition.getCreatetime_end());
			queryParams.put("host", condition.getHost());
			queryParams.put("referip", condition.getReferip());

			if (!StringUtil.isEmpty(condition.getValidate())) {

				queryParams.put("validate",
						condition.getValidate().equals("1") ? "true" : "false");
			}

			List<SessionInfo> infoList = SessionHelper
					.getSessionStaticManager().getAllSessionInfos(queryParams,
							pagesize, offset);

			List<SessionInfoBean> beanList = new ArrayList<SessionInfoBean>();
			if (infoList != null && infoList.size() != 0) {

				for (SessionInfo info : infoList) {
					SessionInfoBean bean = new SessionInfoBean();
					bean.setAppKey(info.getAppKey());
					bean.setAttributes(info.getAttributes());
					bean.setCreationTime(info.getCreationTime());
					bean.setHost(info.getHost());
					bean.setLastAccessedTime(info.getLastAccessedTime());
					bean.setMaxInactiveInterval(formatDuring(info
							.getMaxInactiveInterval()));
					bean.setReferip(info.getReferip());
					bean.setSessionid(info.getSessionid());
					bean.setValidate(info.isValidate());

					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(info.getLastAccessedTime());
					gc.add(Calendar.MILLISECOND,
							(int) info.getMaxInactiveInterval());

					bean.setLoseTime(gc.getTime());
					bean.setRequesturi(info.getRequesturi());
					bean.setLastAccessedUrl(info.getLastAccessedUrl());
					beanList.add(bean);
				}
				list.setMore(true);
				list.setResultSize(infoList.size());
			}

			list.setDatas(beanList);
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return list;
	}

	@Override
	public List<SessionAPP> queryAppSessionData(String appKey) {

		return SessionHelper.getSessionStaticManager().getSessionAPP();
	}

	@Override
	public void delSession(String appkey, String sessionids) {
		try {

			String[] ids = sessionids.split(",");

			for (String sessionid : ids) {

				if (StringUtil.isNotEmpty(sessionid)) {

					SessionHelper.getSessionStaticManager().removeSessionInfo(
							appkey, sessionid);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

	@Override
	public SessionInfoBean getSessionInfo(String appkey, String sessionid) {
		try {

			SessionInfo info = SessionHelper.getSessionStaticManager()
					.getSessionInfo(appkey, sessionid);

			SessionInfoBean bean = new SessionInfoBean();
			bean.setAppKey(info.getAppKey());
			bean.setAttributes(info.getAttributes());
			bean.setCreationTime(info.getCreationTime());
			bean.setHost(info.getHost());
			bean.setLastAccessedTime(info.getLastAccessedTime());
			bean.setMaxInactiveInterval(formatDuring(info
					.getMaxInactiveInterval()));

			bean.setReferip(info.getReferip());
			bean.setSessionid(info.getSessionid());
			bean.setValidate(info.isValidate());
			bean.setRequesturi(info.getRequesturi());
			bean.setLastAccessedUrl(info.getLastAccessedUrl());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(info.getLastAccessedTime());
			gc.add(Calendar.MILLISECOND, (int) info.getMaxInactiveInterval());

			bean.setLoseTime(gc.getTime());

			return bean;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void delAllSessions(String appkey) {
		try {
			SessionHelper.getSessionStaticManager().removeAllSession(appkey);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;

		StringBuffer sb = new StringBuffer();
		if (days != 0) {
			sb.append(days + "天");
		}
		if (hours != 0) {
			sb.append(hours + "小时");
		}
		if (minutes != 0) {
			sb.append(minutes + "分钟");
		}
		if (seconds != 0) {
			sb.append(seconds + "秒");
		}

		return sb.toString();
	}

}
