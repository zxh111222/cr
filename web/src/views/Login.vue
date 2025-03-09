<script setup>
import {ref} from 'vue'
import {ElMessage} from 'element-plus'

const mobile = ref('')
const code = ref('')

const handleGetCode = () => {
  if (!mobile.value || mobile.value.length !== 11) {
    ElMessage.error('请输入正确的手机号')
    return
  }
  ElMessage.success('验证码已发送')
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

  ElMessage.success('登录成功（查看浏览器的控制台输出）')
  console.log('用户输入的手机号:', mobile.value)
  console.log('用户输入的验证码:', code.value)
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="header">
        <img src="@/assets/logo.svg" alt="Logo" class="logo">
        <h1 class="title">bzbx APP</h1>
      </div>

      <el-form class="login-form">
        <el-form-item>
          <el-input
            v-model="mobile"
            placeholder="18012345678"
            maxlength="11"
            :clearable="true"
          />
        </el-form-item>

        <el-form-item>
          <div class="verification-code">
            <el-input
              v-model="code"
              placeholder="请输入验证码"
              class="code-input"
            />
            <el-button
              type="primary"
              class="get-code-btn"
              @click="handleGetCode"
            >
              获取验证码
            </el-button>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            @click="handleLogin"
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

.verification-code {
  display: flex;
  gap: 10px;
}

.code-input {
  flex: 1;
}

.get-code-btn {
  width: 120px;
}

.login-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

:deep(.el-input__inner) {
  height: 40px;
}
</style>
