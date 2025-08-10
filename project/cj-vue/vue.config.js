const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})


module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:19999', // 后端地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': '', // 去掉 /api 前缀
        },
        // 以下配置让代理请求携带 Cookie
        cookieDomainRewrite: {
          "*": "", // 可选，根据需要调整
        },
        secure: false,
      },
    },
  },
};