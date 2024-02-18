import axios from 'axios';

export async function login(email, password) {
  const formData = new FormData();
  formData.append('email', email);
  formData.append('password', password);

  const response = await axios.post('/api/members/login', formData);
  // const response = await axios.post('http://localhost/members/login', formData);
  
  const { token, role } = response.data;

  localStorage.setItem('jwtToken', token);
  localStorage.setItem('userRole', role);

  console.log(role);

  return response;
}

export function logout() {
  localStorage.removeItem('jwtToken');
  localStorage.removeItem('userRole');
}

export async function authAdminCheck() {
  var result = false;
  // SecurityConfig에서 설정해두었음
  // '/admin' 경로 시 백엔드에서 Admin 유저인지 확인하여 아니면 403 에러 보냄
  await axios.get('/api/admin/item/new')
    .then(() => {
      result = true;
    })
    .catch(() => {
      alert("관리자 페이지입니다.");
    });
    return result;
}