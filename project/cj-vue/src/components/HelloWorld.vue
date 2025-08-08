<template>
  <div class="login-page">
    <div class="form">
      <!-- 注册表单 -->
      <form v-show="isRegister" @submit.prevent="handleRegister" class="register-form">
        <input v-model="registerName" type="text" placeholder="name" required />
        <input v-model="registerPassword" type="password" placeholder="password" required />
        <input v-model="registerEmail" type="text" placeholder="email address" required />
        <button type="submit">create</button>
        <p class="message">
          Already registered?
          <a href="#" @click.prevent="showLogin">Sign In</a>
        </p>
      </form>

      <!-- 登录表单 -->
      <form v-show="isLogin" @submit.prevent="handleLogin" class="login-form">
        <input v-model="loginUsername" type="text" placeholder="username" required />
        <input v-model="loginPassword" type="password" placeholder="password" required />
        <button type="submit">login</button>
        <p class="message">
          Not registered?
          <a href="#" @click.prevent="showRegister">Create an account</a>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AuthForm',
  data() {
    return {
      isLogin: true, // 默认显示登录
      isRegister: false,
      // 登录表单数据
      loginUsername: '',
      loginPassword: '',
      // 注册表单数据
      registerName: '',
      registerEmail: '',
      registerPassword: '',
    };
  },
  methods: {
    showLogin() {
      this.isLogin = true;
      this.isRegister = false;
    },
    showRegister() {
      this.isLogin = false;
      this.isRegister = true;
    },
    handleLogin() {
      // 处理登录逻辑，比如调用API
      console.log('Login:', {
        username: this.loginUsername,
        password: this.loginPassword,
      });
      // 这里你可以调用登录API，然后进行路由跳转等操作
    },
    handleRegister() {
      // 处理注册逻辑，比如调用API
      console.log('Register:', {
        name: this.registerName,
        email: this.registerEmail,
        password: this.registerPassword,
      });
      // 这里你可以调用注册API
    },
  },
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
  background: rgb(141,194,111);
  background: linear-gradient(90deg, rgba(141,194,111,1) 0%, rgba(118,184,82,1) 50%);
  font-family: "Roboto", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>