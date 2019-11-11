var logger = (function(){

    var _isInfo = true;
    var _isDebug = true;
    
    var _Info = function(msg){
        if(_isInfo){
            console.log("Info : ", msg);
        }
    }
    var _Debug = function(msg){
        if(_isDebug){
            console.log("Debug : ", msg);
        }
    }

    return {
        info:_Info,
        debug:_Debug
    }

}());