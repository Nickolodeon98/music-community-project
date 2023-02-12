function checkAll(){
    if(document.getElementById("all").checked==true){  //id 를 사용하여 하나의 객체만을 호출
          for(var i=0;i<3;i++) document.getElementsByName("agreement")[i].checked=true;   //name 을 사용하여 배열 형태로 담아 호출
       }
       if(document.getElementById("all").checked==false){
          for(var i=0;i<3;i++) document.getElementsByName("agreement")[i].checked=false;
       }
 }

function checkPw() {
	var p1 = document.getElementById('password').value;
	var num = p1.search(/[0-9]/g);
	var eng = p1.search(/[a-z]/ig);
	var spe = p1.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	var p2 = document.getElementById('verification').value;

	if(p1.length < 8 || p1.length > 20) {
			alert('비밀번호는 8~20글자여야합니다.');
			return false;
		}
	if(p1.search(/\s/)!=-1){
		alert('비밀번호는 공백없이 입력해주세요.');
		return false;
	}
	if(num < 0 || eng < 0 || spe < 0 ){
		alert("영문,숫자, 특수문자를 혼합한 8~20글자를 입력해주세요.");
		return false;
	}
		if( p1 != p2 ) {
			alert("비밀번호가 일치하지 않습니다.");
			return false;
		  } else{
			alert("비밀번호가 변경되었습니다.");
			return true;
		  }

}