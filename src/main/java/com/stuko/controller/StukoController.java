package com.stuko.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stuko.domain.BoardDTO;
import com.stuko.domain.BoardVO;
import com.stuko.domain.CommentDTO;
import com.stuko.domain.CommentVO;
import com.stuko.domain.Criteria;
import com.stuko.domain.HotTopic;
import com.stuko.domain.SearchCriteria;
import com.stuko.domain.uriCutter;
import com.stuko.service.BoardService;
import com.stuko.service.CommentService;
import com.stuko.service.NickName;
import com.stuko.session.StukoSession;
import com.stuko.session.StukoSessionManager;
import com.stuko.util.websocket.config.Message;
import com.stuko.util.websocket.config.MessageDecoder;
import com.stuko.util.websocket.config.MessageEncoder;
import com.stuko.util.websocket.config.ServletAwareConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/stuko")
@ServerEndpoint(
		// 과제1. value에 와일드카드 되는지 확인할 것 >> 안됨 (stuko/* 안됨)
		value = "/stuko",
		// Session, HttpRequestSession을 합치기 위함.
		configurator = ServletAwareConfig.class,
		// sendMsg,onMessage에서 Json Object를 사용하기 위해 encoders와 decoders를 사용함.
		encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class })
public class StukoController {

	@Inject
	private BoardService service;
	
	@Inject
	private CommentService cService;

	Calendar cal = new GregorianCalendar(Locale.KOREA);
	
	
	StukoSessionManager ssm = StukoSessionManager.getInstance();

	public StukoController() {
		System.out.println("웹소켓(서버) 객체생성");
	}


	/******************************************************
	 * 
	 * 	Main View
	 * 
	 *  Uri : /stuko/3
	 * 
	******************************************************/
	@GetMapping("/*")
	public String showMainPage(Model model) {
		log.info("ChatController::showMainPage() invoked.");
		model.addAttribute("numOfPeople", ssm.size());
		return "main_new";
	}
	

	/******************************************************
	 * 
	 * 	Bulletin Controller
	 * 
	******************************************************/
	
	// 0. timeline 초기화
	@RequestMapping(value = "/*/timeline", method = RequestMethod.GET)
	@ResponseBody
	public List<BoardVO> listGET(
			Criteria cri, 
			HttpServletRequest request, 
			uriCutter cutter
		) throws Exception {

		log.info("list GET --------------------");

		int courseid = cutter.getCourseId(request);

		int length = 10;

		cri.setLength(length);
		cri.setCourse_id(courseid);

		List<BoardVO> list = service.readAll(cri);

		return list;
	}
	
