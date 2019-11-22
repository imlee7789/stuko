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
    
    function convertEnterReverse(text){
    	return text.split("<br>").join("\n");
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
//		if ( scrollHeight - height - top == 0) {
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

        _chat.hide();
        $("#stuko_chatting_view").append(_chat);
        _chat.show("fade", 100);

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
			url: _AjaxURI + "/timeline/" + id,
			type: "post",
			data: data,
			dataType: "json",
			success: function(comment){
				console.log(comment);
	            appendComment(comment, null);
	            var commentArea = $(".stuko_modal_comment_input");
	            commentArea.val("");
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
    			url: _AjaxURI + "/timeline/" + id,
    			type: "delete",
    			dataType: "json",
    			data: data,
    			success: function(detail){
    				console.log("_deleteArticle() succeed.");

    				var _okCallback = function(){
    					// NOT-FIXED
    					// 새로고침하면 안된다. 채팅이 날아가기 때문에...
    					location.reload();
    				}
    				
	            	var opt = {
	            			bodyHtml: $("<div></div>").html("게시글 삭제 성공"),
	            			okBtnCallback: _okCallback,
	            			headText: "Delete"
	            	};
	            	
	            	var confirm = new Modal(opt);
	            	
	            	confirm.create();
    				
    			},
    			error:function(data){
    				
    				var _okCallback = function(){
    					location.reload();
    				}
    				console.log("error : " ,data);
    				
    				var text = "비밀번호가 틀렸습니다";

	            	var opt = {
	            			bodyHtml: $("<div></div>").html(text),
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
			url: _AjaxURI + "/timeline/" + id,
			type: "patch",
			dataType: "json",
			success: function(detail){
				console.log("articleRcmdUp() invoked.");

            	var bodyHtml = 
                		$("<div></div>").
                		text("추천하셨습니다");
            	
            	var opt = {
            			bodyHtml: bodyHtml,
            			headText: "Recommend",
            			isCancelBtn: false
            	};
            	
            	var rcmdSpan = $(".modal_bulletin_rcmd_btn");
            	var rcmdCnt = parseInt(rcmdSpan.text())+1;

            	rcmdSpan.text(rcmdCnt);
            	
            	var confirm = new Modal(opt);
            	
            	confirm.create();
            	
            	var articles = $(".stuko_article");
            	
            	for(var i=0; i<articles.length; i++){
                	var dataId = $(articles[i]).attr("data-id");
                	if(dataId == id){
                		$(articles[i]).find("h3>i>span").text(rcmdCnt);
                	}
            	}
            	
			},
			error:function(data){
				console.log("error : " ,data);
            	var bodyHtml = 
            		$("<div></div>").
            		text("추천 실패");
        	
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
			url: _AjaxURI + "/timeline/" + id,
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
			url: _AjaxURI + "/timeline/" + id + "/comment",
			type: "get",
			dataType: "json",
			success: function(respCommentList){
				
				RenderDetailModal(detail, respCommentList);
				
			},
			error:function(data){
				console.log("error : " ,data);
			}			
		});
    }
    

    function _updateArticle(id){
    	console.log("registerArticle() invoked.");
    	
    	var data = {
    			user_id: $("#update_user_name").val(),
    			user_pw: $("#update_user_pswd").val(),
    			content: convertEnter($("#update_content").val())
    	};
    	
    	console.log(data);
    	
		$.ajax({
			url: _AjaxURI + "/timeline/" + id,
			type: "put",
			data: data,
			dataType: "json",
			success: function(data){
				console.log("_registerArticle()::ajax::success");
				
				var _okCallback = function(){
					//modal클로즈
					var modals = $(".stuko_modal");
					for(var i=0; i<modals.length; i++){
						$(modals[i]).remove();
					}
				}

            	var opt = {
            			bodyHtml: $("<div></div>").html("글 수정 성공"),
            			okBtnCallback: _okCallback,
            			headText: "Success"
            	};
            	
            	var confirm = new Modal(opt);
            	
                 	confirm.create();
				
			},
			error:function(data){
				console.log("_registerArticle()::ajax::error");
				console.log("error : ", data);

				var text = "비밀번호가 틀렸습니다";

            	var opt = {
            			bodyHtml: $("<div></div>").html(text),
            			headText: "Update"
            	};
            	
            	var confirm = new Modal(opt);
            	
            	confirm.create();
			}			
		});
    	
    };
    
    
    function _openUpdateModal(id){
    	console.log("_openUpdateModal() invoked.");

    	$.ajax({
			url: _AjaxURI + "/timeline/" + id,
			type: "get",
			dataType: "json",
			success: function(detail){

		    	var bodyHtml = 
		    			$("<div></div>").
		    			addClass("modal_update_body");
		    	
		    	var bodyHtmlText = 
			        "<div class='modal_update_body'>" +
			    		"<h2 id='modal_update_action_type'>글 수정</h2>" +
			    		"<table class='modal_update_table'>" +
			                "<tr class='modal_update_author'>" +
			                    "<td>작성자 명</td>" +
			                    "<td><input disabled type='text' id='update_user_name'></td>" +
			                    "<td>비밀번호</td>" +
			                    "<td><input type='password' id='update_user_pswd'></td>" +
			                "</tr>" +
			                "<tr class='modal_update_content'>" +
			                    "<td>내용</td>" +
			                    "<td colspan='3'>" +
			                        "<textarea id='update_content'></textarea>" +
			                    "</td>" +
			                "</tr>" +
			            "</table>" +
			        "</div>";
		    	
		    	bodyHtml.html(bodyHtmlText);
		    	
		    	bodyHtml.find("#update_user_name").val(detail.user_id);
		    	bodyHtml.find("#update_content").text(convertEnterReverse(detail.content));
		    	
		    	var regiBtn = 
		    			$("<button></button>").
		    			attr("id", "modal_update_regi_btn").
		    			text("수정").
		    			on("click", function(){
		    				_updateArticle(id);
		    			});
		    	
		    	var _cancelCallback = function(){

		        	var bodyHtml = 
		            		$("<div></div>").
		            		text("정말 글 수정을 중단하시겠습니까?");

		        	var _okCallback = function(){
						var modals = $(".stuko_modal");
						for(var i=0; i<modals.length; i++){
							$(modals[i]).remove();
						}
		        	}
		        	
		        	var opt = {
		        			bodyHtml: bodyHtml,
		        			okBtnCallback: _okCallback,
		        			headText: "Update"
		        	};
		        	
		        	var confirm = new Modal(opt);
		        	
		        	confirm.create();
		    	}
		    	
		    	var cancelBtn = 
		    			$("<button></button>").
		    			attr("id", "modal_update_cancel_under_btn").
		    			text("취소").
		    			on("click", function(){
		    				_cancelCallback();
		    				
		    			});

		    	var btns = 
		    			$("<div></div>").
		    			addClass("modal_update_btns").
		    			append(regiBtn).
		    			append(cancelBtn);
		    	
		    	bodyHtml.find(".modal_update_body").append(btns);
		        
		    	var opt = {
		                /* Modal Position */
		                top: "5vh",
		                left: "calc(50vw - 350px)",

		                /* Modal Size */
		                width: "700px",
		                height: "85vh",
		                
		        		bodyHtml: bodyHtml,
		        		headText: "Update article",
		                isFooter: false,
		                cancelCallback: _cancelCallback,
		                closable:false
		        	};
		        	
		    	var updateModal = new Modal(opt);
		    	
		    	updateModal.create();
				
			},
			error:function(data){
				console.log("error : " ,data);
			}
		});
    	
    }
    

    /* ArticleModal - step 3 */
    function RenderDetailModal(detail, respCommentList){
    	console.log("RenderDetailModal() invoked.");
    	
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
	                addClass("far fa-edit").on("click", function(){
	                	_openUpdateModal(detail.id);
	                })
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
                attr("data-id", data.id).
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
                html("<span>댓글 " + data.comment_cnt + "</span>");

        footer.
	        append(time).
	        append(commentCnt);
        
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
                
                newArticle.hide();
                $(".stuko_article_list").prepend(newArticle);
                newArticle.show("blind", 800);
                
                break;
                
            default :
                console.log("Wrong reqType");
            	break;
        }

    } // _onMessage
    
    function _onOpen(event){
        // nickhchange를 여기서 해야할듯?
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
    var isReadyLoadTimeline = true;
    
    
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

    function renderTimeline(pageStart, length){
    	console.log("renderTimeline() invoked.");

    	var data = {
    			pageStart: pageStart,
    			length: length
    	};
    	
		$.ajax({
			url: _AjaxURI + "/timeline",
			type: "get",
			dataType: "json",
			data: data,
			success: function(articleList){
                var timeline = $(".stuko_article_list");
                
                for(var i=0; i<articleList.length; i++){
                	console.log(articleList[i].id);
                	
                    var article = new Article(articleList[i]);
                    
                    timeline.append(article);
                }
                
                isReadyLoadTimeline = true;

			},
			error:function(data){
				console.log("error : " ,data);

                isReadyLoadTimeline = true;
			}
		});
	};
	
	function renderTimelineOnScroll(){
		
		var timelineLen = 5;
		
		$(".stuko_article_list").
			scroll(function(){
				console.log("scroll event invoked.");
				
				var isBottom = isScrollBottom(".stuko_article_list");

				if(isBottom){
					
					if(isReadyLoadTimeline){
						isReadyLoadTimeline = false;
							
						// 타임라인을 어펜드해야한다.
						// but, 마지막 글의 아이디를 알고 있어야 한다.
						// 2019.11.17 lhs : 아이디가 아닌 글의 개수가 몇 개 인지 확인해야한다.
						var articles = $(".stuko_article");
						var articleLenLoaded = articles.length;
						
						renderTimeline(articleLenLoaded, timelineLen);
						
					}
					
				}
			}
		);
		
	};

    function _BoardInit(){
    	console.log("_BoardInit() invoked.");
		renderHotTopic();
		renderTimeline(0, 10);
		renderTimelineOnScroll();
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
    
    function _registerArticle(){
    	console.log("registerArticle() invoked.");
    	
    	var data = {
    			user_id: $("#update_user_name").val(),
    			user_pw: $("#update_user_pswd").val(),
    			content: convertEnter($("#update_content").val())
    	};
    	
    	console.log(data);
    	
		$.ajax({
			url: _AjaxURI + "/timeline",
			type: "post",
			data: data,
			dataType: "json",
			success: function(data){
				console.log("_registerArticle()::ajax::success");
				
				var _okCallback = function(){
					//writemodal클로즈
					var modals = $(".stuko_modal");
					for(var i=0; i<modals.length; i++){
						$(modals[i]).remove();
					}
				}

            	var opt = {
            			bodyHtml: $("<div></div>").html("글 쓰기 성공"),
            			okBtnCallback: _okCallback,
            			headText: "Success"
            	};
            	
            	var confirm = new Modal(opt);
            	
            	confirm.create();
				
			},
			error:function(data){
				console.log("_registerArticle()::ajax::error");
				console.log("error : ", data);
			}			
		});
    	
    };
    
    function _openWriteModal(){
    	console.log("_openWriteModal() invoked.");
    	
    	var bodyHtml = 
    			$("<div></div>").
    			addClass("modal_update_body");
    	
    	var bodyHtmlText = 
	        "<div class='modal_update_body'>" +
	    		"<h2 id='modal_update_action_type'>글 쓰기</h2>" +
	    		"<table class='modal_update_table'>" +
	                "<tr class='modal_update_author'>" +
	                    "<td>작성자 명</td>" +
	                    "<td><input type='text' id='update_user_name'></td>" +
	                    "<td>비밀번호</td>" +
	                    "<td><input type='password' id='update_user_pswd'></td>" +
	                "</tr>" +
	                "<tr class='modal_update_content'>" +
	                    "<td>내용</td>" +
	                    "<td colspan='3'>" +
	                        "<textarea id='update_content'></textarea>" +
	                    "</td>" +
	                "</tr>" +
	            "</table>" +
	        "</div>";
    	
    	bodyHtml.html(bodyHtmlText);
    	

    	var regiBtn = 
    			$("<button></button>").
    			attr("id", "modal_update_regi_btn").
    			text("등록").
    			on("click", function(){
    				console.log("등록");
    				_registerArticle();
    			});
    	
    	var _cancelCallback = function(){

        	var bodyHtml = 
            		$("<div></div>").
            		text("정말 글쓰기를 취소하시겠습니까?");

        	var _okCallback = function(){
				var modals = $(".stuko_modal");
				for(var i=0; i<modals.length; i++){
					$(modals[i]).remove();
				}
        	}
        	
        	var opt = {
        			bodyHtml: bodyHtml,
        			okBtnCallback: _okCallback,
        			headText: "Delete"
        	};
        	
        	var confirm = new Modal(opt);
        	
        	confirm.create();
    	}
    	
    	var cancelBtn = 
    			$("<button></button>").
    			attr("id", "modal_update_cancel_under_btn").
    			text("취소").
    			on("click", function(){
    				_cancelCallback();
    				
    			});

    	var btns = 
    			$("<div></div>").
    			addClass("modal_update_btns").
    			append(regiBtn).
    			append(cancelBtn);
    	
    	bodyHtml.find(".modal_update_body").append(btns);
        
    	var opt = {
                /* Modal Position */
                top: "5vh",
                left: "calc(50vw - 350px)",

                /* Modal Size */
                width: "700px",
                height: "85vh",
                
        		bodyHtml: bodyHtml,
        		headText: "New article",
                isFooter: false,
                cancelCallback: _cancelCallback,
                closable:false
        	};
        	
    	var writeModal = new Modal(opt);
    	
    	writeModal.create();	
    }
    
    function _NaviInit(){
    	$("#write_btn").on("click", function(){
    		_openWriteModal();
    	});
    	$("#home_btn").on("click", function(){
    		location.replace("/");
    	});
	}

	function toggleDictionary() {
		console.log("toggleDictionary() invoked.");
		
		var dict = $(".stuko_dict");
		var status = dict.css("display");
		var dictBtn = $("#dict_btn");
		
		if(status == "none"){
			dict.show("slide", 300);
			dictBtn.addClass("stuko_nav_toggle");

		}else{
			dict.hide("slide", 500);
			dictBtn.removeClass("stuko_nav_toggle");
		}
	}
	
	function getWordTemplate(def){

        var date = new Date(def.insert_ts);
        var time =
                date.getFullYear() + "/" + 
                (date.getMonth() + 1) + "/" + 
                date.getDate() + " " + 
                pad(date.getHours(), 2) + ":" + 
                pad(date.getMinutes(), 2);
		
		var template = 
			"<div class='stuko_word'>" +
				"<div class='stuko_word_time'>" +
					"<i class='fa fa-ad stuko_word_icon'></i>" +
					"<time> " + time + " </time>" +
					"<i class='stuko_text_btn far fa-thumbs-up'><span> " + def.rcmd_cnt + "</span></i>" + 
					"<i class='stuko_text_btn far fa-trash-alt righter'></i>" +
					"<i class='stuko_text_btn modal_bulletin_update_btn far fa-edit righter'></i>" +
				"</div>" +
				"<div class='word_desc'>" +
				def.content +
				"</div>" +
			"<ul class='word_ref_list'>";
		
		for(var i=0; i<def.refs.length; i++){
			template += 
				"<li class='word_ref'>" +
					"<div class='word_ref_desc'>" + def.refs[i].description + "</div>" +
					"<a class='word_ref_url' target='_black' href='" + def.refs[i].url + "'>" + def.refs[i].url + "</a>" +
				"</li>";
		}

		template += "</ul></div>";
		
		return template;
	}
	
	function renderWords(defs){
		
		var wordList = $(".stuko_word_list");

		wordList.text("");
		
		for(var i=0; i<defs.length; i++){
			var template = getWordTemplate(defs[i]);
			
			wordList.append(template);
		}
		
		var words = wordList.find(".registered_word");
		
		console.log(words);
		
		for(var i=0; i<words.length; i++){
//			console.log($(words[i]).text());
//			console.log(words[i]);
//			console.log($(words[i]));
			$(words[i]).on("click", function(){
				console.log($(words[i]).text());
				console.log($(this).text());
				$(".stuko_dcit_search>input").val($(this).text());
				searchWord();
			});

		}
	}
	
	function renderNotFound(){
		console.log("renderNotFound()");

		var wordList = $(".stuko_word_list");

		wordList.text("");
		
		var notfound = 
		"<div class='stuko_word_not_found'>" +
	        "<img src='../../resources/images/notfound.png'><br/>" +
	        "일치하는 단어가 없습니다" +
	    "</div>";
		
		wordList.append(notfound);
	}

	function searchWord(){
		var searchWord = $(".stuko_dcit_search>input").val();

		$(".loading-bg").show("fade", 300);
		setTimeout(function(){
			$.ajax({
				url: _AjaxURI + "/dict/" + searchWord,
				type: "get",
				dataType: "json",
				success: function(data){
					
					if(data !== undefined && data.defs.length > 0){
						renderWords(data.defs);
						
					}else{
						renderNotFound();
					}
					setTimeout(function(){
						$(".loading-bg").hide("fade", 200);
					},200);			
				},
				error:function(data){
					console.log("error : ", data);
					renderNotFound();
					setTimeout(function(){
						$(".loading-bg").hide("fade", 200);
					},200);
				}
			});
		},500);
		
	}

	function _DictInit(){
		console.log("_DictInit() invoked.");
		$("#dict_btn").on("click", toggleDictionary);

		$("button.stuko_text_btn").on("click",searchWord);

		$(".stuko_dcit_search>input").on("keyup", function(e){
			if(event.key == "Enter"){//enter
				searchWord();
			}
		});
	}

    /****************************************
    *   Initialize
    ****************************************/
    _BoardInit();
    _NaviInit();
    _WebSocketInit();
	_ChatInit();
	_DictInit();
	
	$(window).on("keydown", function (e) {
		if(e.key === "ArrowRight" || e.key=== "ArrowLeft"){
			return false;
		}
	});
    
})();