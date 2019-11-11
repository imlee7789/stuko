package com.stuko.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardVO {

	private int id;
	private String user_id;
	private String user_pw;
	private String content;
	private int rcmd_cnt;
	private int course_id;
	private Timestamp insert_ts;
	private int comment_cnt;
}
