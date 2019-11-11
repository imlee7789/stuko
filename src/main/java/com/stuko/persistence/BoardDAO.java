package com.stuko.persistence;

import java.util.List;

import com.stuko.domain.BoardDTO;
import com.stuko.domain.BoardVO;
import com.stuko.domain.Criteria;
import com.stuko.domain.HotTopic;
import com.stuko.domain.SearchCriteria;


public interface BoardDAO {

	public void create(BoardDTO dto) throws Exception;
	
	public BoardVO readOne(int id) throws Exception;
	
	public BoardVO readLastOne(int course_id) throws Exception;
	
	public void update(BoardDTO dto) throws Exception;
	
	public void delete(Integer id) throws Exception;
	
	public List<BoardVO> listAll(Criteria cri) throws Exception;
	
	public void rcmd_cnt(int boardId) throws Exception;
	
	public String checkPw(int id) throws Exception;

	public int countPaging(Criteria cri) throws Exception;
	
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	
	public void updateCommentCnt(Integer boardId, int amount) throws Exception;
	
	public BoardVO hotTopic(HotTopic hotTopic) throws Exception;
}
