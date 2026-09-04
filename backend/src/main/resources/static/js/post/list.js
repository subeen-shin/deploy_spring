/* ========================
DOM 트리 구성이 완료되면 실행
======================== */
document.addEventListener("DOMContentLoaded", e => {
    getPosts();
});

/* ========================
게시글 목록을 불러와서 화면에 배치하는 함수
======================== */
async function getPosts(e) {

    //서버에 게시글 목록을 요청해서 화면에 배치하는 작업

    try {
        //서버에 게시글 목록을 요청
        //url : /api/posts
        //method : get
        /*const response = await fetch("url", {
            method : "방식",
            headers : {},
            body : 보낼값
        });*/

        const queryString = '?' + new URLSearchParams(data).toString();

        const response = await fetch("/api/posts" + queryString);

        if (!response.ok) {
            throw Error("서버 상태 이상");
        }
        //가져오 게시글들을 이용하여 html코드로 구성
        const result = await response.json();
        const { content, hasPrev, hasNext, startPage, endPage, page } = result;
        let str = '';
        content.forEach(post => {
            str += `
				<tr>
			      <td>${post.id}</td>
			      <td><a href="/post/detail.html?num=${post.id}">${post.title}</a></td>
			      <td>${post.memberId}</td>
			      <td>${post.createdAt.slice(0, 10)}</td>
			      <td>${post.upCount}/${post.downCount}</td>
			    </tr>
			`;
        });

        //table태그 안 body에 html코드를 덮어쓰기
        const 테이블바디 = document.querySelector(".table tbody");

        if (result.length != 0) {
            테이블바디.innerHTML = str;
        }
        else {
            테이블바디.innerHTML = `
			<tr>
				<td class="text-center">등록된 게시글이 없습니다.</td>
			</tr>
			`;
        }

        const 페이지네이션 = document.querySelector(".pagination");

        let 페이지네이션코드 = '';

        if (hasPrev) {
            페이지네이션코드 += `
			<li class="page-item">
			     <a class="page-link" href="javascript:void(0);" 
				 onclick="changePage(${startPage - 1 -1})">이전</a>
			</li>
			 `;
        }
		
		//숫자페이지
		for(i = startPage; i <= endPage; i++){
			//현재 페이지에 색상을 추가
			const active = i == page ? "active" : "";
			페이지네이션코드 += `
			<li class="page-item ${active}">
				<a class="page-link" href="javascript:void(0);" 
				onclick="changePage(${i -1})">${i}</a>
			</li>
			`
		}

        if (hasNext) {
            페이지네이션코드 += `
			<li class="page-item">
				<a class="page-link" href="javascript:void(0);" 
				onclick="changePage(${endPage + 1 -1})">다음</a>
			</li>
			`;
        }
		페이지네이션.innerHTML = 페이지네이션코드;
    } catch (e) {
        console.error("게시글 목록 불러오기 실패 : ", e);
    }
}
/* ========================
검색 버튼 눌렀을 때
======================== */
function submitSearch(e) {
    e.preventDefault();
    getPosts();
}
/* ========================
게시글 페이지를 눌렀을 때 (이전, 다음, 번호)
======================== */
function changePage(page) {
    data.page = page;
    getPosts();
}

const data = {
    type: 'all', //검색 타입
    keyword: '', //검색어
    page: 0, //현재 페이지 번호 -1
    size: 3, //한 페이지 게시글 수
    sort: 'id,desc' //정렬 방법
    //page, size, sort가 자동으로 컨트롤러에 pageable 클래스의 객체로 들어감
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