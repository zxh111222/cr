import http from './http'

export function sendCode(mobile) {
  return http.post('/user/send-code', {mobile})
}

export function login(mobile, code) {
  return http.post('/user/login', {mobile, code})
}

export function ping() {
  return http.get('/user/ping')
}
