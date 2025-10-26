<script setup>
import AuthHeader from "@/components/headers/AuthHeader.vue";
</script>

<template>
  <AuthHeader/>
  <div class="auth-layout">
    <div class="auth-container">
      <router-view />
    </div>
  </div>
</template>
