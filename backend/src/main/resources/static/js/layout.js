class AppHeader extends HTMLElement {
    async connectedCallback() {
        try {
            const response = await fetch('/include/header.html');
            if (response.ok) {
                this.innerHTML = await response.text();
            //헤더 추가 후 header-loaded라는 사용자가 만든 이벤트를 발생시킴
            //버블링을 적용하여 header태그 뿐 아니라 document까지 이벤트가 도달하게 함
            this.dispatchEvent(new CustomEvent('header-loaded', { bubbles: true }));
            }
        } catch (error) {
         this.innerHTML = '<h1>헤더 불러오기에 실패했습니다.</h1>';
            console.error('헤더 로드 실패:', error);
        }
    }
}

customElements.define('app-header', AppHeader);

let roleUsers;
let roleGuests;

document.addEventListener("header-loaded", async e=>{
   roleUsers = document.querySelectorAll(".role-user");
   roleGuests = document.querySelectorAll(".role-guest");
   
   const info = await getMyInfo();
   if(!info || !info.username){
      console.log("게스트")
      showByRole("GUEST");
   }
   else{
      showByRole("USER");
   }
})

async function showByRole(role){
   
   switch(role){
      case "USER": showUser(); break;
      default: showGuest();
   }
   
}

function showUser(){
   show(roleUsers)
   hide(roleGuests);
}
function showGuest(){
   show(roleGuests)
   hide(roleUsers)
}

function hide(elements){
   elements.forEach(element => element.style.display = "none");
}
function show(elements){
   elements.forEach(element => element.style.display = "");
}

/* ============================
* 로그아웃 함수                   *
==============================*/
async function logout(e){
   e.preventDefault();
   //사원증 제거
   localStorage.removeItem("accessToken");
   //메인페이지로 이동
   location.href="/";
}