'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

module.exports = {

    dev: {

        // Paths
        assetsSubDirectory: 'static',
        assetsPublicPath: '/',

        // 配置代理规则后，前端代码中的跨域请求会被代理服务器拦截并转发到实际处理请求的服务器，从而绕过了浏览器的同源策略，实现了跨域请求。
        // proxyTable: {
        //     // 这是一个匹配规则，表示所有以 /api 开头的请求都会被代理到目标服务器。
        //     '/api': {
        //         // 这是要代理到的目标服务器地址，即实际处理请求的服务器地址。
        //         target: 'http://localhost:10001', // 要请求的目标服务器地址
        //         // 这是一个布尔值，表示是否改变请求头中的 Origin 字段为目标服务器的地址。
        //         changeOrigin: true,
        //         // 这是一个对象，用于重写请求路径。
        //         pathRewrite: {
        //             // 这是一个规则，表示将请求路径中以 /api 开头的部分替换为空字符串，即将 /api 移除。
        //             // 这是一个正则表达式，表示匹配请求路径的开头，即路径中以 /api 开头的部分。
        //             '^/api': '', // 将请求路径中的 '/api' 替换为空字符串
        //         },
        //     },
        // },

        proxyTable: {
            '/api': {
                target: 'http://localhost:10001',// 设置你调用的接口域名和端口号
                //secure: false, // 如果是https接口，需要配置这个参数
                changeOrigin: true, //是否跨域
                // pathRewrite: {
                //     '^/api': '', // 将请求路径中的 '/api' 替换为空字符串
                // }
            }
        },

        // Various Dev Server settings
        host: 'localhost', // can be overwritten by process.env.HOST
        port: 10099, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
        autoOpenBrowser: false,
        errorOverlay: true,
        notifyOnErrors: true,
        poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-


        /**
         * Source Maps
         */

        // https://webpack.js.org/configuration/devtool/#development
        devtool: 'cheap-module-eval-source-map',

        // If you have problems debugging vue-files in devtools,
        // set this to false - it *may* help
        // https://vue-loader.vuejs.org/en/options.html#cachebusting
        cacheBusting: true,

        cssSourceMap: true
    },


    build: {
        // Template for index.html
        index: path.resolve(__dirname, '../dist/index.html'),

        // Paths
        assetsRoot: path.resolve(__dirname, '../dist'),
        assetsSubDirectory: 'static',
        assetsPublicPath: '/',

        proxyTable: {
            '/api': {
                //target: 'http://127.0.0.1:3001',// 设置你调用的接口域名和端口号
                //secure: false, // 如果是https接口，需要配置这个参数
                changeOrigin: true, //是否跨域
                pathRewrite: {
                    '^/api': '', // 将请求路径中的 '/api' 替换为空字符串
                }
            }
        },
        /**
         * Source Maps
         */

        productionSourceMap: true,
        // https://webpack.js.org/configuration/devtool/#production
        devtool: '#source-map',

        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: false,
        productionGzipExtensions: ['js', 'css'],

        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report
    }
}
