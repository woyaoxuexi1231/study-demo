<template>
  <div class="seckill-test-container">
    <h1>秒杀接口测试</h1>

    <!-- 初始化库存 -->
    <el-card class="section">
      <template #header>
        <h2>1. 初始化库存</h2>
      </template>
      <el-form :inline="true" class="input-group">
        <el-form-item label="商品ID">
          <el-input v-model="initProductId" placeholder="商品ID" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input v-model="initStock" placeholder="库存数量" type="number" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleInitStock">初始化</el-button>
        </el-form-item>
      </el-form>
      <p v-if="initResult" class="result">{{ initResult }}</p>
    </el-card>

    <!-- 查询库存 -->
    <el-card class="section">
      <template #header>
        <h2>2. 查询剩余库存</h2>
      </template>
      <el-form :inline="true" class="input-group">
        <el-form-item label="商品ID">
          <el-input v-model="queryProductId" placeholder="商品ID" type="number" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQueryStock">查询</el-button>
        </el-form-item>
      </el-form>
      <p v-if="stockResult !== null" class="result">剩余库存: {{ stockResult }}</p>
    </el-card>

    <!-- 尝试秒杀 -->
    <el-card class="section">
      <template #header>
        <h2>3. 尝试秒杀</h2>
      </template>
      <el-form :inline="true" class="input-group">
<!--        <el-form-item label="用户ID">-->
<!--          <el-input v-model="seckillUserId" placeholder="商品ID" type="number" />-->
<!--        </el-form-item>-->
        <el-form-item label="商品ID">
          <el-input v-model="seckillProductId" placeholder="商品ID" type="number" />
        </el-form-item>
        <el-form-item label="购买数量">
          <el-input v-model="seckillQuantity" placeholder="购买数量" type="number" />
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="handleSeckill">秒杀</el-button>
        </el-form-item>
      </el-form>
      <div v-if="seckillResult" class="result">
        <p>结果: <strong>{{ seckillResult}}</strong></p>
        <p v-if="seckillResult.message">消息: {{ seckillResult.message }}</p>
        <p>订单ID: {{ seckillResult.orderId || '无' }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

// 初始化库存相关
const initProductId = ref('');
const initStock = ref('');
const initResult = ref('');

// 查询库存相关
const queryProductId = ref('');
const stockResult = ref(null);

// 秒杀相关
// const seckillUserId = ref('');
const seckillProductId = ref('');
const seckillQuantity = ref(1);
const seckillResult = ref(null);

// 关键修改：所有请求路径前加上 /api 前缀
const API_PREFIX = '/api/seckill'; // 匹配 devServer.proxy 的 /api

// 初始化库存
const handleInitStock = async () => {
  try {
    const response = await axios.post(
        `${API_PREFIX}/init-stock/${initProductId.value}/${initStock.value}`
    );
    initResult.value = response.data;
    console.log('初始化成功:', response.data);
  } catch (error) {
    initResult.value = `初始化失败: ${error.response?.data?.message || error.message}`;
    console.error('初始化失败:', error);
  }
};

// 查询库存
const handleQueryStock = async () => {
  try {
    const response = await axios.get(
        `${API_PREFIX}/remain-stock/${queryProductId.value}`
    );
    stockResult.value = response.data;
    console.log('查询成功:', response.data);
  } catch (error) {
    stockResult.value = `查询失败: ${error.response?.data?.message || error.message}`;
    console.error('查询失败:', error);
  }
};

// 尝试秒杀
const handleSeckill = async () => {
  try {
    const response = await axios.post(
        // `${API_PREFIX}/try-seckill/${seckillProductId.value}/${seckillQuantity.value}/${seckillUserId.value}`,
        `${API_PREFIX}/try-seckill/${seckillProductId.value}/${seckillQuantity.value}`,
    );
    seckillResult.value = response.data;
    console.log('秒杀结果:', response.data);
  } catch (error) {
    seckillResult.value = {
      success: false,
      message: error.response?.data?.message || error.message
    };
    console.error('秒杀失败:', error);
  }
};
</script>

<style scoped>
.seckill-test-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.section {
  margin-bottom: 20px;
}

h1 {
  text-align: center;
  color: #333;
}

h2 {
  margin-top: 0;
  color: #444;
  font-size: 1.2em;
}

.input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
}

.result {
  padding: 10px;
  background-color: #f8f8f8;
  border-radius: 4px;
  margin-top: 10px;
}
</style>