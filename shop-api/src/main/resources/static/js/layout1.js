
// 다국어 쿠키 저장
function setLang(lang) {
    var currentUrl = window.location.href;
    var newUrl;

    if (currentUrl.includes('?')) {
        newUrl = currentUrl.includes('lang=') ? currentUrl.replace(/(lang=)[^\&]+/, '$1' + lang) : currentUrl + '&lang=' + lang;
    } else {
        newUrl = currentUrl + '?lang=' + lang;
    }
    // LocaleChangeInterceptor
    window.location.href = newUrl;
    // 콘솔에 현재 쿠키 값 출력
    console.log(window.document.cookie);
}


// 페이지 로드 시 메뉴 아이템 표시 조정
window.onload = function() {
    var token = localStorage.getItem('jwtToken');
    var role = localStorage.getItem('userRole');

    console.log(role);

    if (token) {
        document.getElementById('loginItem').style.display = 'none';
        document.getElementById('joinItem').style.display = 'none';
        document.getElementById('logoutItem').style.display = 'block';

        if (role === 'ROLE_ADMIN') {
            // 어드민 권한인 경우
            document.querySelectorAll('.admin-item').forEach(function(item) {
                item.style.display = 'block';
            });
        } else {
            // 일반 유저인 경우
            document.querySelectorAll('.user-item').forEach(function(item) {
                item.style.display = 'block';
            });
        }
    } else {
        // 로그인하지 않은 경우
        document.getElementById('loginItem').style.display = 'block';
        document.getElementById('joinItem').style.display = 'block';
        document.getElementById('logoutItem').style.display = 'none';
    }
};
