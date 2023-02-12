function checkAll(){
    if(document.getElementById("all").checked==true){  //id 를 사용하여 하나의 객체만을 호출
          for(var i=0;i<3;i++) document.getElementsByName("agreement")[i].checked=true;   //name 을 사용하여 배열 형태로 담아 호출
       }
       if(document.getElementById("all").checked==false){
          for(var i=0;i<3;i++) document.getElementsByName("agreement")[i].checked=false;
       }
 }

function test() {
	var p1 = document.getElementById('password1').value;
	var p2 = document.getElementById('password2').value;

	if(p1.length < 6) {
			alert('입력한 글자가 6글자 이상이어야 합니다.');
			return false;
		}

		if( p1 != p2 ) {
		  alert("비밀번호불일치");
		  return false;
		} else{
		  alert("비밀번호가 일치합니다");
		  return true;
		}
}