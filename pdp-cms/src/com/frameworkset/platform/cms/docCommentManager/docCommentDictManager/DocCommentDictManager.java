package com.frameworkset.platform.cms.docCommentManager.docCommentDictManager;


public interface DocCommentDictManager extends java.io.Serializable {
	/**
	 * 增加一新词汇
	 * @param dictWord
	 * @throws DocCommentDictManagerException
	 */
	public void addOneWord(DocCommentDict dictWord) throws DocCommentDictManagerException;
	/**
	 * 删除指定的词汇
	 * @param wordId
	 * @throws DocCommentDictManagerException
	 */
	public void deleteOneWord(String wordId) throws DocCommentDictManagerException;
	/**
	 * 批量删除词汇
	 * @param wordIds
	 * @throws DocCommentDictManagerException
	 */
	public void deleteWords(String[] wordIds) throws DocCommentDictManagerException;
	/**
	 * 更新指定的词汇
	 * @param wordId
	 * @param dictWord
	 * @throws DocCommentDictManagerException
	 */
	public void updateWord(String wordId,DocCommentDict dictWord) throws DocCommentDictManagerException;
	
}
