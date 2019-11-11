package com.stuko.util.websocket.config;
 
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import lombok.extern.slf4j.Slf4j;
      
@Slf4j
public class ServletAwareConfig extends ServerEndpointConfig.Configurator {
 
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
    	log.info("ServletAwareConfig::modifyHandshake()");
    	
        HttpSession session = (HttpSession) request.getHttpSession();
//        ServletContext ctx = session.getServletContext();

        // 2019.09.30 lhs : intercepter에서 session영역의 attribute에 coureseId와 nickName을 갖고있다.
        config.getUserProperties().put("HttpSession", session);
//      config.getUserProperties().put("httpSessionId", session.getId());
//      config.getUserProperties().put(ServletContext.class.getName(), ctx);
    }
}


