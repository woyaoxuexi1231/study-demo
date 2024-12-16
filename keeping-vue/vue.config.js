const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // lintOnSave: false
  runtimeCompiler: true,

  devServer:{
    port: 10099,
    proxy: {
      '/api': {
        target: 'http://localhost:10001', //API服务器的地址
        ws: true, //代理 websockets
        changeOrigin: true, // 是否跨域，虚拟的站点需要更管origin
        pathRewrite: {
          // '^/api'是一个正则表达式，表示要匹配请求的url中，全部'http://localhost:8081/api' 转接为 http://localhost:8081/
          '^/api': '',
        }
      }
    },
  }
})
