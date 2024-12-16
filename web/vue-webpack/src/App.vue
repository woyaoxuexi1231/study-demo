<template>
  <div>
    <p>hello world</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      msg: "hello world"
    };
  },
  methods: {
    handleClick() {
      console.log("clicked");
    }
  }
}
</script>
<style>
.msg {
  color: red;
}
</style>