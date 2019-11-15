package com.stuko.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.stuko.domain.BoardDTO;
import com.stuko.domain.BoardVO;
import com.stuko.domain.Criteria;
import com.stuko.domain.HotTopic;
import com.stuko.domain.SearchCriteria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BoardDAOImpl implements BoardDAO {

	@Inject
	private SqlSession session;

	private static String namespace = "com.stuko.mapper.BoardMapper";

	@Override
	public int create(BoardDTO dto) throws Exception {

		log.info("BoardDAOImpl :: create() invoked.");

		return session.insert(namespace + ".create", dto);
	}

	@Override
	public BoardVO readOne(int id) throws Exception {

		log.info("BoardDAOImpl :: read() invoked.");

		return session.selectOne(namespace + ".read", id);
	}
	
	@Override
	public BoardVO readLastOne(int course_id) throws Exception {

		log.info("BoardDAOImpl :: readLastOne() invoked.");

		return session.selectOne(namespace + ".readLastOne", course_id);
	}

	@Override
	public void update(BoardDTO dto) throws Exception {

		log.info("BoardDAOImpl :: update() invoked.");

		session.update(namespace + ".update", dto);
	}

	@Override
	public void delete(Integer id) throws Exception {
		
		log.info("BoardDAOImpl :: delete() invoked.");

		session.delete(namespace+".delete",id);
	}

	@Override
	public void rcmd_cnt(int boardId) throws Exception {
		
		log.info("BoardDAOImpl :: recommend_count() invoked.");
		
		session.update(namespace+".recommend", boardId);
	}

	@Override
	public String checkPw(int id) throws Exception {
		
		log.info("BoardDAOImpl :: checkPw() invoked.");
		
		String pw = session.selectOne(namespace+".checkPw", id);
		
		return pw;
	}

	@Override
	public List<BoardVO> listAll(Criteria cri) throws Exception {
		
		log.info("BoardDAOImpl :: listAll() invoked.");
		
		return session.selectList(namespace+".listAll", cri);
	}

	@Override
	public int countPaging(Criteria cri) throws Exception {
		
		log.info("BoardDAOImpl :: countPaging() invoked.");
		
		return session.selectOne(namespace+".countPaging", cri);
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		
		log.info("BoardDAOImpl :: listSearch() invoked.");
		
		return session.selectList(namespace+".listSearch", cri);
	}

	@Override
	public void updateCommentCnt(Integer id, int amount) throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("id", id);
		paramMap.put("amount", amount);
		
		session.update(namespace+".updateCommentCnt", paramMap);
	}

	@Override
	public BoardVO hotTopic(HotTopic hotTopic) throws Exception {
		
		log.info("BoardDAOImpl :: hotTopic() invoked.");
		
		return session.selectOne(namespace+".hotTopic", hotTopic);
	}

}
