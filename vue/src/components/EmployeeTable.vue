<template>
  <div>
    <el-table :data="employeeList" style="width: 100%">
      <el-table-column prop="employeeNumber" label="Employee Number"></el-table-column>
      <el-table-column prop="lastName" label="Last Name"></el-table-column>
      <el-table-column prop="firstName" label="First Name"></el-table-column>
      <el-table-column prop="extension" label="Extension"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
      <el-table-column prop="officeCode" label="Office Code"></el-table-column>
      <el-table-column prop="reportsTo" label="Reports To"></el-table-column>
      <el-table-column prop="jobTitle" label="Job Title"></el-table-column>
    </el-table>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalEmployees"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  name: 'EmployeeTable',
  data() {
    return {
      employeeList: [],
      currentPage: 1,
      pageSize: 10,
      totalEmployees: 0
    };
  },
  mounted() {
    this.fetchEmployees();
  },
  methods: {
    fetchEmployees() {
      const jsonData = {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      };
      // 假设你有一个获取员工列表的API endpoint是 '/api/employees'
      // 使用 axios 或者 Vue resource 发送请求 (这里以axios为例)
      this.$axios.post('/api/employee/getEmployees', jsonData, {
        headers: {
          'Content-Type': 'application/json',
        },
      }).then(response => {
        // 处理成功的响应
        console.log('Response:', response.data);
        this.employeeList = response.data.list;
        this.totalEmployees = response.data.total;
      }).catch(error => {
        // 处理错误
        console.error('Error:', error);
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchEmployees();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchEmployees();
    }
  }
}
</script>

<style scoped>
</style>
