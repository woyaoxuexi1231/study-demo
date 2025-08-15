```
src/
├── assets/               # 静态资源
├── components/           # 公共组件
│   ├── Header.vue        # 导航栏
│   ├── TodoList.vue      # 待办事项列表
│   ├── UserCard.vue      # 用户卡片
│   └── Modal.vue         # 模态框组件
├── composables/          # 组合式函数
│   └── useFetch.js       # 数据获取逻辑
├── router/               # 路由配置
│   └── index.js
├── store/                # Pinia 状态管理
│   └── counter.js
├── views/                # 页面级组件
│   ├── Home.vue          # 首页
│   ├── About.vue         # 关于页
│   ├── Todos.vue         # 待办事项页
│   └── Users.vue         # 用户列表页
├── App.vue               # 根组件
├── main.js               # 入口文件
└── styles/               # 全局样式
    └── main.css

```