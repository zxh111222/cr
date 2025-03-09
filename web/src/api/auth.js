import http from './http'

export function sendCode(mobile) {
  return http.post('/user/send-code', {mobile})
}
