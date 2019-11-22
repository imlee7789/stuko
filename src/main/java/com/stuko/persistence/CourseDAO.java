package com.stuko.persistence;

import java.util.List;

import com.stuko.domain.CourseDTO;
import com.stuko.domain.CourseVO;

public interface CourseDAO {

	public abstract void createCourse(CourseDTO cdto);
	
	public abstract List<CourseVO> readCourse();

	public abstract CourseVO connectCourse(CourseDTO cdto);

	public abstract void updateCourse(CourseDTO cdto);
	
	public abstract void deleteCourse(CourseDTO cdto);
	
}