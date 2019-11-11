package com.stuko.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.stuko.domain.WordVO;

@Repository
public class WordDAOImpl 
	implements WordDAO {

	@Inject
	private SqlSession session;

	private static String namespace = "com.stuko.mapper.WordMapper";

	@Override
	public WordVO readWord(WordVO vo) {
		return session.selectOne(namespace + ".readWord", vo);
	}

	@Override
	public List<String> readWordsMatched(WordVO vo) {
		List<String> words = new ArrayList<>();
		words = session.selectList(namespace + ".readWordsMatched", vo);
		return words;
	}

	@Override
	public void createWord(WordVO vo) {
		session.insert(namespace + ".createWord", vo);
	}

	@Override
	public void deleteWord(int id) {
		session.delete(namespace + ".deleteWord", id);
	}

}
