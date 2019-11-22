package com.stuko.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.stuko.domain.CourseDTO;
import com.stuko.domain.CourseVO;
import com.stuko.persistence.CourseDAO;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class CourseServiceImpl implements CourseService {

	@Inject
	private CourseDAO cMapper;
	
	@Override
	public void courseCreate(CourseDTO cdto) {
		
		log.info(">>>>코스 생성 호출");
		
		cMapper.createCourse(cdto);
		
		log.info(">>>>코스 생성 성공");
		
	} //courseCreate

	@Override
	public List<CourseVO> courseInfo() throws Exception {
		
		log.info(">>>>코스 정보 확인 호출");
		
		List<CourseVO> cvoList = cMapper.readCourse();
		
		log.info(">>>>코스 정보 호출 성공");
		
		return cvoList;
		
	} //courseInfo

	@Override
	public CourseVO courseModify(CourseDTO cdto) {
		
		log.info(">>>>코스 정보 변경 호출");
		
//		CourseVO cvo = cMapper.updateCourse(cdto);
		cMapper.updateCourse(cdto);
		
		log.info(">>>>코스 정보 변경 성공");
		
		return null;
		
	} //courseModify

	@Override
	public void courseDelete(CourseDTO cdto) {
		
		log.info(">>>>코스 삭제 호출");
		
		cMapper.deleteCourse(cdto);
		
		log.info(">>>>코스 삭제 성공");
		
	} //courseDelete

	public boolean pwCheck(CourseDTO cdto) {
		
		log.info(">>>>코스 비밀번호 확인 invoked");
		
		log.info(cdto.getConn_pw());
		
		CourseVO cvo = cMapper.connectCourse(cdto);

		if(cvo != null) {
			log.info(">>>>코스 비밀번호 확인 Success");
			return true;
			
		}else {
			log.info(">>>>코스 비밀번호 확인 Fail");
			return false;
			
		}
		
	} //pwCheck
	
} //end class
