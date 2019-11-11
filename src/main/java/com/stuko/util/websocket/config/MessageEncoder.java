package com.stuko.util.websocket.config;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Converts the message class to a string.
 */
public class MessageEncoder implements Encoder.Text<Message> {

	// 브라우저로 보낼 Json 문자열을 만들어주는 메소드.
	// Json Object를 문자열로 만들어준다.
	@Override
	public String encode(Message message) throws EncodeException, NullPointerException {

		JsonObjectBuilder joBuildr = Json.createObjectBuilder().add("reqType", message.getReqType()); // not null
		
		// TIMELINE
		if (message.getReqType() == Message.REQ_TIMELINE) {

			JsonObject jsonObject = 
					Json.createObjectBuilder().
						add("reqType", message.getReqType()).
						add("id", message.getId()).
						add("user_id", message.getUser_id()).
						add("content", message.getContent()).
						add("rcmd_cnt", message.getRcmd_cnt()).
						add("insert_ts", message.getInsert_ts().toString()).
						add("comment_cnt", message.getComment_cnt()).
						build();
			return jsonObject.toString();
			
			
		} else {
		// CHATTING

			try {
				joBuildr.add("nickName", message.getNickName());

			} catch (NullPointerException e) {
				if (message.getReqType() == Message.REQ_NICK && message.getReqType() == Message.REQ_CHAT) {
					throw new NullPointerException();
				}
			}

			try {
				joBuildr.add("message", message.getMessage());
			} catch (NullPointerException e) {
				if (message.getReqType() == Message.REQ_CHAT) {
					throw new NullPointerException();
				}
			}

			try {
				joBuildr.add("count", message.getCount());
			} catch (NullPointerException e) {
				if (message.getReqType() == Message.REQ_COUNT) {
					throw new NullPointerException();
				}
			}

			JsonObject jsonObject = joBuildr.build();

			return jsonObject.toString();
		}
		

	}

	@Override
	public void init(EndpointConfig ec) {
		System.out.println("MessageEncoder - init method called");
	}

	@Override
	public void destroy() {
		System.out.println("MessageEncoder - destroy method called");
	}

}