# vue2-demo

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).



## 文件结构

```txt
my-vue2-project/
├── public/                  # 静态资源目录（无需 webpack 处理）
│   ├── index.html           # 主入口 HTML 文件
│   └── favicon.ico          # 网站图标
├── src/                     # 源代码目录
│   ├── assets/              # 静态资源（图片、样式、字体等）
│   │   ├── images/          # 图片文件
│   │   └── styles/          # 全局样式文件（如 reset.css）
│   ├── components/          # 通用组件（可复用）
│   │   ├── Button.vue       # 示例组件
│   │   └── Header.vue       # 示例组件
│   ├── views/               # 页面级组件（路由对应）
│   │   ├── Home.vue         # 首页组件
│   │   └── About.vue        # 关于页组件
│   ├── router/              # 路由配置
│   │   └── index.js         # Vue Router 配置
│   ├── store/               # 状态管理（Vuex）
│   │   ├── modules/         # 模块化状态
│   │   └── index.js         # Vuex 主入口
│   ├── api/                 # 接口请求封装（可选）
│   │   └── http.js          # Axios 封装示例
│   ├── utils/               # 工具函数
│   │   └── helpers.js       # 辅助函数示例
│   ├── App.vue              # 根组件
│   └── main.js              # 项目入口文件
├── .env                     # 环境变量配置
├── babel.config.js          # Babel 转译器的配置文件，用于将现代 JavaScript 转换为向后兼容版本
├── vue.config.js            # Vue 项目的可选配置文件，可以配置 webpack、devServer 等
├── jsconfig.json            # JavaScript 项目的配置文件，用于提供代码编辑器的智能提示支持
├── package.json             # 项目依赖和脚本，项目名称、版本、描述等元信息、项目依赖(dependencies)和开发依赖(devDependencies)、可运行的脚本命令(scripts)，如 serve, build, lint 等
└── README.md                # 项目说明文档
```

