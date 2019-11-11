package com.stuko.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stuko.domain.CommentDTO;
import com.stuko.domain.CommentVO;
import com.stuko.persistence.BoardDAO;
import com.stuko.persistence.CommentDAO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
	
	@Inject
	private CommentDAO dao;
	
	@Inject
	private BoardDAO bDao;

	@Override
	public List<CommentVO> commentRead(Integer id) throws Exception {
		
		log.info("CommentServiceImpl :: commentRead(Integer id) invoked");

		return dao.commentList(id);
	}

	@Transactional
	@Override
	public void commentModify(CommentDTO dto) throws Exception {

		log.info("CommentServiceImpl :: commentModify(BoardDTO dto) invoked");

		dao.commentCreate(dto);
		bDao.updateCommentCnt(dto.getBulletin_id(), 1);
	}

	@Override
	public CommentVO readLastOne(int boardid) throws Exception{
		log.info("CommentServiceImpl :: readLastOne(Integer boardid) invoked");

		return dao.readLastOne(boardid);
	}

}
