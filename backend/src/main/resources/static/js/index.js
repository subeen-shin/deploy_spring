/*==========================
DOM 트리 구조 로딩 후 실행
============================ */
document.addEventListener("DOMContentLoaded", async e=>{
	const info = await getMyInfo();
	let str = "반갑습니다."
	if(info && info.username){
		str = info.username +"님 " + str;
	}
	document.querySelector("#box").innerHTML = str;
});