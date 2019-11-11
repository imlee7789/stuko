package com.stuko.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {

	private Integer id;
	private Integer bulletin_id;
	private String user_id;
	private String content;
	private Timestamp insert_ts;
}
