package com.stuko.persistence;

import java.util.List;

import com.stuko.domain.WordVO;

public interface WordDAO {

	public abstract WordVO readWord(WordVO vo);
	
	public abstract List<String> readWordsMatched(WordVO vo);

	public abstract void createWord(WordVO vo);

	public abstract void deleteWord(int id);
	
}
