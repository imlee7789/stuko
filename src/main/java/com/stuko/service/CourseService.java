package com.stuko.service;

import java.util.List;

import com.stuko.domain.CourseDTO;
import com.stuko.domain.CourseVO;

public interface CourseService {

	void courseCreate(CourseDTO cdto);
	
	List<CourseVO> courseInfo() throws Exception;
	
	CourseVO courseModify(CourseDTO cdto);
	
	void courseDelete(CourseDTO cdto);
	
	boolean pwCheck(CourseDTO cdto);
	
}
