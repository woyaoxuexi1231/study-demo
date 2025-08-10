# Vue2 项目

## 首先确保已安装 Node.js (建议版本 12.x 或 14.x) 和 npm

``` 
# 需要安装好 node.js, 这里版本是 18.20.5
node -v
v18.20.5
```

## 安装 Vue CLI

```
# 安装 Vue CLI  Vue2 项目通常使用 Vue CLI 创建
npm install -g @vue/cli
# 或者
yarn global add @vue/cli

# 验证安装
vue --version
```

## 创建 Vue2 项目

```
vue create vue2-demo
```

在交互式命令行中：

1. 选择 "Manually select features"
2. 选择需要的功能 (Babel, Router, Vuex, CSS Pre-processors 等)
3. 选择 Vue 版本 2.x
4. 选择路由模式 (建议选 history)
5. 选择 CSS 预处理器 (如 Sass/SCSS)
6. 选择 ESLint 配置 (如 Standard)
7. 选择何时进行 lint (建议选 "Lint on save")
8. 选择配置文件存放位置 (建议选 "In dedicated config files")
9. 是否保存为预设 (可选)

## 项目结构说明

``` tex
vue2-demo/
├── node_modules/     # 依赖包
├── public/           # 静态文件
│   ├── index.html    # 主HTML文件
│   └── favicon.ico   # 网站图标
├── src/              # 源代码
│   ├── assets/       # 静态资源
│   ├── components/   # 组件
│   ├── router/       # 路由配置
│   ├── store/        # Vuex状态管理
│   ├── views/        # 页面视图
│   ├── App.vue       # 根组件
│   └── main.js       # 应用入口
├── .gitignore        # Git忽略配置
├── babel.config.js   # Babel配置
├── package.json      # 项目配置
└── README.md         # 项目说明
```



# Vue3 项目

#### 方法一：使用 Vue CLI

```
# 同上，装好 node 和 vue 脚手架
vue create vue3-demo

# 然后选择 vue3 版本的就可以了
```

#### 方法二：使用 Vite (推荐)

```
npm create vite@latest vue3-demo-vite --template vue

yarn create vite vue3-demo-vite --template vue
```

### 项目结构说明 (Vite 创建)

```txt
vue3-demo/
├── node_modules/
├── public/           # 静态文件
├── src/              # 源代码
│   ├── assets/       # 静态资源
│   ├── components/   # 组件
│   ├── App.vue       # 根组件
│   └── main.js       # 应用入口 (使用createApp)
├── .gitignore
├── index.html        # 主HTML文件 (Vite中在根目录)
├── package.json
├── vite.config.js    # Vite配置文件
└── README.md
```



