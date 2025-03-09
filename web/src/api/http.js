import axios from 'axios'

const http = axios.create({
  baseURL: 'http://localhost:8081',
  timeout: 5000
})

// Request interceptor
http.interceptors.request.use(
  config => {
    console.log('请求：', config)
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// Response interceptor
http.interceptors.response.use(
  response => {
    console.log('响应：', response)
    return response.data
  },
  error => {
    console.log('响应错误：', error)
    return Promise.reject(error)
  }
)

export default http
