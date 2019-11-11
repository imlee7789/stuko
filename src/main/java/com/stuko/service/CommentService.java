package com.stuko.service;

import java.util.List;

import com.stuko.domain.CommentDTO;
import com.stuko.domain.CommentVO;

public interface CommentService {

	public List<CommentVO> commentRead(Integer id) throws Exception;
	
	public void commentModify(CommentDTO dto) throws Exception;

	public CommentVO readLastOne(int boardid) throws Exception;
}
