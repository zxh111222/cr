import axios from 'axios'
import {useUserStore} from "@/stores/user.js";

const http = axios.create({
  baseURL: 'http://localhost:8081',
  timeout: 5000
})

// Request interceptor
http.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token){
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
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
