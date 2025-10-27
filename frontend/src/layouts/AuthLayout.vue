<script setup>
import AuthHeader from "@/components/headers/AuthHeader.vue";
</script>

<template>
  <AuthHeader/>
  <main>
    <div class="auth-layout">
      <div class="auth-container">
        <router-view />
      </div>
    </div>
  </main>
</template>
