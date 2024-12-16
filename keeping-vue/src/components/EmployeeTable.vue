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
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleEdit(scope.row)">Edit</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalEmployees">
      </el-pagination>
    </div>

    <el-dialog :visible.sync="editDialogVisible" title="Edit Employee">
      <el-form :model="editFormData" label-width="120px">
        <el-form-item label="First Name">
          <el-input v-model="editFormData.firstName"></el-input>
        </el-form-item>
        <el-form-item label="Last Name">
          <el-input v-model="editFormData.lastName"></el-input>
        </el-form-item>
        <!-- Add additional fields as necessary -->
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitEdit">Save</el-button>
      </div>
    </el-dialog>
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
      totalEmployees: 0,
      editDialogVisible: false,
      editFormData: {}  // The form data that will be bound to edit form inputs
    };
  },
  mounted() {
    this.fetchEmployees();
  },
  methods: {
    fetchEmployees() {
      const jsonData = {pageNum: this.currentPage, pageSize: this.pageSize};
      const url = '/api/tkmybatis/getEmployees';      // Should be adjusted to your actual API endpoint
      // const url = '/tkmybatis/getEmployees';
      this.$axios.post(url, jsonData, {headers: {'Content-Type': 'application/json'}})
        .then(response => {
          console.log('Response:', response.data);
          this.employeeList = response.data.list;
          this.totalEmployees = response.data.total;
        })
        .catch(error => {
          console.error('Error:', error);
          this.$message.error('Failed to fetch employees: ' + error);
        });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchEmployees();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchEmployees();
    },
    handleEdit(row) {
      this.editFormData = Object.assign({}, row);  // Copies row data to form data
      this.editDialogVisible = true;
    },
    submitEdit() {
      const url = '/api/tkmybatis/employees/update';  // Adjust this API endpoint to match your requirements
      // const url = '/tkmybatis/employees/update';  // Adjust this API endpoint to match your requirements
      this.$axios.post(url, this.editFormData, {headers: {'Content-Type': 'application/json'}})
        .then(() => {
          this.editDialogVisible = false;
          this.$message.success('Employee updated successfully');
          this.fetchEmployees();  // Refresh the employee list
        })
        .catch(error => {
          console.error('Error:', error);
          this.$message.error('Failed to update employee: ' + error);
        });
    }
  }
}
</script>

<style scoped>
</style>
