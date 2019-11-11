package com.stuko.persistence;

import java.util.List;

import com.stuko.domain.DefinitionVO;
import com.stuko.domain.WordVO;

public interface DefinitionDAO {
	
	public abstract List<DefinitionVO> readDefs(WordVO vo) throws NullPointerException;
	
	public abstract DefinitionVO readDef(int id);

	public abstract void insertDef(DefinitionVO vo);

	public abstract int getLastInsertedId();

	public abstract void updateDef(DefinitionVO vo);
	
	public abstract void increaseRcmdCnt(int id);
	
	public abstract void deleteDef(int id);
}
