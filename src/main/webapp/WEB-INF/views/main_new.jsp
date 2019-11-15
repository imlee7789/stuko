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
    <link rel="stylesheet" href="../../resources/css_new/modal.css">

    <script></script>

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
            <li class="stuko_btn">
                <i class="fas fa-book"></i>
                <span>용어 사전</span>
            </li>
        </ul>
        <footer>
            <div class="stuko_btn">
                <i class="fas fa-home"></i>
                <span>홈페이지</span>
            </div>
        </footer>
    </nav>

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