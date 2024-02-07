function getCsrfToken() {
    axios.get('/csrf')
        .then(function (response) {
            const csrfToken = response.data.token;
            localStorage.setItem('csrfToken', csrfToken);
        })
        .catch(function (error) {
            console.error('CSRF 토큰 요청 실패: ', error);
        });
}