package com.stuko.service;

import com.stuko.domain.DefinitionDTO;
import com.stuko.domain.WordDTO;

//@Service
public interface DictService {

	public WordDTO readWord(String courseId, String word_name) 
		throws NullPointerException;

	// 특정 단어에 def를 추가한다.
	// param
	// - courseId
	// - word_name
	// - DefinitionDTO
	public void createNewDef(
			String courseId, 
			String word_name, 
			DefinitionDTO defdto);
	
	public void replaceDef(int defId, DefinitionDTO defdto) throws Exception;
	
	public void deleteDef(int defId, DefinitionDTO defdto) throws Exception;
	
}
