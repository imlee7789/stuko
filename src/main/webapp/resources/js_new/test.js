var modal = modal || {};

modal.test = function(name){
    var _this = this.this;
    var _name = name;

    var _getName = function(){
        console.log(_name);
    };
    var _setName = function(name){
        _name = name;
    };

    return {
        getName: _getName,
        setName: _setName

    }
};

var md1 = new modal.test("original");
var md2 = new modal.test("dse");

md1.getName();
md2.getName();

md1.setName("md1");
md2.setName("md2");

md1.getName();
md2.getName();