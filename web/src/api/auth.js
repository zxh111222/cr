import http from './http'

const MODULE_PREFIX = '/u'

export function sendCode(mobile) {
  return http.post(MODULE_PREFIX + '/user/send-code', {mobile})
}

export function login(mobile, code) {
  return http.post(MODULE_PREFIX + '/user/login', {mobile, code})
}

export function ping() {
  return http.get(MODULE_PREFIX + '/user/ping')
}
