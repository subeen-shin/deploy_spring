/* ========================
DOM 트리 구성이 완료되면 실행
======================== */
document.addEventListener("DOMContentLoaded", e => {
    getBoardsAndDisplay();
});
/* ========================
게시판 목록을 가져와서 화면에 배치
======================== */
async function getBoardsAndDisplay() {
    try {
        //게시판 목록 가져오기
        const response = await fetch(`/api/boards`, {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        const result = await response.json();

        //게시판 목록을 화면에 배치하기
        let html = '<option value="11">게시판을 선택하세요.</option>';
        result.forEach(board => {
            html += `
				<option value="${board.id}">${board.name}</option>
			`;
        })
        document.querySelector("[name=boardId]").innerHTML = html;
    } catch (e) {
        console.error(e);
    }
}
/* ========================
게시글을 등록하는 함수
======================== */
async function insertPost(e) {
    e.preventDefault();

    //입력값(제목, 내용, 게시판)체크
    if (data.title.trim().length == 0) {
        alert("제목을 입력하세요.");
        return;
    }
	if (data.boardId.length == 0) {
		alert("게시판을 선택하세요.");
		return;
	}
	

    //입력값(제목, 내용, 게시판)체크
    if (data.content.trim().length == 0) {
        alert("내용을 입력하세요.");
        return;
    }
	
	//게시글과 첨부파일을 하나로 합침
	const formData = new FormData();
	
	const jsonBlob = new Blob([JSON.stringify(data)], 
	{type : "application/json"});
	formData.append("post", jsonBlob);
	
	const 첨부파일요소들 = document.querySelectorAll("[name=files]");
	첨부파일요소들.forEach(첨부파일요소=>{
		const 첨부파일들 = 첨부파일요소.files;
		for(let i = 0; i < 첨부파일들.length; i++){
			formData.append("files", 첨부파일들[i])
		}
	});
	
	
	
	//서버로 게시판 등록 요청
	try {
	        //게시판 게시글 등록 요청
	        const response = await authFetch(`/api/posts`, {
	            method: "post",
	            body: formData
	        });
			
	        const result = await response.json();
			alert(result.message);
			if(result.success){
				location.href ="/post/list.html";
			}
	       
	        
	    } catch (e) {
	        console.error(e);
	    }
	
}




const data = {
	title : '',
	content : '',
	boardId : ''
}
/* ========================
입력태그(input, select, textarea등)에 입력하면 입력된 값을 가져와서
data 객체에 저장하도록 하는 함수
- 단, data 객체는 전역으로 선언이 되어 있어야 한다.
======================== */
function changeInput(e) {
    //객체에 있는 값들을 변수에 쉽게 저장하는 방법
    const { name, value } = e.target;
    //객체에 있는 속성의 값을 변경
    data[name] = value;

}