	// 1. 게시글 생성, 빈칸은 뷰에서 처리해주기.
	@RequestMapping(value = "/*/timeline", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BoardDTO> registPOST(BoardDTO dto, HttpServletRequest request, uriCutter cutter)
			throws Exception {

		log.info("regist post--------------------");
		log.info(dto.toString());

		int courseid = cutter.getCourseId(request);

		dto.setCourse_id(courseid);

		log.info(">>>>> courseid : " + courseid);

		service.regist(dto);

		ResponseEntity<BoardDTO> responseEntity = 
				new ResponseEntity<BoardDTO>(dto, HttpStatus.OK);
		
		Message message = new Message();

		BoardVO vo = service.readLastOne(courseid);

		message.setReqType(Message.REQ_TIMELINE);
		message.setId(vo.getId());
		message.setUser_id(vo.getUser_id());
		message.setContent(vo.getContent());
		message.setRcmd_cnt(vo.getRcmd_cnt());
		message.setInsert_ts(vo.getInsert_ts());
		message.setComment_cnt(vo.getComment_cnt());

		Collection<StukoSession> sessions = ssm.getSessions();
		for (StukoSession ss : sessions) {
			try {
				int cId = Integer.parseInt(ss.getCourseId());
				if (cId == courseid) {
					ss.getSession().getBasicRemote().sendObject(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return responseEntity;
	}
	
	// 2. 게시글 삭제
	@RequestMapping(value = "/*/timeline/*", method = RequestMethod.DELETE)
	@ResponseBody
	public void remove(@RequestParam("user_pw") String user_pw, Model model, Criteria cri,
			HttpServletRequest request, 
			HttpServletResponse response,
			uriCutter cutter) throws Exception {

		log.info("BoardController :: remove invoked.");
		
		System.out.println();

		int id = cutter.getBoardId(request);

		boolean result = service.check_pw(id, user_pw);
		if (result) {

			service.remove(id);
			response.setStatus(HttpServletResponse.SC_OK);
			
		} else {

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	// 3. 게시글 자세히 보기
	@RequestMapping(value = "/*/timeline/*", method = RequestMethod.GET)
	@ResponseBody
	public BoardVO readOne(HttpServletRequest request, uriCutter cutter) throws Exception {

		int boardId = cutter.getBoardId(request);

		BoardVO vo = service.readOne(boardId);

		log.info(">>>>>>> id : " + boardId);

		return vo;
	}

	// 3-1. 게시글 댓글 보기
	@RequestMapping(value = "/*/timeline/*/comment", method = RequestMethod.GET)
	@ResponseBody
	public List<CommentVO> CommentRead(HttpServletRequest request, uriCutter cutter) throws Exception {

		int comUseBoardId = cutter.getcomUseBoardId(request);

		List<CommentVO> vo = cService.commentRead(comUseBoardId);

		log.info(">>>>>>> id : " + comUseBoardId);

		return vo;
	}

	// 4. 게시글 수정
	@RequestMapping(value = "/*/timeline/*", method = RequestMethod.PUT)
	public ResponseEntity<BoardDTO> modifyPUT(BoardDTO dto, HttpServletRequest request, uriCutter cutter)
			throws Exception {

		log.info("BoardController :: modifyPUT invoked.");

		int boardId = cutter.getBoardId(request);

		dto.setId(boardId);
		service.modify(dto);

		ResponseEntity<BoardDTO> responseEntity = new ResponseEntity<BoardDTO>(dto, HttpStatus.OK);

		return responseEntity;
	}

	// 4-1. 게시글 수정시 pw체크
	@RequestMapping(value = "/*/timeline/*/password", method = RequestMethod.POST)
	public void passwordPOST(BoardDTO dto, HttpServletResponse response, HttpServletRequest request, uriCutter cutter)
			throws Exception {

		String user_pw = dto.getUser_pw();
		int boardId = cutter.getcomUseBoardId(request);

		boolean result = service.check_pw(boardId, user_pw);
		if (result) {

			response.setStatus(HttpServletResponse.SC_OK);

		} else {

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		}
	}

	// 5. 게시글 추천
	@RequestMapping(value = "/*/timeline/*", method = RequestMethod.PATCH)
	public void rcmd_cnt(BoardDTO dto, HttpServletResponse response, HttpServletRequest request, uriCutter cutter)
			throws Exception {

		log.info("BoardController :: rcmd_cnt invoked.");

		int boardId = cutter.getBoardId(request);

		service.rcmd_up(boardId);

		response.setStatus(HttpServletResponse.SC_OK);
	}

	// 6. 검색
	@RequestMapping(value = "/*/timeline/search", method = RequestMethod.GET)
	@ResponseBody
	public List<BoardVO> listPage(SearchCriteria cri, HttpServletRequest request, uriCutter cutter) throws Exception {

		log.info("SearchBoardController :: listPage() invoked.");
		log.info(cri.toString());

		int length = 10;

		int course_id = cutter.getSerUseCourseId(request);

		cri.setSearchType("all");
		cri.setLength(length);
		cri.setCourse_id(course_id);

		List<BoardVO> list = service.readSearch(cri);

		return list;
	}

	// 7. 댓글생성
	@RequestMapping(value = "/*/timeline/*", method = RequestMethod.POST)
	@ResponseBody
	public CommentVO register(CommentDTO dto, HttpServletRequest request, uriCutter cutter)
			throws Exception {

		int boardid = cutter.getBoardId(request);

		HttpSession session = (HttpSession) request.getSession();

		log.info(">>>> session" + session);

		String httpSessionId = session.getId();

		log.info(">>>> httpSessionId" + httpSessionId);

		String user_id = ssm.getNickName(httpSessionId);
		//String user_id = dto.getUser_id();

		log.info(">>>> user_id" + user_id);

		dto.setUser_id(user_id);

		dto.setBulletin_id(boardid);
		cService.commentModify(dto);
		
		CommentVO vo = cService.readLastOne(boardid);
		
		return vo;
	}
	
	
	// Hot Topic
	@RequestMapping(value = "/*/timeline/hottopic", method = RequestMethod.GET)
	@ResponseBody
	public BoardVO readHotTopic(HotTopic hotTopic, HttpServletRequest request, uriCutter cutter) throws Exception {

		int course_id = cutter.getSerUseCourseId(request);

		log.info("course_id>>>>>>>>>>>>> : " + course_id);

		Date insert_ts = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String insertts = format.format(insert_ts);

		hotTopic.setCourse_id(course_id);
		hotTopic.setInsert_ts(insertts);

		log.info("insert_ts>>>>>>>>>>>>> : " + insert_ts);

		BoardVO vo = service.readHotTopic(hotTopic);

		return vo;
	}
	
	
	
	/******************************************************
	 * 
	 * 	WebSocket Controller
	 * 
	******************************************************/
	

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) throws IOException, EncodeException {
		log.info("ChatController::onOpen() inovked.");

		ssm.put(session, config);

		// 2019.10.10 채팅방 입장/퇴장 안내를 할 것인가??
		// basic.sendText("채팅방에 입장하셨습니다.");

		// 1. 사람수 변경 시 공지
		this.numInChatChanged();
	}
	
	private void numInChatChanged() {
		Message message = new Message();
		message.setReqType(Message.REQ_COUNT);
		message.setCount(ssm.size());

		Collection<StukoSession> sessions = ssm.getSessions();
		for (StukoSession ss : sessions) {
			try {
				ss.getSession().getBasicRemote().sendObject(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// message = type +"_"+ msg;
	// message = "chat_안녕하세요";
	private void sendMessageToAllSession(Session self, Message message) {

		log.info("ChatController::sendMessageToAllSession() invoked");
		
		// 1. 보내줄 데이터 Message 객체 생성
		String senderNickName = ssm.getNickName(self);
		String msg = message.getMessage();
		
		// sender courseId
		String senderCouseId = ssm.getCourseId(self);
		
		Message sendMessage = new Message();
		sendMessage.setReqType(Message.REQ_CHAT);
		sendMessage.setNickName(senderNickName);
		sendMessage.setMessage(msg);

		// ########메시지 파싱##########
		try {

			Collection<StukoSession> sessions = ssm.getSessions();
			
			for (StukoSession stukoSession : sessions) {
				Session session = stukoSession.getSession();
				// subscriber courseId
				String subCourseId = ssm.getCourseId(session);
				if(senderCouseId.equals(subCourseId)) {
					session.getBasicRemote().sendObject(sendMessage);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@OnMessage
	public void onMessage(Session session, Message message) {
		log.info("ChatController::onMessage() invoked.");
		
		// 1. reqType 요청 확인 ( chat, nick )
		int reqType = message.getReqType();
		
		if(reqType == Message.REQ_CHAT) {
			sendMessageToAllSession(session, message);
			
		} else if(reqType == Message.REQ_NICK) {
			
			String nickName = ssm.getNickName(session);
			log.info("현재 닉네임: " + nickName);
			
			nickName = NickName.changeNickName(nickName);
			log.info("바뀐 닉네임:  " + nickName);
			
			// 2019.10.10 ssm에서 nickChange를 해줘야한다.
			// 2019.10.20 ssm에서 닉을 변경하고 httpSession의 nick도 변경해야한다.
			ssm.changeNickName(session, nickName);
			
		}

	}

	@OnClose
	public void onClose(Session session) {

		// 2019.10.10 NickName제거는 HttpSession.destroy()에서 해야한다. 
//		String nickName = ssm.getNickName(session);
//		NickName.putNickName(nickName);
		ssm.remove(session);

		// 1. 사람수 변경 시 공지
		this.numInChatChanged();
		
	}

	@OnError
	public void onError(Throwable e, Session session) {
		// 2019.10.10 에러일 때도 사람 수 변경 공지를 해야하는가?
		
		log.info("error invoked.");
		e.printStackTrace();
	}

}
