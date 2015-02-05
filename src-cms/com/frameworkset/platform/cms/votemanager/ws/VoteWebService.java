package com.frameworkset.platform.cms.votemanager.ws;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "VoteWebService", targetNamespace = "com.frameworkset.platform.cms.votemanager.ws")
public interface VoteWebService {

	// 根据用户工号获取投票列表
	public @WebResult(name = "votes", partName = "partVotes")
	List<VoteTitle> getVoteListByWorkNo(
			@WebParam(name = "workNo", partName = "partWorkNo") String workNo,
			@WebParam(name = "siteName", partName = "partSiteName") String siteName);

	// 获取投票待办数
	public @WebResult(name = "voteCount", partName = "partVoteCount")
	String getVoteCount(
			@WebParam(name = "workNo", partName = "partWorkNo") String workNo,
			@WebParam(name = "siteName", partName = "partSiteName") String siteName);
}
