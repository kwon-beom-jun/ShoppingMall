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

  return response;
}

export function logout() {
  localStorage.removeItem('jwtToken');
  localStorage.removeItem('userRole');
  // 로그아웃 후 페이지 리디렉션
}
