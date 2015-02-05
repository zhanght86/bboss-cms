package com.frameworkset.platform.cms.votemanager;

import java.util.List;

import javax.jws.WebService;

import com.frameworkset.platform.cms.votemanager.ws.VoteTitle;
import com.frameworkset.platform.cms.votemanager.ws.VoteWebService;
import com.frameworkset.platform.cms.voteservice.VoteMobileService;

@WebService(name = "VoteWebService", targetNamespace = "com.frameworkset.platform.cms.votemanager.ws")
public class VoteWebServiceImpl implements VoteWebService {
	private VoteMobileService voteService;
	@Override
	public List<VoteTitle> getVoteListByWorkNo(String workNo, String siteName) {
		// TODO Auto-generated method stub
		return voteService.getVoteListByWorkNo(workNo, siteName);
	}

	@Override
	public String getVoteCount(String workNo, String siteName) {
		// TODO Auto-generated method stub
		return voteService.getVoteCount(workNo, siteName);
	}
}
