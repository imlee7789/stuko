package com.stuko.util.websocket.config;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {

	// decode()
	// 브라우저로부터 받은 Json 문자열을
	// Json Object로 변환하는 메소드
	@Override
	public Message decode(String jsonMessage) throws DecodeException, NullPointerException {

		System.out.println("MessageDecoder::decode()");
		System.out.println("\t+ jsonMessage : " + jsonMessage);

		JsonObject jsonObject = Json.createReader(new StringReader(jsonMessage)).readObject();

		Message message = new Message();

		int reqType = jsonObject.getInt("reqType");
		if (reqType < Message.REQ_NICK || reqType > Message.REQ_COUNT) {
			// Args 뭐가 필요하지?
			throw new DecodeException("", "");
		}

		// reqtype은 필수이므로 상위로 익셉션을 던져야한다.
		message.setReqType(reqType);

		try {
			message.setCount(jsonObject.getInt("count"));
		} catch (Exception e) {
			//count가 없을수도있다.
		}
		try {
			message.setNickName(jsonObject.getString("nickName"));
		} catch (Exception e) {
			//count가 없을수도있다.
		}
		try {
			message.setMessage(jsonObject.getString("message"));
		} catch (Exception e) {
			//count가 없을수도있다.
		}

		return message;

	}

	@Override
	public boolean willDecode(String jsonMessage) {
		System.out.println("MessageDecoder::willDecode()");
		try {
			// Check if incoming message is valid JSON

			Json.createReader(new StringReader(jsonMessage)).readObject();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void init(EndpointConfig ec) {
		System.out.println("MessageDecoder -init method called");
	}

	@Override
	public void destroy() {
		System.out.println("MessageDecoder - destroy method called");
	}

}