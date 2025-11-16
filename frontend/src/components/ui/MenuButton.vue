<script setup>

import MenuIcon from "@/components/icons/MenuIcon.vue";
</script>

<template>
<div class="menu">
  <button
      class="menu__button"
      type="button"
  >
    <MenuIcon/>
  </button>
</div>
</template>

<style scoped>
.menu__button{
  background: rgb(0, 0, 0, 0);
  border: 0;
  cursor: pointer;
}
</style>