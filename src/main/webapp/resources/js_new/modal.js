/****************************************
*   Usages
*
*   var opt = {};
*   var modal = new Modal([opt]);
*   modal.create();
*   modal.show();
*   modal.hide();
*   modal.destroy();
****************************************/
// 개선사항 1
// ok버튼 클릭시 무조건 destroy를 하면안된다.
// 예를 들어 확인이 아닌 '등록'이라는 동작을 할 때
// 등록 실패 시 모달을 꺼버린다면 써놓은 글이 날아간다.
// 이것은 일반적이지 않으며 불편하다.

// 개선사항 2
// closeCallback이벤트를 등록할 수 있게 해야한다.
var Modal = Modal || {};

Modal = function(option){
    
    /****************************************
    *   Default Options
    ****************************************/
    var _modal = null;


    /****************************************
    *   Default Options
    ****************************************/
    var _draggable = {
        handle: ".stuko_modal_handle",
        cancel: ".stuko_modal_close_btn",
        cursor: "move"
    };

    
    /****************************************
    *   Options for modal
    ****************************************/
    var _option = {
        /* Modal Position */
        top: null,
        left: null,

        /* Modal Size */
        width: null,
        height: null,

        /* Color */
        bgColor: "white",

        /* Modal Options */
        isBgClosable: true,

        /* Header options */
        headText: null,
        
        // Dependency : jQuery-ui
        isDraggable: true,

        /* Body options */
        bodyHtml: null,

        /* Footer options */
        isFooter: true,
        isOkBtn: true,
        isCancelBtn: true,
        okBtnText: "확인",
        cancelBtnText: "취소",
        
        okBtnCallback: null,        
        cancelCallback: null,
        
        /* NOT-FIXED */
        closable: true
    };


    /****************************************
    *   Utils
    ****************************************/
    function isOptValid(opt){
        if(typeof opt == "undefined" || opt == null) {
            return false;
        }
        return true;
    }


    /****************************************
    *   Constructor
    ****************************************/
    _modal = function(){
        /* Custom Option Setting */ 
        if(isOptValid(option)){
            /* Modal Position */
            if(isOptValid(option.top)){
                _option.top = option.top;
            }
            if(isOptValid(option.left)){
                _option.left = option.left;
            }
    
            /* Modal Size */
            if(isOptValid(option.width)){
                _option.width = option.width;
            }
            if(isOptValid(option.height)){
                _option.height = option.height;
            }

            if(isOptValid(option.bgColor)){
                _option.bgColor = option.bgColor;
            }

            /* Modal Options */
            if(isOptValid(option.isBgClosable)){
                _option.isBgClosable = option.isBgClosable;
            }
            if(isOptValid(option.closable)){
            	_option.closable = option.closable;
            }
    
            /* Header options */
            if(isOptValid(option.headText)){
                _option.headText = option.headText;
            }
            if(isOptValid(option.isDraggable)){
                _option.isDraggable = option.isDraggable;
            }
    
            /* Body options */
            if(isOptValid(option.bodyHtml)){
                _option.bodyHtml = option.bodyHtml;
            }
    
            /* Footer options */
            if(isOptValid(option.isOkBtn)){
                _option.isOkBtn = option.isOkBtn;
            }
            if(isOptValid(option.isCancelBtn)){
                _option.isCancelBtn = option.isCancelBtn;
            }
            if(isOptValid(option.okBtnCallback)){
                _option.okBtnCallback = option.okBtnCallback;
            }
            if(isOptValid(option.cancelCallback)){
            	_option.cancelCallback = option.cancelCallback;
            }
            if(isOptValid(option.okBtnText)){
                _option.okBtnText = option.okBtnText;
            }
            if(isOptValid(option.cancelBtnText)){
                _option.cancelBtnText = option.cancelBtnText;
            }
            if(isOptValid(option.isFooter)){
            	_option.isFooter = option.isFooter;
            }
        }


        /* Modal */
        var modal = 
                $("<div></div>").
                addClass("stuko_modal");

        /* Modal Content */
        var content = 
                $("<div></div>").
                addClass("stuko_modal_content");

        if(isOptValid(_option.top)){
            content.css("top", _option.top);
        }
        if(isOptValid(_option.left)){
            content.css("left", _option.left);
        }
        if(isOptValid(_option.width)){
            content.css("width", _option.width);
        }
        if(isOptValid(_option.height)){
            content.css("height", _option.height);
        }

        if(_option.isBgClosable){
            modal.on("click", function(event){
                _destroy();
            });

    		if(isOptValid(_option.cancelCallback)){
    			modal.on("click", function(event){
    				_option.cancelCallback();
    			});
    		}
            
            content.on("click", function(event){
                event.stopPropagation();
            });
        }

        
        /* Modal Header */
        var header = 
                $("<header></header>").
                addClass("stuko_modal_container").
                addClass("stuko_modal_handle");

        var headerTitle = 
                $("<span></span>");

        var closeBtnIcon = 
                $("<i></i>").
                addClass("fas fa-times");
        
        var closeBtn = 
                $("<div></div>").
                addClass("stuko_btn").
                addClass("stuko_modal_close_btn").
                append(closeBtnIcon).
                on("click", function(){
        			_destroy();;
                });

		if(isOptValid(_option.cancelCallback)){
			closeBtn.on("click", function(event){
				_option.cancelCallback();
			});
		}

        if(_option.isDraggable){
            $(content).draggable(_draggable);
        }

        if(isOptValid(_option.headText)){
            $(headerTitle).text(_option.headText);
        }

        header.
            append(headerTitle).
            append(closeBtn);

        content.append(header);

        /* Modal Body */
        var body = 
                $("<div></div>").
                addClass("stuko_modal_container");

        if(isOptValid(_option.bodyHtml)){
            body = 
                _option.bodyHtml.
                addClass("stuko_modal_container");
        }
        if(isOptValid(_option.bgColor)){
            body.css("backgroundColor", _option.bgColor);
        }

        content.append(body);
        
        /* Modal Footer */
        if(_option.isFooter){
        	var footer = 
        		$("<footer></footer").
        		addClass("stuko_modal_container");
        	
        	var btnOk = 
        		$("<button></button>").
        		addClass("stuko_btn stuko_modal_button_ok").
        		text(_option.okBtnText);
        	
        	var btnCancel =
        		$("<button></button>").
        		addClass("stuko_btn stuko_modal_button_cancel").
        		text(_option.cancelBtnText);
        	
        	if(_option.isOkBtn){
        		btnOk.on("click", function(event){
    				_destroy(_option.okBtnCallback);
        		});
        	}
        	
        	if(_option.isCancelBtn){
        		btnCancel.on("click", function(event){
        			_destroy();
        		});
        		if(isOptValid(_option.cancelCallback)){
        			btnCancel.on("click", function(event){
        				_option.cancelCallback();
        			});
        		}
        	}
        	
        	
        	footer.
        	append(btnOk).
        	append(btnCancel);
        	
        	content.append(footer);
        }

        modal.append(content);

        return modal;
    }();


    /****************************************
    *   Exposed API
    ****************************************/
    function _create(){
        _modal.hide();
        $("body").append(_modal);
        _modal.fadeIn();
    }
    
    function _destroy(_callback){
        console.log("_destroy");
        console.log(_option);

        if(_option.closable){
	        _modal.fadeOut(150, function(){
	                $(_modal).remove();	
	    
	            if(isOptValid(_callback)){
	                _callback();
	            }
	        });
	        
        }else{
            if(isOptValid(_callback)){
                _callback();
            }        	
        }
    }
    function _show(){
        _modal.show();
    }
    function _hide(){
    	console.log("modal::_hide()")
        _modal.hide();
    }
    function _getElement(){
        return _modal;
    }


    /****************************************
    *   New Modal Object
    ****************************************/
    return {
        create:_create,
        destroy:_destroy,
        show:_show,
        hide:_hide,
        getElement: _getElement
    }
};