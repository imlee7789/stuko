<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <title>STUKO</title>

    <!-- jQuery -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- jQuery-ui -->
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <!-- jQuery 하위 호환성을 위한 jquery-migrate.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-migrate/3.1.0/jquery-migrate.min.js"></script>

    <!-- FontAwsome icon -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css">

    <!-- Stuko -->
    <script src="../../resources/js_new/modal.js"></script>
    <script async src="../../resources/js_new/stukoWebSocket.js"></script>
    
    <link rel="stylesheet" href="../../resources/css_new/stuko.css">

</head>

<body>
    <nav class="stuko_nav">
        <header>
            <img class="stuko_logo" src="../../resources/images/img_logo_white.png">
        </header>
        <ul>
            <li id="write_btn" class="stuko_btn">
                <i class="fas fa-pencil-alt"></i>
                <span>글 쓰기</span>
            </li>
            <li id="dict_btn" class="stuko_btn">
                <i class="fas fa-book"></i>
                <span>용어 사전</span>
            </li>
        </ul>
        <footer>
            <div id="home_btn" class="stuko_btn">
                <i class="fas fa-home"></i>
                <span>홈페이지</span>
            </div>
        </footer>
    </nav>
    
    <div class="stuko_dict">
        <header class="stuko_dict_header">
            <h2>용어 사전</h2>
            <div class="stuko_dcit_search">
                <input type="text" placeholder="용어 검색">
                <button class="stuko_text_btn"><i class="fas fa-search"></i></button>
            </div>
            <button class="stuko_btn stuko_dict_regi_btn">용어 추가</button>
        </header>
        <div class="loading-bg">
            <div class="loading-container">
                <div class="loading"></div>
                <div id="loading-text">loading</div>
            </div>
        </div>
        <div class="stuko_word_list">
            <div class="stuko_word_init">
				검색하고 싶은 단어를 입력하세요
            </div>
            
         
			<!-- <div class="stuko_word" style="display: none;">
				<div class="stuko_word_time">
					<time> 2019/10/14 12:34 </time>
					<i class="stuko_text_btn far fa-thumbs-up"><span> 44</span></i> 
					<i class="stuko_text_btn far fa-trash-alt righter"></i>
					<i class="stuko_text_btn modal_bulletin_update_btn far fa-edit righter"></i>
				</div>
				<div class="word_desc">
					<span class="stuko_dict_link">썬마이크로시스템즈(썬)</span>에서 최초 발표한 언어이다.
					오우크(Oak)언어에서부터 시작해서 인터넷 프로그래밍 언어로 발전하면서, 자바라는 이름으로 변경되었다.
				</div>
				<ul class="word_ref_list">
					<li class="word_ref">
						<div class="word_ref_desc">이것이 자바다 yes24</div>
						<a class="word_ref_url" href="#"> https://www.yes24.com?name=이것이 자바다 </a>
					</li>
					<li class="word_ref">
						<div class="word_ref_desc">생활코딩 자바</div>
						<a class="word_ref_url" href="#"> https://www.opentutorials.org/course/1223 </a>
					</li>
				</ul>
			</div> -->
			
		</div>
    </div>

    <div class="stuko_board">
        <div id="stuko_hottopic">
            <h2>
                <i class="fab fa-hotjar"></i>
                <span>Hot Topic</span>
            </h2>
            <!-- <div class="stuko_article">
                <h3>
                    <span>Git 사용법 설명 부탁드립니다.</span>
                    <i class="far fa-thumbs-up"><span>44</span></i>
                </h3>
                <div class="article_content">
                    현재 프로젝트를 진행하는데 Git을 사용할 줄 안다면 매우 도움이 될 것 같습니다. Git의 기본 개념과 구성 방식 그리고 사용방법을 배우고 싶습니다.
                </div>
                <div class="article_footer">
                    <time>
                        2019/10/14 12:34
                    </time>
                    <div>
                        댓글
                        <span>23</span>
                    </div>
                </div>
            </div> -->
        </div>
        <div class="stuko_timeline_search">
            <input type="text" placeholder="게시글 검색">
            <button class="stuko_text_btn"><i class="fas fa-search"></i></button>
        </div>
        <div class="stuko_timeline">
            <div class="stuko_article_list">
            </div>
        </div>
    </div>
    
    <div class="stuko_chatting">
        <header>
            <i class="fas fa-circle"></i>
            <span>채팅창</span>
            <span id="stuko_chatter_cnt">${numOfPeople}</span>명
            <button id="stuko_nick_change" class="stuko_text_btn">닉네임 변경하기</button>
        </header>
        <div id="stuko_chatting_view"></div>
        <form>
            <textarea id="stuko_chatting_input"
                placeholder="타인을 모욕 / 비방하는 행위는 법적으로 처벌 받을 수 있습니다."></textarea>
            <button id="stuko_chatting_sender" 
                    class="stuko_btn" 
                    type="button">보 내 기
            </button>
        </form>
    </div>
</body>

</html>