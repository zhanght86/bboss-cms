package com.frameworkset.platform.cms.docCommentManager.docCommentDictManager;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;


public class DocCommentDictManagerImpl implements DocCommentDictManager{

	public void addOneWord(DocCommentDict dictWord) throws DocCommentDictManagerException{
		PreparedDBUtil preparedDB = new PreparedDBUtil();
		String sql = "insert into td_cms_doccomment_dict(word,description,siteid,id) values(?,?,?,?)";
		try {
			long dictId = preparedDB.getNextPrimaryKey("td_cms_doccomment_dict") ;
			
			preparedDB.preparedInsert(sql);
			preparedDB.setString(1, dictWord.getWord());
			preparedDB.setString(2, dictWord.getDescription());
			preparedDB.setLong(3, dictWord.getSiteId());
			preparedDB.setLong(4,dictId) ;
			preparedDB.executePrepared();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentDictManagerException("新词汇插入失败！" + e.getMessage());
		}
		
	}

	public void deleteOneWord(String wordId) throws DocCommentDictManagerException{
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_doccomment_dict where id = " + wordId;
		try {
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentDictManagerException("删除词汇失败！" + e.getMessage());
		}
	}

	public void deleteWords(String[] wordIds) throws DocCommentDictManagerException{
		try{
			for(int i=0;i<wordIds.length;i++){
				this.deleteOneWord(wordIds[i]);
			}
		}catch (DocCommentDictManagerException e) {
			throw e;
		}
	}

	public void updateWord(String wordId, DocCommentDict dictWord) throws DocCommentDictManagerException {
		PreparedDBUtil preparedDB = new PreparedDBUtil();
		String sql = "update td_cms_doccomment_dict set word=?,description=?,siteid=? where id = " + wordId;
		try {
			preparedDB.preparedUpdate(sql);
			preparedDB.setString(1, dictWord.getWord());
			preparedDB.setString(2, dictWord.getDescription());
			preparedDB.setLong(3, dictWord.getSiteId());
			preparedDB.executePrepared();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentDictManagerException("更新词汇失败！" + e.getMessage());
		}	
	}

}
