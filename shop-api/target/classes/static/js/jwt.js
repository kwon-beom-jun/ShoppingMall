
document.addEventListener('DOMContentLoaded', function() {
    // 로컬 스토리지에서 JWT 토큰 확인
    var jwtToken = localStorage.getItem('jwtToken');

    if (jwtToken) {
        // 토큰이 존재할 경우, 필요한 추가 처리 수행
        console.log('JWT 토큰이 존재합니다');
        // 필요한 추가 처리...
    } else {
        console.log('JWT 토큰이 존재하지 않습니다.');
        // 로그인 페이지로 리디렉션 등의 조치를 취할 수 있습니다.
    }
});


// 토큰 생성(로그인)
async function login() {

    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var formData = new FormData();
    formData.append("email", email);
    formData.append("password", password);

    // CSRF 미사용
//    getCsrfToken();
//    const csrfToken = localStorage.getItem('csrfToken');
//    formData.append("_csrf", csrfToken);

    axios.post('/members/login', formData)
        .then(function(response) {
            var token = response.data.token;
            var role = response.data.role; // 서버로부터 받은 사용자 권한
            console.log('로그인 성공. 토큰: ', token, ', 역할: ', role);
            saveTokenAndRoleAndRedirect(token, role); // 토큰과 역할 저장
        })
        .catch(function(error) {
            if (error.response && error.response.data) {
                // 서버에서 보낸 에러 메시지를 받아 HTML에 표시
                var errorMsg = error.response.data.error;
                document.getElementById('loginErrorMsg').innerText = errorMsg;
            } else {
                console.error('로그인 실패: ', error);
            }
        });
}


// 토큰 제거(로그아웃)
document.addEventListener("DOMContentLoaded", function() {
    // 토큰 제거(로그아웃) 코드
    document.getElementById('logoutLink').addEventListener('click', function() {
        // 팝업을 통해 사용자에게 확인 받기
        var confirmLogout = confirm("로그아웃 하시겠습니까?");
        if (confirmLogout) {
            // localStorage에서 JWT 토큰 제거
            localStorage.removeItem('jwtToken');
            // 홈 페이지로 리다이렉트
            window.location.href = '/';
        }
    });
});


// 로그인 성공 후 JWT 토큰과 사용자 권한을 로컬 스토리지에 저장
function saveTokenAndRoleAndRedirect(token, role) {
    localStorage.setItem('jwtToken', token);
    localStorage.setItem('userRole', role); // 사용자 권한 저장
    window.location.href = '/';
}

