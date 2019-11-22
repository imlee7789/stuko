package com.stuko.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.stuko.domain.uriCutter;
import com.stuko.service.NickName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StukoSessionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler
		) throws Exception {
		
		uriCutter uc = new uriCutter();
		
		log.info("StukoSessionInterceptor::preHandle()");

		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("nickName") == null || session.getAttribute("courseId") == null) {
			System.out.println("nickName is null");
			
			response.sendRedirect("/");
			
			return false;
//			String nickName = NickName.randomNick();
//			
//			String courseId = Integer.toString(uc.getCourseIdInIct(request));
//
//			session.setAttribute("nickName", nickName);
//			session.setAttribute("courseId", courseId);
		}
		
		String[] uris = request.getRequestURI().split("/");
		
		System.out.println("###############"+uris[2]);
		System.out.println(session.getAttribute("courseId"));
		if(!session.getAttribute("courseId").toString().equals(uris[2])) {

			response.sendRedirect("/");
			
			return false;
		}

		return true;
	}

}
