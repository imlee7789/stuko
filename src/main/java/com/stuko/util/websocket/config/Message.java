package com.stuko.util.websocket.config;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// DTO와 같은 역할이다.
// Message라는 이름일뿐.... 이름바꿔도 되지!
public class Message {


	/******************************
	 * 
	 *  Message for chatting
	 * 
	 ******************************/
	final static public int REQ_NICK	= 1;
	final static public int REQ_CHAT	= 2;
	final static public int REQ_COUNT	= 3;
	final static public int REQ_TIMELINE= 4;

	
	private int reqType;
	private String nickName;
	private String message;
	private int count;


	/******************************
	 * 
	 *  boardDTO
	 * 
	 ******************************/
	private int id;
	private String user_id;		// not null
	private String user_pw;		// not null
	private String content;		// not null
	private int rcmd_cnt;
	private int course_id;		// URI로 받음
	private Date insert_ts;
	private int comment_cnt;

}