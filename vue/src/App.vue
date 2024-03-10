<!--
</template> 标签是 Vue.js 单文件组件中的一个标记，用于定义组件的模板部分结束。
在 Vue.js 的单文件组件中，模板部分通常用 <template> 标签包裹，用于定义组件的结构、布局和内容。
而 </template> 标签则表示模板的结束位置，即在这之后是组件的其他部分，如 JavaScript 代码和样式。
-->
<template>
  <div>
    <el-container>
      <el-aside width="200px">
        <el-row class="tac">
          <el-col>
            <!--            <h5>默认颜色</h5>-->
            <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose">
              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-location"></i>
                  <span>导航一</span>
                </template>
                <el-menu-item-group>
                  <template slot="title">分组一</template>
                  <el-menu-item index="1-1">选项1</el-menu-item>
                  <el-menu-item index="1-2">选项2</el-menu-item>
                </el-menu-item-group>
                <el-menu-item-group title="分组2">
                  <el-menu-item index="1-3">选项3</el-menu-item>
                </el-menu-item-group>
                <el-submenu index="1-4">
                  <template slot="title">选项4</template>
                  <el-menu-item index="1-4-1">选项1</el-menu-item>
                </el-submenu>
              </el-submenu>
              <el-menu-item index="2">
                <i class="el-icon-menu"></i>
                <router-link to="/employeeTable">EmployeeTable</router-link>
              </el-menu-item>
              <el-menu-item index="3" disabled>
                <i class="el-icon-document"></i>
                <span>不可用</span>
              </el-menu-item>
              <el-menu-item index="4">
                <i class="el-icon-setting"></i>
                <router-link to="/Calendar">Calendar</router-link>
              </el-menu-item>
              <el-menu-item index="5">
                <i class="el-icon-menu"></i>
                <router-link to="/progress">progress</router-link>
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-aside>
      <el-container>
        <el-header>
          <img style="width: 60px; height: 60px" src="./assets/logo.png">
        </el-header>
        <el-main>
          <!-- <router-link> 是 Vue Router 提供的用于创建页面导航链接的组件。-->
          <!-- 创建一个链接，当用户点击这个链接时，Vue Router 会导航到指定的路径 “/employeeTable” -->
          <!-- <router-link to="/progress">Progress</router-link>-->
          <!-- 这是 Vue Router 提供的组件，用于渲染当前路径对应的组件。它的作用是将页面内容动态加载到这个位置，实现单页面应用的页面切换效果。 -->
          <router-view/>
        </el-main>
        <el-footer>
          © 2024 154347188@qq.com
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.el-header {
  background-color: #ffffff;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-footer {
  background-color: #ffffff;
  color: #333;
  text-align: right;
  line-height: 60px;
}

.el-aside {
  background-color: #ffffff;
  color: #333;
  /*text-align: center;*/
  line-height: 200px;
}

.el-main {
  background-color: #ffffff;
  color: #333;
  /*text-align: center;*/
  line-height: 10px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
