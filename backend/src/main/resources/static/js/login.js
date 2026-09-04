/*==========================
입력한 회원 정보를 저장하는 객체
============================ */
const member = {
    id: '',
    pw: '',
}


/*==========================
서버에 로그인을 요청하는 함수
============================ */


async function login(e) {
    e.preventDefault();

    if (member.id.trim().length == 0) {
        alert("아이디를 입력하세요.");
        return;
    }
    //서버로 회원 정보를 주면서 가입요청
    //url : /api/auth/login
    try {
        const response = await fetch("/api/auth/login", {
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
        alert(result.state.message);
		//회원가입에 성공하면 사원증 저장
		if(result.state.success){
			localStorage.setItem("accessToken", result.accessToken);
			location.href ="/";
		}
      
    } catch (e) {
        console.log("로그인 실패 : ", e);
        alert("서버에 이상이 있습니다.");
    }
}
/*==========================
입력 태그에 입력이 되면 member 객체와 일치하는 속성의 값을
변경하는 함수
============================ */

function changeInput(e) {
    const { name, value } = e.target;
    member[name] = value;
}