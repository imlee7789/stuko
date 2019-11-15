package com.stuko.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.stuko.domain.BoardDTO;
import com.stuko.domain.BoardVO;
import com.stuko.domain.Criteria;
import com.stuko.domain.HotTopic;
import com.stuko.domain.SearchCriteria;
import com.stuko.persistence.BoardDAO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO dao;

	@Override
	public boolean regist(BoardDTO dto) throws Exception {
		
		log.info("BoardServiceImpl :: regist(BoardDTO dto) invoked");
		
		if(dao.create(dto) == 1) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public BoardVO readOne(int id) throws Exception {
		
		log.info("BoardServiceImpl :: read(Integer id) invoked");

		return dao.readOne(id);
	}
	
	@Override
	public BoardVO readLastOne(int course_id) throws Exception {
		
		log.info("BoardServiceImpl :: readLastOne(Integer id) invoked");

		return dao.readLastOne(course_id);
	}

	@Override
	public void modify(BoardDTO dto) throws Exception {
		
		log.info("BoardServiceImpl :: modify(BoardDTO dto) invoked");

		dao.update(dto);
	}

	@Override
	public void remove(Integer id) throws Exception {
		
		log.info("BoardServiceImpl :: remove(Integer id) invoked");

		dao.delete(id);
	}

	@Override
	public void rcmd_up(int boardId) throws Exception {
		
		log.info("BoardServiceImpl :: rcmd_up(BoardDTO dto) invoked");
		
		dao.rcmd_cnt(boardId);
	}

	@Override
	public boolean check_pw(int id, String user_pw) throws Exception {
		
		log.info("BoardServiceImpl :: check_pw() invoked");
		
		boolean result = false;
		
		String pw = dao.checkPw(id);
		
		// user_pw 와 pw를 비교.
		if(user_pw.equals(pw)) {
			
			result = true;
		}
		
		return result;
	}

	@Override
	public List<BoardVO> readAll(Criteria cri) throws Exception {

		log.info("BoardServiceImpl :: listAll() invoked");
		
		return dao.listAll(cri);
	}

	@Override
	public int readCountCriteria(Criteria cri) throws Exception {
		
		log.info("BoardServiceImpl :: listCountCriteria() invoked");
		
		return dao.countPaging(cri);
	}

	@Override
	public List<BoardVO> readSearch(SearchCriteria cri) throws Exception {
		
		log.info("BoardServiceImpl :: readSearch() invoked");
		
		return dao.listSearch(cri);
	}

	@Override
	public BoardVO readHotTopic(HotTopic hotTopic) throws Exception {
		
		log.info("BoardServiceImpl :: readHotTopic() invoked");
		
		return dao.hotTopic(hotTopic);
	}

}
