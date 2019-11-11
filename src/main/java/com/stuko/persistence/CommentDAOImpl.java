package com.stuko.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.stuko.domain.CommentDTO;
import com.stuko.domain.CommentVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommentDAOImpl implements CommentDAO {

	@Inject
	private SqlSession session;

	private static String namespace = "com.stuko.mapper.CommentMapper";

	@Override
	public List<CommentVO> commentList(Integer bulletin_id) throws Exception {

		log.info("CommentDAOImpl :: list() invoked.");

		return session.selectList(namespace + ".list", bulletin_id);
	}

	@Override
	public void commentCreate(CommentDTO dto) throws Exception {

		log.info("CommentDAOImpl :: create() invoked.");

		session.insert(namespace + ".create", dto);
	}

	@Override
	public int getBoardId(Integer id) throws Exception {
		
		return session.selectOne(namespace+".getBoardId", id);
	}

	@Override
	public CommentVO readLastOne(int boardid) throws Exception {
		// TODO Auto-generated method stub
		return session.selectOne(namespace+".readLastOne", boardid);
	}

}
