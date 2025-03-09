<script setup>
import {computed, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {sendCode, login} from "@/api/auth.js";

const mobile = ref('18012345678')
const code = ref('')

const loading = ref(false)

const isLoginDisabled = computed(() => {
  return code.value.length < 4 || !code.value
})

const handleGetCode = () => {
  if (!mobile.value || mobile.value.length !== 11) {
    ElMessage.error('请输入正确的手机号')
    return
  }
  sendCode(mobile.value)
    .then((res) => {
    if (res.code === 200) {
      ElMessage.success('验证码已发送')
    } else {
      ElMessage.error(res.msg || '发送验证码失败')
    }
  })
    .catch((error) => {
      ElMessage.error(error.response?.data?.msg || '发送验证码失败')
    })
}

const handleLogin = () => {
  if (!mobile.value || mobile.value.length !== 11) {
    ElMessage.error('请输入正确的手机号')
    return
  }
  if (!code.value || code.value.length < 4) {
    ElMessage.error('请输入有效的验证码')
    return
  }

  loading.value = true
  login(mobile.value, code.value)
    .then((res) => {
      if (res.code === 200) {
        ElMessage.success('登录成功')
      } else {
        ElMessage.error(res.msg || '登录失败')
      }
    })
    .catch((error) => {
      ElMessage.error(error.response?.data?.msg || '登录失败')
    })
    .finally(() => {
      loading.value = false
    })
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="header">
        <img src="@/assets/logo.svg" alt="Logo" class="logo">
        <h1 class="title">cr APP</h1>
      </div>

      <el-form class="login-form">
        <el-form-item>
          <el-input
            size="large"
            v-model="mobile"
            placeholder="18012345678"
            maxlength="11"
            :clearable="true"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="code"
            placeholder="请输入验证码"
          >
            <template #append>
              <el-button
                type="primary"
                size="large"
                class="get-code-btn"
                :style="{ color: 'var(--el-color-primary)' }"
                @click="handleGetCode"
              >
                获取验证码
              </el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            @click="handleLogin"
            :disabled="isLoginDisabled"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 50px;
  height: 50px;
  margin-bottom: 16px;
}

.title {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.login-form {
  width: 100%;
}

.get-code-btn {
  width: 120px;
  background-color: transparent !important;
}

.login-btn {
  width: 100%;
  font-size: 16px;
}
</style>
