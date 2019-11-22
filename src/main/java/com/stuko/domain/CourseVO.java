package com.stuko.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CourseVO {

	private int id;
	private String conn_pw;
	private String course_name;
	private String description;
	private Timestamp insert_ts;
}
