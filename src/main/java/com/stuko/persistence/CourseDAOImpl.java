package com.stuko.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.stuko.domain.CourseDTO;
import com.stuko.domain.CourseVO;

@Repository
public class CourseDAOImpl 
	implements CourseDAO {

	@Inject
	private SqlSession session;

	private static String namespace = "com.stuko.mapper.CourseMapper";

	@Override
	public void createCourse(CourseDTO cdto) {
		session.insert(namespace + ".createCourse", cdto);
	}

	@Override
	public List<CourseVO> readCourse(){
		List<CourseVO> courses = new ArrayList<>();
		courses = session.selectList(namespace + ".readCourse");
		return courses;
	}

	@Override
	public CourseVO connectCourse(CourseDTO cdto){
		CourseVO cvo = session.selectOne(namespace + ".connectCourse", cdto);
		
		return cvo;
	}

	@Override
	public void updateCourse(CourseDTO cdto){
		session.update(namespace + ".updateCourse", cdto);
	}

	@Override
	public void deleteCourse(CourseDTO cdto){
		session.delete(namespace + ".updateCourse", cdto);
	}

}
