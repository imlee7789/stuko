package com.stuko.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.stuko.service.NickName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StukoSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.info("StukoSessionListener::sessionCreated() invoked");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.info("StukoSessionListener::sessionDestroyed() invoked");
		
		HttpSession hSession = se.getSession();
		
		if(hSession.getAttribute("nickName") != null) {
			String nickName = hSession.getAttribute("nickName").toString();
			
			NickName.putNickName(nickName);
		}

	}

}