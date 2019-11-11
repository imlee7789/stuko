package com.stuko.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Class	DefinitionVO
 * 
 * Name		Desc	Date			
 * hslee	made	2019.09.25	
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefinitionVO {
	
	private int id;
	private int word_id;
	private String content;
	private String login_id;
	private int rcmd_cnt;
	private Timestamp insert_ts;

}
