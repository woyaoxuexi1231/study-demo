const path = require('path');
// 导入css打包的插件
const MiniCssPlugin = require('mini-css-extract-plugin');
// 导入HtmlWebpackPlugin
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {

    // 构建模式设置为开发模式
    // mode一共三个模式 production(默认,启用一堆优化插件) development(启用NamedChunksPlugin和NamedModulesPlugin) none(无任何优化)
    /* */
    mode: "development",

    // 入口文件, Webpack通过Entry入口来开始应用的构建
    // Context是包含入口文件的目录，必须使用绝对路径。不配置Context的情况下，在哪个目录执行Webpack命令，Webpack就会从该目录进行入口文件的搜索。

    // 第一种配置方式
    // main.js相对应用入口目录src的路径为./main.js
    // entry: './main.js',
    // 指定src目录为应用入口目录
    // context: path.resolve(__dirname, 'src'),

    // 第二种配置方式
    entry: './src/main.js',

    // 配置输出文件的名称,这些输出文件将写入到 output.path 选项指定的目录下
    output: {
        // 构建后的文件名
        // 对于单个入口的应用,filename可以指定为固定的值,多个入口的项目需要借助占位符 [name], filename: [name].js
        // 具体的是在 entry 配置多个不同的入口,不同入口的名字会通过 [name] 占位符替换
        filename: "bundle.js",
        // 构建后文件的存放目录
        path: path.resolve(__dirname, './build')
        // publicPath 该选项把输出目录配置为浏览器环境下的URL地址，默认是空字符串，使用相对路径
    },

    // plugin用于扩展webpack的功能
    plugins: [
        // 实例化 MiniCssPlugin
        new MiniCssPlugin({
            // 输出的css文件名 todo 输出的文件只有一个 main.css
            filename: 'css/[name].css',
        }),
        new HtmlWebpackPlugin({
            // 引用chunks数组
            chunks: ['main'],
            // 构建后的文件名
            filename: "index-pack.html",
            // 源文件名
            template: "index-pack.html"
        })
    ],

    module: {
        // 用配置模块的读取和解析规则,这些规则能够修改模块的创建方式,对模块运用在loader或者修改解析器
        // 1. 匹配条件: test,include,exclude来指定要应用或者要排除的文件
        // 2. loader列表: 对匹配成功的文件运用执行loader,也可以传递数组,多个loader的运用顺序是从后往前应用的
        //               比如 use: ['style-loader', 'css-loader'], 那么执行顺序是 css-loader->style-loader
        rules: [
            // loader主要职责就是进行文件转换,通过配置 module.rules 数组,告诉webpack遇到哪些文件使用哪些loader进行加载和转换
            // 1. loader的顺序是从后到前的
            // 2. loader需要的选项可以通过标准的JavaScript对象的形式传入
            // 3. 不同的loader支持的选项是不同的,需要查阅loader对应的文档
            {
                // 以css结尾的文件应用的loader
                test: /\.css$/,

                // 第一种方式, 使用 css-loader 和 style-loader
                // css-loader完成CSS文件到JavaScript模块的转换
                // 而style-loader则将JavaScript模块中的CSS样式内容通过DOM操作写入到HTML页面的style节点中
                // 如果需要单独打包CSS文件，则需要配合Plugin
                // 这里没有配置css文件具体输出的目录
                // use: ['style-loader', 'css-loader']

                // 第二种方式, 把 style-loader 替换成 MiniCssPlugin 提供的 loader
                use: [MiniCssPlugin.loader, 'css-loader'],
            },
            {
                // 以gif/png/jpg结尾的文件
                test: /\.(png|jpg|gif)$/,
                use: [
                    {
                        // 使用file-loader进行加载
                        loader: 'file-loader',
                        options: {
                            // 构建文件名和目录设置, name,ext 是两个占位符
                            name: 'images/[name].[ext]',
                            // 是否启动ES6模板系统,这里未启用
                            esModule: false,
                            // 输出的根目录
                            publicPath: 'build'
                        }
                    }
                ]
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

    }
}