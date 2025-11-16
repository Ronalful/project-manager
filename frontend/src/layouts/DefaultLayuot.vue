<script setup xmlns="http://www.w3.org/1999/html">

import DefaultHeader from "@/components/headers/DefaultHeader.vue";
import DefaultFooter from "@/components/footers/DefaultFooter.vue";
</script>

<template>
  <DefaultHeader/>
  <main>
    <div class="default-layout">
      <div class="default-container">
        <router-view />
      </div>
    </div>
  </main>
  <DefaultFooter/>
</template>

<style scoped>
.default-container{
  margin: 5em 10em 5em 10em;
}
</style>