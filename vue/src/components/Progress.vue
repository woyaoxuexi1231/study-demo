<template>
  <div>
    <el-calendar v-model="value">
    </el-calendar>
  </div>
</template>

<script>
export default {
  name: "Progress",
  data() {
    return {
      value: new Date()
    }
  }
}
</script>

<style scoped>

</style>
