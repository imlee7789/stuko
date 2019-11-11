package com.stuko.domain;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SearchCriteria extends Criteria{

	private String searchType;
	private String keyword;
	private int pageStart;
	private int length;
	private int course_id;
}
