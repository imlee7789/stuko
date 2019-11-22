//package com.stuko.mapper;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.Update;
//
//import com.stuko.domain.CourseDTO;
//import com.stuko.domain.CourseVO;
//
//public interface CourseMapper {
//	
//	@Insert("INSERT INTO tbl_course (conn_pw, course_name, description) VALUES (#{conn_pw}, #{course_name}, #{description})")
//	void createCourse(CourseDTO cdto);
//
//	@Select("SELECT * FROM tbl_course")
//	List<CourseVO> readCourse();
//	
//	@Select("SELECT * FROM tbl_course WHERE conn_pw=#{conn_pw}")
//	CourseVO ConnectCourse(CourseDTO cdto);
//	
//	@Update("UPDATE tbl_course SET conn_pw=#{conn_pw}, ch_name=#{course_name}, description=#{description}")
//	CourseVO updateCourse(CourseDTO cdto);
//	
//	@Delete("DELETE FROM tbl_course WHERE (id=#{id})")
//	void deleteCourse(CourseDTO cdto);
//	
//} // end interface
