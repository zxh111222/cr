import {defineStore} from 'pinia'
import {ref} from 'vue'

// 这里演示另一种写法；工作中不同的项目可能用到不同的Vue版本，不同写法都接触接触，混个眼熟
export const useUserStore = defineStore('user', () => {
  const id = ref('')
  const mobile = ref('')
  const token = ref('')

  const setUserInfo = (userInfo) => {
    id.value = userInfo.id
    mobile.value = userInfo.mobile
    token.value = userInfo.token
  }

  const clearUserInfo = () => {
    id.value = ''
    mobile.value = ''
    token.value = ''
  }

  return {
    id,
    mobile,
    token,
    setUserInfo,
    clearUserInfo
  }
})
