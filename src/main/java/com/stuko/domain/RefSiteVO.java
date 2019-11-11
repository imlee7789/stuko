package com.stuko.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Class	RefSiteVO
 * 
 * Name		Desc	Date			
 * hslee	made	2019.09.25	
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefSiteVO {

	private int id;
	private int def_id;
	private String url;
	private String description;
	
}
