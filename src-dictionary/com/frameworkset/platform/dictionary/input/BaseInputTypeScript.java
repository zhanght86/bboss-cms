package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import com.frameworkset.platform.dictionary.DictAttachField;
import com.frameworkset.platform.dictionary.KeyWord;

public abstract class BaseInputTypeScript implements InputTypeScript {
	protected DictAttachField dictatt;
	public BaseInputTypeScript(DictAttachField dictatt)
	{
		this.dictatt = dictatt;
	}

	public String getFunctionScript(String fiedName, String inputTypeName, String value, int maxLength) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFunctionContent(String fiedName, String inputTypeName, String value, int maxLength) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 判断当前字段是不是关键字段，如果是返回true，否则返回false
	 * @param keyWords
	 * @return
	 */
	protected boolean isReadOnly(Map keyWords)
	{
		if(keyWords == null)
			return false;
		KeyWord key = (KeyWord)keyWords.get(dictatt.getTable_column().toUpperCase());
		if(key != null)
			return true;
		return false;
	}

//	public String getExtendHtmlContent(DictAttachField dictatt) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
