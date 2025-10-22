<script setup>
import ThemeSwitch from "@/components/ThemeSwitch.vue";
</script>

<template>
  <ThemeSwitch></ThemeSwitch>
  <div class="auth-layout">
    <div class="auth-container">
      <router-view /> <!-- Здесь будут рендериться дочерние компоненты -->
    </div>
  </div>
</template>
