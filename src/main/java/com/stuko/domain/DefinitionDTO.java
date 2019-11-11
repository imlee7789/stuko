package com.stuko.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefinitionDTO {
	
	// DictService.readWord
	private int id;
	private String content;
	private int rcmd_cnt;
	private Timestamp insert_ts;
	private RefSiteDTO[] refs;
	
	// DictService.addNewDef
	private String login_id;
}
