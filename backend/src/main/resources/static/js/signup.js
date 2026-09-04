/*==========================
입력한 회원 정보를 저장하는 객체
============================ */
const member = {
	id : '',
	pw :'',
	pw2 :'',
	email :''
}


/*==========================
서버에 회원가입을 요청하는 함수
============================ */


async function signup(e) {
    e.preventDefault();
	//아이디 체크
	//id 정규표현식
	// \w : 숫자나 알파벳 1글자
	// {최소, 최대} => {3, } 최소 3자이상
	// ^ : 시작
	// $ : 끝
	//정규표현식 맛보기
	const idRegex = /^\w{3,}$/;
	if(!idRegex.test(member.id)){
		alert("아이디는 3자이상입니다.")
		return;
	}
	//비번 체크
	if(member.pw.length < 3){
		alert("비번은 3글자 이상입니다.")
		return;
	}
	//비번 확인 체크
	if(member.pw !== member.pw2){
		alert("비밀번호가 일치하지 않습니다.");
		return;
	}
	//이메일은 입력확인 체크
	if(member.email.length === 0){
		alert("이메일은 필수 항목입니다.");
		return;
	}
    const 폼태그 = e.target;
    //form 태그 안에 있는 입력값들을 가져옴
    const formData = new FormData(폼태그);
    //객체로만듬
    const data = Object.fromEntries(formData);


    //서버로 회원 정보를 주면서 가입요청
    //url : /api/auth/signup
    try {
        const response = await fetch("/api/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json" //전송할 데이터 타입
            },
            body: JSON.stringify(member)
        });
		//실패하면 알림 문구
        if (!response.ok) {
			throw new Error("요청 실패!");
			}
			//성공하면 성공문구 출력하고 메인페이지로
            const result = await response.json();
            alert(result.message);
			//회원 가입 성공 후 메인 페이지로 이동
			if(result.success){
				location.href = "/";
			}
			
    } catch (e) {
        console.log("회원가입 실패 : ", e);
		alert("서버에 이상이 있습니다.");
    }
}
/*==========================
입력 태그에 입력이 되면 member 객체와 일치하는 속성의 값을
변경하는 함수
============================ */

function changeInput(e){
	const {name, value} = e.target;
	member[name] = value;
}