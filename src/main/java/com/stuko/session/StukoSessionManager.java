package com.stuko.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import lombok.extern.slf4j.Slf4j;

/*
 * Date 		Name 	Description
 * 2019.09.27	hsl		처음 만든다.
 * 2019.09.30	hsl		HttpSession에서 courseId, nickName를 관리한다.
 * 						관리된 정보를 WebSocket.open() 시 가져다 쓴다.
 * 
 * How to use
 * 1. Add config to @ServerEndPoint
 * 		@ServerEndpoint(value = "/chatws1", configurator=ServletAwareConfig.class)
*/ 
@Slf4j
public class StukoSessionManager {

	/***** Singleton *****/
	
	private static StukoSessionManager instance = new StukoSessionManager();
	
	// String key = HttpSession.getId();
	private Map<String, StukoSession> sessions = new HashMap<>();
	
	private StukoSessionManager() {
	}
	
	public static StukoSessionManager getInstance() {
		return instance;
	}


	/***** Util *****/
	public String getHttpSessionId(EndpointConfig config) {
		return config.getUserProperties().get("httpSessionId").toString();
	}
	
	
	/***** Session *****/

	/* how to use
	 * 	1. get sessions
	 * 		Collection<StukoSession> sessions = ssm.getSessions();
	 * 
	 * 	2. enhanced for
	 * 		for ( StukoSession session : sessions ) {
	 * 			;;
	 * 		}
	*/
	public Collection<StukoSession> getSessions() {
		return sessions.values();
	}
	
	// 2019.09.30 lhs : 직접 액세스할 일이 없다.
	private void put(String HttpSessionId, StukoSession ss) {
		sessions.put(HttpSessionId, ss);
	}
	
	public void put(Session session, EndpointConfig config) {
		log.info("StukoSessionManager::put() invoked.");
		
		HttpSession httpSession = (HttpSession) config.getUserProperties().get("HttpSession");
		
		String httpSessionId = httpSession.getId();
		String courseId = httpSession.getAttribute("courseId").toString();
		String nickName = httpSession.getAttribute("nickName").toString();
		
		log.info("\t + [Enter][ httpSessionId : " + httpSessionId + 
				", courseId : " + courseId + 
				", nickName : " + nickName + "]");
		
		StukoSession ss = new StukoSession(session, courseId, nickName);
		this.put(httpSessionId, ss);
		
	}
	
	public void remove(String httpSessionId) {
		log.info("StukoSessionManager::remove() invoked.");
		String courseId = this.getCourseId(httpSessionId);
		String nickName = this.getNickName(httpSessionId);
		log.info("\t + [Leave][ httpSessionId : " + httpSessionId + 
				", courseId : " + courseId + 
				", nickName : " + nickName + "]");
		sessions.remove(httpSessionId);
	}
	
	public void remove(Session session) {
		String HttpSessionId = this.getHttpSessionId(session);
		this.remove(HttpSessionId);
	}
	
	public String getCourseId(String HttpSessionId) {
		return sessions.get(HttpSessionId).getCourseId();
	}
	
	public String getCourseId(Session session) {
		String HttpSessionId = this.getHttpSessionId(session);
		return sessions.get(HttpSessionId).getCourseId();
	}
	
	public String getNickName(String HttpSessionId) {
		String nickName = sessions.get(HttpSessionId).getNickName();
		
		return nickName;
	}
	
	public String getNickName(Session session) {
		String HttpSessionId = this.getHttpSessionId(session);
		String nickName = this.getNickName(HttpSessionId);
		
		return nickName;
	}
	
	public StukoSession getStukoSession(String httpSessionId) {
		return sessions.get(httpSessionId);
	}
	
	public String getHttpSessionId(Session session) {
		String httpSessionId = "";
		Set<String> keySet = sessions.keySet();
		
		for(String key : keySet) {
			if (sessions.get(key).getSession() == session) {
				httpSessionId = key;
				break;
			}
		}
		
		return httpSessionId;
	}
	
	public void changeNickName(Session session, String nickName) {
		String httpSessionId = this.getHttpSessionId(session);
		StukoSession ss = this.getStukoSession(httpSessionId);
		ss.setNickName(nickName);
	}
	
	public int size() {
		return this.sessions.size();
	}
	
}

/*
 * 1. StukoSessionManager
 * 		접속 중인 세션들을 관리해야한다.
 * 	
 * 		1-1. 별도 목록 관리가 필요한 이유 ?
 * 			WebSocketSession은 subscriber(구독자)에게 새로운 데이터(채팅/타임라인)이 추가됐을 때 전달해줘야한다.(server to client)
 * 			이 때 별도의 구독자 목록(List<session>)이 있어야 전달해줄 수 있다.
 * 
 * 		1-2. HttpSession과 WebSocketSession의 데이터를 같이 관리해야 하는 이유
 * 			- 중복 데이터 관리를 피하기 위함.
 * 			- timeline의 신규 글이 쓰여질 때
 * 			  request는 http(post)로 들어온다.
 * 			    이 때 WebSocket 방식으로 모든 접속 WebSocketSession에 글을 보내줘야한다.
 * 			    이 경우 request는 WebSocketSessionId를 가져오지 못한다.
 * 			    그러므로 HttpSessionId와 WebScoketSessionId 두가지를 같이 관리해야한다.
 * 
 * 		1-3. Session 유지 (2019.09.30)
 * 			- 새로 고침 혹은 새 창을 띄웠을 때 같은 유저여야한다.
 * 			- browser의 jsessionid으로 동일성을 확인한다.
 * 			  (어디에서?????)
 * 			
 * 
 * 
 * 2. StukoSession
 * 		- http.sessionId (key)
 * 		- session	: 구독자에게 데이터를 보내려면 session을 갖고 있어야 한다.
 * 		- courseId	: 강좌별로 통신을 나눠주기 위함.
 * 		- nickname	: 채팅 및 게시판댓글에서 사용한다.
 * 		
 * 	
 * 
 * 3. StukoSessionManager
 * 
 *		********** WebSocket관련 유틸 메소드 **********
 *		getHttpSessionId(EndpointConfig config)
 *
 *
 *
 *
 *		********** Session목록 처리 메소드 **********
 * 
 * 		put(String HttpSessionId, StukoSession stukoSession)
 * 
 * 		put(String HttpSessionId, int courseId)
 * 
 * 
 * 		remove(String HttpSessionId)
 * 
 * 		remove(Session session)
 * 
 * 
 * 		getCourseId(String HttpSessionId)
 * 
 * 		getCourseId(Session session)	>> iteration 필요
 * 
 * 		getNickName(String HttpSessionId)
 * 
 * 		getNickName(Session session)	>> iteration 필요
 *
 * 		getSession(String HttpSessionId)
 * 
 * 		getHttpSessionId(Session session)	>> iteration 필요
 * 
 * 
 */













