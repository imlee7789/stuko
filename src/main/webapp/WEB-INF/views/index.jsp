<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.stuko.domain.CourseVO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>

	<meta charset="UTF-8">
	<title>Stuko</title>
	
	<link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon">
	
	<link href="https://fonts.googleapis.com/css?family=Jua&display=swap"
		rel="stylesheet">
	<link rel="stylesheet" type="text/css"
		href="../../resources/css_new/index.css">
	
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery-migrate/3.1.0/jquery-migrate.min.js"></script>
	
	<script>
		$(function() {
	
			$('.course-info').click(function() {
	
				$('.course-info').removeClass('course-select');
				$(this).addClass('course-select');
			}); //course-info
	
	
			$('.button-box').click(function() {
	
				$('.pw-modal').css("display", "block");
	
			}); //button-box
	
			$('.pw-modal-submit>button').click(function() {
	
				console.log($(".course-select"));
	
				var courseId = $(".course-select").data("id");
	
				console.log("course.id : ", courseId);
	
				var conn_pw = $('#conn_pw').val();
	
				console.log("conn_pw : ", conn_pw);
	
				var data = {"conn_pw" : conn_pw};
	
				$.ajax({
					url : "/login/" + courseId,
					type : "post",
					data : data,
					dataType: "json",
					success : function(data) {
						console.log("data : ", data);
	
						location.replace("/stuko/"+courseId);
					},
					error: function(data){
						console.log(data);
						alert("비밀번호를 잘못입력하셨습니다.");
					}
				});
			});
	
			$('#cancel').click(function() {
	
				$('.pw-modal').hide();
	
			});
	
		}); //$
	</script>
</head>
<body>
	<div class="opacity-box"></div>
	<div class="wrapper">
		<div class="contents">

			<div class="title">
				<img src="../../resources/images/img_logo.png"> Stupid
				Koreans'secret lecture note
			</div>
			<div class="article">

				<div class="description">
					강의를 들으면서 수업에 방해가 될까봐,<br> 혹시 틀리면 부끄러울까봐<br> 질문을 하지 못하신 적이
					있나요?<br> <br> Stuko는 수업 중 질문을 하지 못하는<br> 수줍은 한국인들을
					위한 웹사이트입니다.<br> <br> 각 강좌마다 게시판 & 채팅 방을 통해<br> 익명으로
					질문 및 관련 정보를 포스팅 할 수 있습니다.<br> 또한 용어사전이 제공되어<br> 강좌 내 중요
					용어들을 포스팅 할 수 있습니다.:)
				</div>
				<div class="course">
					<div class="subtitle">Course</div>
					<div class="course-list">
						<ul style="width:${195*(fn:length(cvolist))+6}px">

							<form:form action="/stuko1" method="get" class="form-click">

								<c:forEach items="${cvolist}" var="cvo" varStatus="status">

									<li class="course-info" data-id="${cvo.id}"><c:set
											var="count" value="${status.count}" /> <c:if
											test="${count > 7}">
											<c:set var="count" value="${count%6}" />
										</c:if>

										<div class="random-img">
											<img src="../../resources/images/vector${count}.png">
										</div>
										<div class="course-name">${cvo.course_name}<br>
											<br>
											<span>${cvo.description}</span>
										</div></li>

								</c:forEach>

							</form:form>
						</ul>
					</div>
					<button class="button-box">Enter</button>
				</div>

			</div>

			<div class="pw-modal">
				<div class="pw-modal-text">비밀번호</div>
				<div class="pw-modal-input">
					<input type="password" id="conn_pw">
				</div>
				<div class="pw-modal-submit">
					<button type="submit" class="pw-modal-btn">확인</button>
					<input type="reset" value="취소" id="cancel">
				</div>
			</div>
		</div>
	</div>

	<div class="footer">Copyrightⓒ 2019 TeamFour.All rights reserved.
	</div>
</body>
</html>