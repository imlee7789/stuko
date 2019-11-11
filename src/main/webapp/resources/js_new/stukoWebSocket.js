(function(){
    var serverEndPoint = "stuko";
    var _AjaxURI = window.location.pathname;
    var _WebSocketURL = "ws://" + window.location.host + "/" + serverEndPoint;
    
    var _ws = null;

    const _reqType = {
        CHAT_NICK_CHG	: 1,
        CHAT_NEW_MSG	: 2,
        CHAT_CNT_CHG	: 3,
        BOARD_TIMELINE	: 4
	};


    /****************************************
    *   Utils
    ****************************************/
    function pad(n, width) {
        n = n + '';
        return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
    }
    
    function convertEnter(text){
    	return text.split("\n").join("<br>");
    }

    function isExist(opt){
        if(typeof opt == "undefined" || opt == null) {
            return false;
        }
        return true;
    }
    
    function scrollSetTop(id){
        $(id).scrollTop(0);	
    }

    function scrollSetBottom(id){
        $(id).scrollTop($(id)[0].scrollHeight);
    }

    function isScrollTop(id){
    }

    function isScrollBottom(id){
        // if you looking at not a bottom,
        // scroll don't be moved
		var top = $(id).scrollTop();
		var scrollHeight = $(id)[0].scrollHeight;
		var height = $(id).height();
    	
		if ( scrollHeight - height - top < 50) {
			return true;
        }
        return false;
    }

    function appendChat(data){
    	console.log("appendChat() invoked.");

        if(
            !isExist(data.nickName) ||
            !isExist(data.message)
        ){
            console.log("Wrong Message received");
            return;
        }
		
		var today = new Date();
        var chatTime = 
                pad(today.getHours(), 2) + 
                ":" + 
                pad(today.getMinutes(), 2);
        
        var _chat = 
                $("<div></div>").
                addClass("chat");

        _chat.append(
                $("<div></div>").
                addClass("chat_nick").
                text(data.nickName));
    
        _chat.append(
                $("<div></div>").
                addClass("chat_msg").
                html(data.message));

        _chat.append(
                $("<div></div>").
                addClass("chat_time").
                text(chatTime));
        
        var isBottom = isScrollBottom("#stuko_chatting_view");
        
        $("#stuko_chatting_view").append(_chat);

        if(isBottom){
        	scrollSetBottom("#stuko_chatting_view");        	
        }
    }
    /////////////////////////////
    function _sendMessage(){
    	console.log("_sendMessage");
    	var messageArea = $("#stuko_chatting_input");
    	var message = messageArea.val();
    	
    	if(message == ""){
    		return;
    	}

		if(message[message.length-1] == '\n') {
			message[message.length-1] = '';
		}
		
		message = convertEnter(message);
		
		var data = 
				{reqType: 2,
				message: message};
		
		_ws.send(JSON.stringify(data));
		messageArea.val("");
		
		$("#stuko_chatting_sender").attr("disabled", true);
		
		setTimeout(function(){
			console.log("_sendMessage:: ready");
			$("#stuko_chatting_sender").attr("disabled", false);
			
		}, _chatDelay);
    }
    
    function _registerComment(content, id){
    	console.log("_registerComment() invoked.");
    	
    	if(content == ""){
    		return;
    	}

		if(content[content.length-1] == '\n') {
			content[content.length-1] = '';
		}
		
		var data = {
			content: convertEnter(content)
		}
		
		$.ajax({
			url: "/stuko/1/timeline/" + id,
			type: "post",
			data: data,
			dataType: "json",
			success: function(detail){
				
			},
			error:function(data){
				console.log("error : ", data);
			}			
		});
    };
    
    function _deleteArticle(id, detailModal){
		console.log("_deleteArticle() invoked.");
    	
    	var bodyHtml = 
        		$("<div></div>").
        		append(
    				$("<input></input>").
    				prop("type", "password").
    				prop("placeholder", "비밀번호를 입력하세요").
    				css({
    					display: "block",
    					padding: "20px",
    					width: "100%",
    					height: "100%",
    					outline: "none",
    					border: "solid 1px #cc816d"
					})
				);
    	
    	var _okCallback = function(){
    		
    		var user_pw = $(bodyHtml).find("input").val()+"";
    		
    		console.log("=====");
    		console.log(user_pw);
    		
    		var data = {
    				user_pw: user_pw
    		};
    		
        	$.ajax({
    			url: "/stuko/1/timeline/" + id + "?user_pw=" + user_pw,
    			type: "delete",
    			dataType: "json",
    			success: function(detail){
    				console.log("_deleteArticle() succeed.");

//    				var _okCallback = function(){
//    					var modals = $(".stuko_modal_content");
//    					for(var modal in modals){
//    						modal.close();
//    					}
//    				}
    				
	            	var opt = {
	            			bodyHtml: $("<div></div>").html("게시글 삭제 성공"),
//	            			okBtnCallback: _okCallback,
	            			headText: "Delete"
	            	};
	            	
	            	var confirm = new Modal(opt);
	            	
	            	confirm.create();
    				
    			},
    			error:function(data){
    				
//    				var _okCallback = function(){
//    					var modals = $(".stuko_modal_content");
//    					for(var modal in modals){
//    						modal.close();
//    					}
//    				}
    				console.log("error : " ,data);
    				
    				var text = "게시글 삭제 실패";
    				
    				if(data.status == 200) {
    					text = "게시글 삭제 성공"
    				}

    				console.log(text);
    				
	            	var opt = {
	            			bodyHtml: $("<div></div>").html(text),
//	            			okBtnCallback: _okCallback,
	            			headText: "Delete"
	            	};
	            	
	            	var confirm = new Modal(opt);
	            	
	            	confirm.create();
    			}
    		});
    	};
    	
    	var opt = {
    			bodyHtml: bodyHtml,
    			okBtnCallback: _okCallback,
    			headText: "Delete"
    	};
    	
    	var confirm = new Modal(opt);
    	
    	confirm.create();
    		
    }
    
    function appendComment(comment, commentList){

    	if(commentList == "undefined" || commentList == null){
    		commentList = $(".stuko_modal_comment_list");
    	}
        
        var date = new Date(comment.insert_ts);
        var time =
                date.getFullYear() + "/" + 
                (date.getMonth() + 1) + "/" + 
                date.getDate() + " " + 
                pad(date.getHours(), 2) + ":" + 
                pad(date.getMinutes(), 2);

                
        var cmt = 
                $("<div></div>").
                addClass("comment").
                append(
                    $("<div></div>").
                    append(
                        $("<span></span>").
                        addClass("comment_user").
                        text(comment.user_id)
                    ).
                    append(
                        $("<time></time>").
                        addClass("comment_time").
                        text(time)
                    ).
                    append(
                        $("<div></div>").
                        addClass("comment_content").
                        html(comment.content)
                    )
                );
        
        commentList.append(cmt);
    }

    // 2019.10.28 한번 articleRcmdUp가 작동한 id는
    // 가능한 길게 유지돼야 한다.
    // session destroy까지 막아야할 것으로 보인다.
    function articleRcmdUp(id){
    	$.ajax({
			url: "/stuko/1/timeline/" + id,
			type: "patch",
			dataType: "json",
			success: function(detail){
				console.log("articleRcmdUp() invoked.");

            	var bodyHtml = 
                		$("<div></div>").
                		text("추천하셨습니다.");
            	
            	var opt = {
            			bodyHtml: bodyHtml,
            			headText: "Recommend"
            	};
            	
            	var confirm = new Modal(opt);
            	
            	confirm.create();
            	
			},
			error:function(data){
				console.log("error : " ,data);
            	var bodyHtml = 
            		$("<div></div>").
            		text("추천하셨습니다.");
        	
	        	var opt = {
	        			bodyHtml: bodyHtml,
	        			headText: "Recommend"
	        	};
	        	
	        	var confirm = new Modal(opt);
	        	
	        	confirm.create();
			}
		});
    }
    
    /* ArticleModal - step 1 */
    function ArticleDetailModal(id){
    	
    	$.ajax({
			url: "/stuko/1/timeline/" + id,
			type: "get",
			dataType: "json",
			success: function(detail){
				
				ArticleDetialModalStep2(detail, id);
				
			},
			error:function(data){
				console.log("error : " ,data);
			}			
		});
    }

    /* ArticleModal - step 2 */
    function ArticleDetialModalStep2(detail, id){
    	
    	$.ajax({
			url: "/stuko/1/timeline/" + id + "/comment",
			type: "get",
			dataType: "json",
			success: function(respCommentList){
				
				RenderDetialModal(detail, respCommentList);
				
			},
			error:function(data){
				console.log("error : " ,data);
			}			
		});
    }

    /* ArticleModal - step 3 */
    function RenderDetialModal(detail, respCommentList){
    	console.log("RenderDetialModal() invoked.");
    	
    	/* Option */
        var option = {
            /* Modal Position */
            top: "5vh",
            left: "20vw",

            /* Modal Size */
            width: "60vw",
            height: "90vh",

            /* Color */
            bgColor: "#f5f5f5",

            /* Header options */
            headText: "Detail",

            /* Footer options */
            isFooter: false
        };
        
    	/* Modal Body */
        var bodyHtml =
        		$("<div></div>");

        /* Bullet */
        var bulletin =
        		$("<div></div>").
                addClass("stuko_modal_detail");

        var name =
            	$("<span></span>").
                addClass("modal_bulletin_name").
                text(detail.user_id);

        var rcmdBtn =
	            $("<button></button>").
	            addClass("stuko_text_btn").
	            css("backgroundColor", option.bgColor).
	            append(
	                $("<i></i>").
                    addClass("far fa-thumbs-up").
                    append(
                        $("<span></span>").
                        addClass("modal_bulletin_rcmd_btn").
                        text(detail.rcmd_cnt)
                    )
	            ).
	    		on("click", function(){
	    			console.log("asdf");
	            	// 2019.10.28 rcmdup을 1회로 막는다.
		        	articleRcmdUp(detail.id);
		        	$(rcmdBtn).prop("disabled", true);
		        });

        var updateBtn =
	            $("<div></div>").
	            append(
	                $("<i></i>").
	                addClass("stuko_text_btn").
	                addClass("modal_bulletin_update_btn").
	                addClass("far fa-edit")
	            );

        var deleteBtn =
	            $("<div></div>").
	            append(
	                $("<i></i>").
	                addClass("stuko_text_btn").
	                addClass("far fa-trash-alt")
	            ).
	            on("click", function(){
	
	            	var bodyHtml = 
	                		$("<div></div>").
	                		text("삭제하시겠습니까?");
	
	            	var _okCallback = function(){
	            		var data = $(".stuko_modal_comment_input").val();
	            		_deleteArticle(detail.id, detailModal);
	            	}
	            	
	            	var opt = {
	            			bodyHtml: bodyHtml,
	            			okBtnCallback: _okCallback,
	            			headText: "Delete"
	            	};
	            	
	            	var confirm = new Modal(opt);
	            	
	            	confirm.create();
	            });

        var bulletinContent =
            $("<div></div>").
                addClass("modal_bulletin_content").
                html(detail.content)

        bulletin.
            append(
                $("<h2></h2>").
                append(name).
                append(rcmdBtn)
            ).
            append(
                $("<h3></h3>").
                append($("<span>내용</span>")).
                append(deleteBtn).
                append(updateBtn)
            ).
            append(bulletinContent);

        /* Comment */
        var commentDiv =
                $("<div></div>").
                addClass("stuko_modal_comment");

        var commentLen = 0;
        
        if(typeof respCommentList != "undefined" && respCommentList != null){
        	commentLen = respCommentList.length;
        }
        
        var commentHeader =
                $("<h2></h2>").
                append(
                    $("<span></span>").
                    html("댓글(<span>" + commentLen + "</span>) 개")
                );
        
        var commentList = 
                $("<div></div>").
                addClass("stuko_modal_comment_list");

        
        for(var i=0; i<commentLen; i++){
            var comment = respCommentList[i];
            console.log(comment);
            
            appendComment(comment, commentList);
        }


        var commentFooter = 
                $("<form actoin='#'></form>").
                addClass("stuko_modal_comment_footer").
                append(
                    $("<textarea></textarea>").
                    addClass("stuko_modal_comment_input")
                ).
                append(
                    $("<input type='button'></input>").
                    addClass("stuko_btn").
                    addClass("stuko_modal_comment_regi_btn").
                    val("등록").
                    on("click", function(){
                    	var _thisBtn = $(this);
                    	
                    	_thisBtn.prop("disabled", true);
                    	
                    	var bodyHtml = 
	                    		$("<div></div>").
	                    		text("댓글을 다시겠습니까?");


                    	var _okCallback = function(){
                    		var data = $(".stuko_modal_comment_input").val();
                    		_registerComment(data, detail.id);
                    		
                    		_thisBtn.prop("disabled", false);        

                        	var bodyHtml = 
                            		$("<div></div>").
                            		text("댓글을 추가했습니다.");
                        	
                        	var opt = {
                        			bodyHtml: bodyHtml,
                        			headText: "Comment"
                        	};
                        	
                        	var confirm = new Modal(opt);
                        	
                        	confirm.create();
                    		
                    	}
                    	
                    	var _cancelCallback = function(){
                    		_thisBtn.prop("disabled", false);
                    		
                    	}
                    	
                    	var opt = {
                    			bodyHtml: bodyHtml,
                    			headText: "Comment",
                    			okBtnCallback: _okCallback,
                    			cancelCallback:_cancelCallback 
                    	};
                    	
                    	var confirm = new Modal(opt);
                    	
                    	confirm.create();
                    	
                    })
                );

        commentDiv.
                append(commentHeader).
                append(commentList).
                append(commentFooter);

        bodyHtml.
                append(bulletin).
                append(commentDiv);

        option.bodyHtml = bodyHtml;

        var detailModal = new Modal(option);

        detailModal.create();
    };

    function Article(data){

        if(
            !isExist(data.id) ||
            !isExist(data.user_id) ||
            !isExist(data.rcmd_cnt) ||
            !isExist(data.content) ||
            !isExist(data.insert_ts) ||
            !isExist(data.comment_cnt)
        ){
            console.log("Wrong Message received");
            return;
        }
            
        var _article = 
                $("<div></div>").
                addClass("stuko_article").
                on("click", function(){
		        		ArticleDetailModal(data.id);
		        });
        
        /* Article - Header */
        var header = $("<h3></h3>");
        
        var headerText = 
                $("<span></span>").
                text(data.user_id);
        
        var rcmdBtn = 
                $("<i></i>").
                addClass("far fa-thumbs-up").
                html("<span>" + data.rcmd_cnt + "</span>");
        
        header.
            append(headerText).
            append(rcmdBtn);

        /* Article - Body */
        var body = 
                $("<div></div>").
                addClass("article_content").
                html(data.content);

        /* Article - Footer */
        var footer =
                $("<div></div>").
                addClass("article_footer");

        var date = new Date(data.insert_ts);
        var time =
                $("<time></time>").
                text(
                    date.getFullYear() + "/" + 
                    (date.getMonth() + 1) + "/" + 
                    date.getDate() + " " + 
                    pad(date.getHours(), 2) + ":" + 
                    pad(date.getMinutes(), 2));

        var commentCnt =
                $("<div></div>").
                text("<span>" + data.comment_cnt + "</span>");

        _article.
            append(header).
            append(body).
            append(footer);
		
		return _article;
    }


    /****************************************
    *   WebSocket
    ****************************************/
    function _onMessage(event){
    	console.log("_onMessage() invoked.");
        var data = JSON.parse(event.data);

        switch(data.reqType){
            case _reqType.CHAT_NICK_CHG:
                console.log("Wrong reqType");
                break;

            case _reqType.CHAT_NEW_MSG:
                appendChat(data);
                break;

            case _reqType.CHAT_CNT_CHG:
                $("#stuko_chatter_cnt").html(data.count);
                break;

            case _reqType.BOARD_TIMELINE:
                var newArticle = new Article(data);
                $(".stuko_article_list").prepend(newArticle);
                break;
                
            default :
                console.log("Wrong reqType");
            	break;
        }

    } // _onMessage
    
    function _onOpen(event){
        // nickhcange를 여기서 해야할듯?
        console.log("_onOpen()");
    }

    function _onClose(event){
        console.log("_onClose()");
    }

    function _onError(event){
        console.log("_onError()");
    }
    
    function _WebSocketInit(){
    	console.log("_WebSocketInit() invoked.");

        // Connect
        _ws = new WebSocket(_WebSocketURL);

        _ws.onopen = _onOpen;
        _ws.onmessage = _onMessage;
        _ws.onclose = _onClose;
        _ws.onerror = _onError;

    };


    /****************************************
    *   Board
    ****************************************/
    function renderHotTopic(){
		$.ajax({
			url: _AjaxURI + "/timeline/hottopic",
			type: "get",
			success: function(data){
				console.log("renderHotTopic::success");
				
				if(data === ""){
					// if there's no data
					var errImage = 
						$("<img></img>").
						attr("src", "../../resources/images/err_page.png").
						css({
							"height" : "200px",
							"width" : "100%"
						});
					
					$("#stuko_hottopic").append(errImage);
					
				}else{
					var article = new Article(data);
					$("#stuko_hottopic").append(article);
				}

				
			},
			error: function(resp){
				console.log("renderHotTopic::error");
				
			}
		});
    }

    function renderTimeline(){
		$.ajax({
			url: _AjaxURI + "/timeline",
			type: "get",
			dataType: "json",
			success: function(articleList){
                var timeline = $(".stuko_article_list");
                
                for(var i=0; i<articleList.length; i++){
                	
                    var article = new Article(articleList[i]);
                    
                    timeline.append(article);
                }

			},
			error:function(data){
				console.log("error : " ,data);
			}
		});
	};

    function _BoardInit(){
    	console.log("_BoardInit() invoked.");
		renderHotTopic();
		renderTimeline();
    };
   

    /****************************************
    *   Chatting
    ****************************************/
    var _chatDelay = 1000;
    var _nickDelay = 10000;
    
    function _sendMessage(){
    	console.log("_sendMessage");
    	var messageArea = $("#stuko_chatting_input");
    	var message = messageArea.val();
    	
    	if(message == ""){
    		return;
    	}

		if(message[message.length-1] == '\n') {
			message[message.length-1] = '';
		}
		
		message = convertEnter(message);
		
		var data = 
				{reqType: 2,
				message: message}
		
		_ws.send(JSON.stringify(data));
		messageArea.val("");
		
		$("#stuko_chatting_sender").attr("disabled", true);
		
		setTimeout(function(){
			console.log("_sendMessage:: ready");
			$("#stuko_chatting_sender").attr("disabled", false);
			
		}, _chatDelay);
    }
    
    function _changeNick(){
    	var nickBtn = $("#stuko_nick_change");
    	if(nickBtn.prop("disabled")){
    		return;
    	}
    	
    	var data = {reqType: _reqType.CHAT_NICK_CHG};
    	
    	_ws.send(JSON.stringify(data));

    	nickBtn.attr("disabled", true);
    	

    	var bodyHtml = 
        		$("<div></div>").
        		text("닉네임을 변경했습니다.");
    	
    	var opt = {
    			bodyHtml: bodyHtml,
    			headText: "Nickname"
    	};
    	
    	var confirm = new Modal(opt);
    	
    	confirm.create();
		
		setTimeout(function(){
			console.log("_changeNick:: ready");
			nickBtn.attr("disabled", false);
			
		}, _nickDelay);
    }
    
    function _ChatInit(){
    	console.log("_ChatInit() invoked.");
    	
    	$("#stuko_chatting_sender").on("click", _sendMessage);
    	$("#stuko_chatting_input").on("keypress", function(event){
			if(event.key == "Enter"){//enter
				if(event.shiftKey == false) {
					event.preventDefault();
					
					if(!$("#stuko_chatting_sender").prop("disabled")){
						_sendMessage();
					}
				}
		    }
    	});

		$("#stuko_nick_change").on("click", _changeNick);
		
    	
    };


    /****************************************
    *   Initialize
    ****************************************/
    _BoardInit();
    _WebSocketInit();
    _ChatInit();
    
})();