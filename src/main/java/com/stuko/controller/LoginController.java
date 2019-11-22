package com.stuko.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stuko.domain.CourseDTO;
import com.stuko.domain.CourseVO;
import com.stuko.service.CourseService;
import com.stuko.service.NickName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

	@Inject
	private CourseService courseService;
	
	@GetMapping("/")
	public String index(
			Model model,
			HttpServletRequest request 
			) throws Exception {
		
		List<CourseVO> cvolist = courseService.courseInfo();
		
		log.info(">>>>covlist : " + cvolist.toString());

		HttpSession session = request.getSession();

		session.setAttribute("nickName", null);
		session.setAttribute("courseId", null);
		
		int courseCount = cvolist.size();
		
		model.addAttribute("cvolist", cvolist);
		model.addAttribute("courseCount", courseCount);
		
		return "/index";
	
	} //index
	

	@PostMapping("/logout")
	@ResponseBody
	public CourseDTO logOut(
			HttpServletRequest request, 
			HttpServletResponse response 
		) {
		
		HttpSession session = request.getSession();
		
		String nickName = session.getAttribute("nickName").toString();
		
		NickName.putNickName(nickName);
		
		session.setAttribute("nickName", null);
		session.setAttribute("courseId", null);
		
		CourseDTO cdto = new CourseDTO();
		return cdto;
	}
	
	@PostMapping("/login/*")
	@ResponseBody
	public CourseDTO pwCheck(
			CourseDTO cdto,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model
		) {
		log.info("pwCheck() invoked.");

		String conn_pw = cdto.getConn_pw();

		String uri = request.getRequestURI();
		String[] separate = uri.split("/");
		int courseId = Integer.parseInt(separate[separate.length - 1]);
		
		log.info("\tpw : " + conn_pw);
		log.info("\tcourseId : " + courseId);
		
		cdto.setId(courseId);
		
		boolean isSuccess = courseService.pwCheck(cdto);
		
		if(isSuccess) {
			HttpSession session = request.getSession(true);
			
			if (session.getAttribute("nickName") == null || session.getAttribute("courseId") == null) {
				String nickName = NickName.randomNick();
				
				session.setAttribute("nickName", nickName);
				session.setAttribute("courseId", courseId);
			}
			response.setStatus(HttpServletResponse.SC_OK);
			
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		return cdto;
	} //pwCheck
	
} //end class
