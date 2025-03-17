import {defineStore} from 'pinia'
import {ref} from 'vue'

// 这里演示另一种写法；工作中不同的项目可能用到不同的Vue版本，不同写法都接触接触，混个眼熟
export const useUserStore = defineStore('user', () => {
  const id = ref(localStorage.getItem('userId') || '')
  const mobile = ref(localStorage.getItem('userMobile') || '')
  const token = ref(localStorage.getItem('userToken') || '')

  const setUserInfo = (userInfo) => {
    id.value = userInfo.id
    mobile.value = userInfo.mobile
    token.value = userInfo.token

    localStorage.setItem('userId', userInfo.id)
    localStorage.setItem('userMobile', userInfo.mobile)
    localStorage.setItem('userToken', userInfo.token)
  }

  const clearUserInfo = () => {
    id.value = ''
    mobile.value = ''
    token.value = ''

    localStorage.removeItem('userId')
    localStorage.removeItem('userMobile')
    localStorage.removeItem('userToken')
  }

  return {
    id,
    mobile,
    token,
    setUserInfo,
    clearUserInfo
  }
})
