package com.stuko.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordDTO {
	
	private int course_id;
	private String word_name;
	private DefinitionDTO[] defs;
}
