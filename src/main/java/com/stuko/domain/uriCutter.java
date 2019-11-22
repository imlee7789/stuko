package com.stuko.domain;

import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class uriCutter {

	int courseId;
	int boardId;
	int comUseBoardId;
	int serUseCourseId;

	public int getCourseId(HttpServletRequest request) {

		String uri = request.getRequestURI();
		String[] separate = uri.split("/");
		int courseId = Integer.parseInt(separate[separate.length - 2]);

		return courseId;
	}

	public int getCourseIdInIct(HttpServletRequest request) {

		String uri = request.getRequestURI();
		String[] separate = uri.split("/");
		int courseId = Integer.parseInt(separate[separate.length - 1]);

		return courseId;
	}
	
	public int getSerUseCourseId(HttpServletRequest request) {

		String uri = request.getRequestURI();
		String[] separate = uri.split("/");
		int serUseCourseId = Integer.parseInt(separate[separate.length - 3]);

		return serUseCourseId;
	}

	public int getBoardId(HttpServletRequest request) {

		String uri = request.getRequestURI();
		
		String[] separate = uri.split("/");
		
		int boardId = Integer.parseInt(separate[separate.length - 1]);

		return boardId;
	}
	
	public int getcomUseBoardId(HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String[] separate = uri.split("/");
		int comUseBoardId = Integer.parseInt(separate[separate.length - 2]);
		
		return comUseBoardId;
	}
}
