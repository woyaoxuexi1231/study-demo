<template>
  <div class="login-page">
    <div class="form">
      <!-- 注册表单 -->
      <form v-show="isRegister" @submit.prevent="handleRegister" class="register-form">
        <input v-model="registerName" type="text" placeholder="name" required/>
        <input v-model="registerPassword" type="password" placeholder="password" required/>
        <input v-model="registerEmail" type="text" placeholder="email address" required/>
        <button type="submit">create</button>
        <p class="message">
          Already registered?
          <a href="#" @click.prevent="showLogin">Sign In</a>
        </p>
      </form>

      <!-- 登录表单 -->
      <form v-show="isLogin" @submit.prevent="handleLogin" class="login-form">
        <input v-model="loginUsername" type="text" placeholder="username" required/>
        <input v-model="loginPassword" type="password" placeholder="password" required/>
        <button type="submit">login</button>
        <p class="message">
          Not registered?
          <a href="#" @click.prevent="showRegister">Create an account</a>
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

// 响应式数据
const isLogin = ref(true);
const isRegister = ref(false);

const loginUsername = ref('');
const loginPassword = ref('');

const registerName = ref('');
const registerEmail = ref('');
const registerPassword = ref('');

// 切换到登录表单
const showLogin = () => {
  isLogin.value = true;
  isRegister.value = false;
};

// 切换到注册表单
const showRegister = () => {
  isLogin.value = false;
  isRegister.value = true;
};


const errorMessage = ref('')
// 处理登录请求
const handleLogin = async () => {
  errorMessage.value = ''

  try {
    const params = new URLSearchParams()
    params.append('username', loginUsername.value)
    params.append('password', loginPassword.value)

    console.info(loginUsername.value)
    console.info(loginPassword.value)

    const response = await axios.post('http://localhost:19999/login', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      // 要携带 cookie/session
      withCredentials: true
    })

    console.log(response)

    // 登录成功，可以跳转到首页或其他页面
    window.location.href = '/'
  } catch (error) {
    errorMessage.value = '登录失败，请检查用户名或密码'
  }
}

// 处理注册请求
const handleRegister = async () => {
  const { registerName, registerEmail, registerPassword } = this; // ❌ 错误写法，下面修正

  // ✅ 正确的写法：使用 ref() 的 .value 获取值
  try {
    const response = await axios.post('http://localhost:8080/api/auth/register', {
      name: registerName.value,
      email: registerEmail.value,
      password: registerPassword.value,
    });

    console.log('注册成功:', response.data);
    alert('注册成功，请登录！');
    showLogin(); // 切换到登录页
  } catch (error) {
    console.error(
        '注册失败:',
        error.response ? error.response.data : error.message
    );
    alert('注册失败，请稍后再试');
  }
};
</script>

<style scoped>
/* 这里粘贴你原来的 CSS 样式 */
@import url(https://fonts.googleapis.com/css?family=Roboto:300);

.login-page {
  width: 360px;
  padding: 8% 0 0;
  margin: auto;
}

.form {
  position: relative;
  z-index: 1;
  background: #FFFFFF;
  max-width: 360px;
  margin: 0 auto 100px;
  padding: 45px;
  text-align: center;
  box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);
}

.form input {
  font-family: "Roboto", sans-serif;
  outline: 0;
  background: #f2f2f2;
  width: 100%;
  border: 0;
  margin: 0 0 15px;
  padding: 15px;
  box-sizing: border-box;
  font-size: 14px;
}

.form button {
  font-family: "Roboto", sans-serif;
  text-transform: uppercase;
  outline: 0;
  background: #4CAF50;
  width: 100%;
  border: 0;
  padding: 15px;
  color: #FFFFFF;
  font-size: 14px;
  -webkit-transition: all 0.3 ease;
  transition: all 0.3 ease;
  cursor: pointer;
}

.form button:hover,
.form button:active,
.form button:focus {
  background: #43A047;
}

.form .message {
  margin: 15px 0 0;
  color: #b3b3b3;
  font-size: 12px;
}

.form .message a {
  color: #4CAF50;
  text-decoration: none;
}

body {
  background: #76b852; /* fallback for old browsers */
  background: rgb(141, 194, 111);
  background: linear-gradient(90deg, rgba(141, 194, 111, 1) 0%, rgba(118, 184, 82, 1) 50%);
  font-family: "Roboto", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>