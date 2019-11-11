package com.stuko.service;

import java.util.List;

import com.stuko.domain.BoardDTO;
import com.stuko.domain.BoardVO;
import com.stuko.domain.Criteria;
import com.stuko.domain.HotTopic;
import com.stuko.domain.SearchCriteria;


public interface BoardService {

	public void regist(BoardDTO dto) throws Exception;
	
	public BoardVO readOne(int boardId) throws Exception;
	
	public BoardVO readLastOne(int course_id) throws Exception;
	
	public void modify(BoardDTO dto) throws Exception;
	
	public void remove(Integer id) throws Exception;
	
	public void rcmd_up(int boardid) throws Exception;
	
	public boolean check_pw(int id, String user_pw) throws Exception;
	
	public List<BoardVO> readAll(Criteria cri) throws Exception;
	
	public int readCountCriteria(Criteria cri) throws Exception;
	
	public List<BoardVO> readSearch(SearchCriteria cri) throws Exception;
	
	public BoardVO readHotTopic(HotTopic hotTopic) throws Exception;
}
