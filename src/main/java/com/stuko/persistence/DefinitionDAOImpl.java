package com.stuko.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.stuko.domain.DefinitionVO;
import com.stuko.domain.WordVO;

@Repository
public class DefinitionDAOImpl
	implements DefinitionDAO {

	@Inject
	private SqlSession session;

	private static String namespace = "com.stuko.mapper.DefinitionMapper";

	@Override
	public List<DefinitionVO> readDefs(WordVO vo) 
		throws NullPointerException {
		
		List<DefinitionVO> defs = new ArrayList<>();
		defs = session.selectList(namespace + ".readDefs", vo);
		
		return defs;
	}

	@Override
	public DefinitionVO readDef(int id) {
		DefinitionVO vo = session.selectOne(namespace + ".readDef", id);
		return vo;
	}

	@Override
	public void insertDef(DefinitionVO vo) {
		session.insert(namespace + ".insertDef", vo);
	}

	@Override
	public int getLastInsertedId() {
		int lastInserted = session.selectOne(namespace + ".getLastInsertedId");
		return lastInserted;
	}

	@Override
	public void updateDef(DefinitionVO vo) {
		session.update(namespace + ".updateDef", vo);
	}

	@Override
	public void increaseRcmdCnt(int id) {
		session.update(namespace + ".updateDef", id);
	}

	@Override
	public void deleteDef(int id) {
		session.delete(namespace + ".deleteDef", id);
	}

}
