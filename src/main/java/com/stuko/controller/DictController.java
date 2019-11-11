package com.stuko.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stuko.domain.DefinitionDTO;
import com.stuko.domain.WordDTO;
import com.stuko.service.DictService;
import com.stuko.util.restapi.RestURIHelper;


@Controller
//RequestMapping
//1. /stuko/*/dict 에서 '*'은 구분자'/'를 포함하지 않는다.
@RequestMapping("/stuko/*/dict")
public class DictController {
	
	// bean으로 등록하는 방법은?
	RestURIHelper restHelper = new RestURIHelper();

	@Inject
	DictService dictService;
	
	@ResponseBody
	@GetMapping("/*")
	public WordDTO getDefs(
		HttpServletRequest request, 
		HttpServletResponse resp) {
		
		String uri = request.getRequestURI();
		String courseId = restHelper.getCourseId(uri);
		String word = restHelper.getWord(uri);
		WordDTO dto = new WordDTO();
		
		try {
			dto = dictService.readWord(courseId, word);
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		
		return dto;
	}

	@ResponseBody
	@DeleteMapping("/*")
	public void deleteDef(
			HttpServletRequest request,
			HttpServletResponse resp,
			DefinitionDTO defdto
		) {

		String uri = request.getRequestURI();
		int defId = restHelper.getDefId(uri);

		try {
			dictService.deleteDef(defId, defdto);
			
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
	}

	@ResponseBody
	@PutMapping("/*")
	public void replaceDef(
			HttpServletRequest request,
			HttpServletResponse resp,
			DefinitionDTO defdto
		) {
		
		String uri = request.getRequestURI();
		int defId = restHelper.getDefId(uri);
		
		try {
			dictService.replaceDef(defId, defdto);
			
		} catch (Exception e) {
			// Exception 종류를 나눠야한다.
			// ex, login, null, ......
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			
		}
		
	}

	@ResponseBody
	@PostMapping
	public void postDef(
			HttpServletRequest request,
			HttpServletResponse resp,
			DefinitionDTO defdto
		) {
		
		String uri = request.getRequestURI();
		String courseId = restHelper.getCourseId(uri);
		String word_name = restHelper.getWord(uri);

		try {
			dictService.createNewDef(courseId, word_name, defdto);
			resp.setStatus(HttpServletResponse.SC_CREATED);
			
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			
		}
		
	}

}
