package com.stuko.persistence;

import java.util.List;

import com.stuko.domain.CommentDTO;
import com.stuko.domain.CommentVO;

public interface CommentDAO {

	public List<CommentVO> commentList(Integer bulletin_id) throws Exception;
	
	public void commentCreate(CommentDTO dto) throws Exception;
	
	public int getBoardId(Integer id) throws Exception;

	public CommentVO readLastOne(int boardid) throws Exception;
}
