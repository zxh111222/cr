<script setup>
import {onMounted, ref} from "vue";
import {ping} from "@/api/auth.js"

const content = ref()

onMounted(() => {
  ping()
    .then((res) => {
      content.value = res.data
    })
    .catch((error) => {
      console.log('访问 ping 出错' + error)
    })
})
</script>

<template>
<div>
  <h1>这是一个 Dashboard 页</h1>
  <p>ping : {{ content }}</p>
</div>
</template>

<style scoped>

</style>
