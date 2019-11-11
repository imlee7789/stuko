package com.stuko.util.restapi;

import java.util.Arrays;

public class RestURIHelper {
	
	private String appName = "stuko";
	private String[] serviceName = {"dict"};
	
	public String getCourseId(String uri) {
		String[] splited = uri.split("/");
		String courseId = "";
		if (splited.length > 2 && splited[1].contentEquals(appName)) {
			courseId = splited[2];
		}
		
		return courseId;
	}
	
	// uri : /stuko/3/dict/자바
	public String getWord(String uri) {
		String[] splited = uri.split("/");
		String word = "";
		if (
				splited.length == 5 && 
				splited[1].equals(appName) &&
				Arrays.asList(serviceName).contains(splited[3])
		) {
			word = splited[4];
		}
		
		return word;
	}

	// uri : /stuko/3/dict/자바/5
	public int getDefId(String uri) 
		throws NumberFormatException {
		
		String[] splited = uri.split("/");
		int defId = 0;
		if (
				splited.length == 6 && 
				splited[1].equals(appName) &&
				Arrays.asList(serviceName).contains(splited[3])
		) {
			defId = Integer.parseInt(splited[5]);
		}
		
		return defId;
	}


	//	/stuko/3/dict/4
//	public boolean isUriCorrected(String uri) {
//		String[] matched = {
//				"",
//				"stuko", 		// app			fixed
//				"", 			// courseId		number
//				"service name", // dict			fixed
//				"", 			// word_name	String
//				"" 				// def_number	int
//		};
//		
//		return true;
//	}
}
