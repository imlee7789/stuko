package com.stuko.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Class	WordVO
 * 
 * Name		Desc	Date			
 * hslee	made	2019.09.25	
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordVO {

	private int id;
	private String word_name;	// 생성 후 변경 필요 X
	private int course_id;
	private Timestamp insert_ts;
	private Timestamp search_ts;
	
}
