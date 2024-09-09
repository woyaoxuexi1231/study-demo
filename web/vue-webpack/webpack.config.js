const path = require('path');
const {VueLoaderPlugin} = require('vue-loader')
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const config = {

    mode: "development",
    entry: './src/main.js',

    // 配置输出文件的名称,这些输出文件将写入到 output.path 选项指定的目录下
    output: {
        // 构建后的文件名
        // 对于单个入口的应用,filename可以指定为固定的值,多个入口的项目需要借助占位符 [name], filename: [name].js
        // 具体的是在 entry 配置多个不同的入口,不同入口的名字会通过 [name] 占位符替换
        filename: "[name].[hash8].js",
        // 构建后文件的存放目录
        path: path.resolve(__dirname, 'build')
        // publicPath 该选项把输出目录配置为浏览器环境下的URL地址，默认是空字符串，使用相对路径
    },

    // plugin用于扩展webpack的功能
    plugins: [
        // 实例化 MiniCssPlugin
        new VueLoaderPlugin(),
        new HtmlWebpackPlugin({
            // 引用chunks数组
            chunks: ['main'],
            // 构建后的文件名
            filename: "index.html",
            // 源文件名
            template: "index.html"
        })
    ],

    module: {
        rules: [
            {
                test: /\.vue$/,
                use: ['vue-loader'],
            },
            {
                // 以gif/png/jpg结尾的文件
                test: /\.css$/,
                use: [process.env.NODE_ENV === 'production' ? MiniCssExtractPlugin.loader : 'vue-style-loader', 'css-loader', 'postcss-loader']
            },
            {
                test: /\.js$/,
                use: 'babel-loader',
            }
        ]
    },


    // 为了提升调试和开发的效率,不需要改一次代码就npm run一次,webpack也有解决方案
    // Webpack提供webpack-dev-server,这个会启动一个http服务器和一个webpack,通过websocket协议告知浏览器更新文件,以实现实时预览
    // 这种技术也被称为LiveReload技术
    // 开发服务器配置
    devServer: {
        // 模块热替换功能,启动模块热替换之后,只会替换变更的模块,开发单页应用(VUE,REACT)时方便
        hot: false,
        // URL的根目录, devServer的文档根目录
        // contentBase不再推荐了
        static: {
            // path.resolve()把路径解析为绝对路径, path.join()把路径按照字符串拼接的方式连接起来
            directory: path.resolve(__dirname, 'build'),
        },
        // devServer的http服务端口
        port: 8081
    },

    // 通过resolve选项我们可以定义webpack的解析规则
    resolve: {
        // 指定导入语句中省略文件扩展名时,webpack需要尝试自动解析的文件扩展名列表
        extensions: ['.js', '.vue', '.json']
    }
}

if (process.env.NODE_ENV === 'production') {
    // 在生产环境下添加 css 提取插件
    config.plugins.push(new MiniCssExtractPlugin({
        filename: '[name].css'
    }))
}

module.exports = config;

