<template>
  <div id="app">
    <router-view/>
    <HelloWorld/>
  </div>
</template>

<script>
import {defineComponent} from "vue";
import HelloWorld from "@/components/HelloWorld.vue";

export default defineComponent({
  components: {HelloWorld}
})


</script>