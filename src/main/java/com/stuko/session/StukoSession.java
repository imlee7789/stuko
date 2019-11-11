package com.stuko.session;

import javax.websocket.Session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Date 		Name 	Description
 * 2019.09.26	hsl		처음 만듦
*/ 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StukoSession {

	/*
	 * Date 		Name 	Description
	 * 2019.09.26	hsl		만약 Session id 이외 필요한 정보가 없다면
	 * 						session field는 삭제한다.
	*/ 
	// StukoSession은 Map<StukoSession>으로 StukoSessionManager에서 사용한다.
	
	Session session; // javax.websocket.Session;
	String courseId;
	String nickName;
	
}


// 필요한 정보
// sessionId
// channelId
// nickName





