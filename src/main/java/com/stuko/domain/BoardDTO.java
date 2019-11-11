package com.stuko.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	
	private int id;
	private String user_id;		// not null
	private String user_pw;		// not null
	private String content;		// not null
	private int rcmd_cnt;
	private int course_id;		// URI로 받음
	private Date insert_ts;
	private int comment_cnt;
}
