//로그인한 회원 정보를 가져오는 함수
async function getMyInfo(){
	//토큰으로 회원 정보 가져오기
	const token = localStorage.getItem("accessToken");
	
	if(!token){
		//console.error("로그인이 필요합니다.");
		return null;
	}
	//토큰을 서버에 전송하여 회원 정보 가져옴
	try{
		const response = await authFetch("/api/auth/me", {
			method : "get"
		});
		
		//토큰이 만료되서 인증에 실패한 경우
		if(response.status === 401 || response.status === 403){
			console.error("인증이 만료되거나 권한이 없습니다.");
			localStorage.removeItem("accessToken");
			return null;
		}
		
		const result = await response.json();
		return result;
	}catch(e){
		console.error(e);
		return null;
	}
}
//로그인 인증이 필요한 요청을 처리하는 함수
async function authFetch(url, options ={}){
	//fetch로 보낼 headers 정보를 담은 객체
	const headers = {
		...options.headers
	}
	
	//토큰 가져옴. 사원증을 가져옴
	const accessToken = localStorage.getItem("accessToken");
	
	//토큰이 있으면 headers에 토큰 정보를 추가
	if(accessToken){ //사원증이 있으면
	headers["Authorization"] = "Bearer " + accessToken; //요청할때 사원증 보여주기
	}
	
	const config = {
		...options,
		headers
	}
	
	const response = await fetch(url, config);
	//토큰 만료되면 리프레쉬토큰으로 재발급 후 다시 전송(나중에)
	//사원증 유효기간이 지나면 새 사원증 발급한 후 재요청
	if(response.status === 401 || response.status === 403){
		
	}
	return response;
}
