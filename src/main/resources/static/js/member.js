function checkAll(){
    if(document.getElementById("all").checked==true){  //id 를 사용하여 하나의 객체만을 호출
          for(var i=0;i<3;i++) document.getElementsByName("agreement")[i].checked=true;   //name 을 사용하여 배열 형태로 담아 호출
       }
       if(document.getElementById("all").checked==false){
          for(var i=0;i<3;i++) document.getElementsByName("agreement")[i].checked=false;
       }
 }

function checkPw() {

	var p1 = document.getElementById('newPassword').value;
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

function checkJoinForm(){
  var email = document.getElementById("email");
  var password = document.getElementById("password");
  var doubleCheckPw = document.getElementById("doubleCheckPw");
  var nickName = document.getElementById("nickName");
  var name = document.getElementById("name");

  if(!checkExistData(email,"이메일"){
     alert("이메일을 입력해주세요");
     email.focus();
    return false;
  }
  if(!checkExistData(password,"비밀번호")){
     alert("비밀번호을 입력해주세요");
     password.focus();
    return false;
  }
  if(!checkExistData(doubleCheckPw,"비밀번호 확인")){
     alert("비밀번호를 한 번 더 입력해주세요");
     doubleCheckPw.focus();
    return false;
  }
  if(!checkExistData(nickName,"닉네임")){
     alert("닉네임을 입력해주세요");
     nickName.focus();
    return false;
  }
  if(!checkExistData(name,"이름")){
     alert("이름을 입력해주세요");
     name.focus();
    return false;
  }
  if(!checkAccept()){
    return false;
  }
  alert("가입되었습니다.")
  return true;
}

function checkExistData(value){
  if(value==""){
    return false;
  }
  return true;
}

function checkAccept(){
  if(document.getElementById('agreement1').checked && document.getElementById('agreement2').checked){
    return true;
  } else {
    alert("서비스 이용 및 개인정보 수집과 이용에 대한 안내에 동의가 필요합니다.");
    return false;
  }
}

