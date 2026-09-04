/* ========================
DOM 트리 구성이 완료되면 실행
======================== */
document.addEventListener("DOMContentLoaded", e => {
    getPost();
    getComments();
});

/* ========================
게시글을 가져와 화면에 출력하는 함수
======================== */
async function getPost() {
    //url에 있는 게시글 번호를 가져옴
    const urlParams = new URLSearchParams(location.search);
    const postId = urlParams.get("num");
    try {
        //서버에 게시글을 요청하여 게시글을 가져와서 콘솔에 게시글을 출력하는 코드까지 작성하세요.
        //js, 컨트롤러, 서비스, 레포지토리 다 작성 

        //게시글을 가져옴
        //서버에 게시글 번호를 주고 게시글을 가져오라고 요청
        //url : /api/posts/게시글번호
        const response = await fetch(`/api/posts/${postId}`);
        //게시글 불러오기 실패
        if (!response.ok) {
            const result = await response.text();
            alert(result);
            throw Error("게시글이 없거나 삭제 됨");
        }
        const result = await response.json();
        const post = result.post;
        const files = result.files;
        console.log(files)
        //가져온 게시글을 화면에 출력
        insertValue("[name=title]", post.title);
        insertValue("[name=writer]", post.memberId);
        insertValue("[name=boardName]", post.board.name);
        insertValue("[name=view]", post.viewCount);
        insertValue("[name=createdAt]", post.createdAt.slice(0, 10));
        insertValue("[name=content]", post.content);
        document.querySelector(".up-count").textContent = post.upCount;
        document.querySelector(".down-count").textContent = post.downCount;

        //수정/추가버튼 보여주기/감추기
        visibleButtons(false);

        const 첨부파일박스 = document.querySelector("#files");
        //첨부파일 보여주기
        if (!files || files.length == 0) {
            첨부파일박스.innerHTML = `<div class="form-control">없음</div>`;
            return;
        }
        let html = '';
        //첨부파일 있으면
        files.forEach(file => {
            html += `
				<a 
					class="form-control" href="/api/upload/${file.savedName}" 
					download="${file.origianlName}">
					${file.origianlName}
				</a>
			`;
        })
        첨부파일박스.innerHTML = html;
    } catch (e) {
        console.error("게시글 가져오기 실패 : ", e);
    }
}
/* ========================
입력 요소의 value를 수정하는 함수
======================== */
function insertValue(selector, value) {
    document.querySelector(selector).value = value;
}
/* ========================
게시글 수정/삭제 버튼을 보여줄지를 결정하는 함수
======================== */
function visibleButtons(visible) {
    if (!visible) {
        document.querySelector(".btns").innerHTML = '';
    }
}
/* ========================
서버에 댓글 정보를 전송해서
댓글을 등록하는 함수 
======================== */
async function sendComment(e) {
    e.preventDefault();

    //서버에 보낼 정보를 만듬

    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData);

    if (data.content.trim().length == 0) {
        alert("댓글을 입력하세요.")
        return;
    }

    try {
        const urlParams = new URLSearchParams(location.search);
        const 게시글번호 = urlParams.get("num");


        //url : /api/posts/게시글번호/comments
        //method : post

        const response = await authFetch(`/api/posts/${게시글번호}/comments`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        alert(result.message);
        if (result.success) {
            //댓글 목록 새로고침
			changePage(0);
            //댓글 입력창 비우기
            const 댓글입력창 = e.target.querySelector("textarea");
            댓글입력창.value = "";

        }
    } catch (e) {
        console.error(e);
    }
}

/* ========================
서버에 댓글 정보를 전송해서
댓글을 등록하는 함수 
======================== */
async function getComments() {

    //댓글 목록 요청
    try {
        const urlParams = new URLSearchParams(location.search);
        const 게시글번호 = urlParams.get("num");

        const response = await authFetch(`/api/posts/${게시글번호}/comments?page=${data.page}`);

        if (!response.ok) {
            return;
        }

        const result = await response.json();

        const { content, page, startPage, endPage, hasNext, hasPrev, totalContentSize } = result;
        //댓글 목록 화면에 출력 
        displayComments(content);
        //댓글 페이지네이션 처리
        displayCommentPaging(page, startPage, endPage, hasNext, hasPrev, totalContentSize);
    } catch (e) {
        console.error(e);
    }


}
function displayComments(comments) {
    let html = '';
    comments.forEach(댓글 => {
        if (댓글.id == 댓글.originId) {
            html += `<li class="list-group-item p-3">`;
        } else {
            html += `
			<li class="list-group-item p-3 ms-4 bg-light border-start border-3 border-primary">
			`
        }
        html += `
			
		     <div class="d-flex justify-content-between align-items-center mb-1">
		       <span class="fw-bold">${댓글.memberId}</span>
		       <small class="text-muted">${댓글.createdAt.replace("T", " ")}</small>
		     </div>
		     <p class="mb-0 text-secondary">
			 	${댓글.content}
		     </p>
		   </li>
		`;
    })
    const 댓글목록 = document.querySelector("#comment-box");
    댓글목록.innerHTML = html;
}

function displayCommentPaging(page, startPage, endPage, hasNext, hasPrev, totalContentSize) {
    const 페이지네이션 = document.querySelector(".pagination");

    let 페이지네이션코드 = '';
	
	if(totalContentSize == 0){
		페이지네이션.innerHTML = '<l1>등록된 댓글이 없습니다.</l1>'
		return;
	}

    if (hasPrev) {
        페이지네이션코드 += `
				<li class="page-item">
				     <a class="page-link" href="javascript:void(0);" 
					 onclick="changePage(${startPage - 1 - 1})">이전</a>
				</li>
				 `;
    }

    //숫자페이지
    for (i = startPage;i <= endPage;i++) {
        //현재 페이지에 색상을 추가
        const active = i == page ? "active" : "";
        페이지네이션코드 += `
				<li class="page-item ${active}">
					<a class="page-link" href="javascript:void(0);" 
					onclick="changePage(${i - 1})">${i}</a>
				</li>
				`
    }

    if (hasNext) {
        페이지네이션코드 += `
				<li class="page-item">
					<a class="page-link" href="javascript:void(0);" 
					onclick="changePage(${endPage + 1 - 1})">다음</a>
				</li>
				`;
    }
    페이지네이션.innerHTML = 페이지네이션코드;
}
/* ========================
게시글 페이지를 눌렀을 때 (이전, 다음, 번호)
======================== */
function changePage(page) {
    data.page = page;
    getComments();
}

const data = {
	page : 0
	
}

async function clickLike(state){
	const urlParams = new URLSearchParams(location.search);
	const 게시글번호 = urlParams.get("num");
	
	//좋아요/싫어요 정보를 전송
	try{
		const response = await authFetch(`/api/posts/${게시글번호}/likes`, {
			method : "post",
			headers : {
				"Content-Type" : "application/json"
			},
			body : JSON.stringify({state})
		});
		//결과를 알림창으로 띄움
		const result = await response.json();
		alert(result.ms.message);
		if(result.ms.success){
			
			//좋아요,싫어요 숫자를 업데이트
	
			//좋아요,싫어요 버튼 상태를 업데이트
		}
	}catch(e){
		console.error(e);
	}

	
}
