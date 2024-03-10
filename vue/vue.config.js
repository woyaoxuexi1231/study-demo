// 引入了 @vue/cli-service 模块中的 defineConfig 方法，这个方法用于定义 Vue 项目的配置。
const {defineConfig} = require('@vue/cli-service')
// 这里使用 module.exports 导出一个对象，这个对象包含了 Vue 项目的配置信息。defineConfig 方法接受一个配置对象作为参数，该对象用于配置 Vue 项目的各种选项。
module.exports = defineConfig({
    // 这个选项表示是否对依赖进行转译。当设置为 true 时，Vue CLI 会通过 Babel 对项目依赖进行转译。这对于需要兼容旧版浏览器的项目非常有用。
    transpileDependencies: true,
    devServer: {
        port: 10099 // 你想要使用的端口号
    }
})
