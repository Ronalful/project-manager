<script setup>
import GotoButton from "@/components/ui/GotoButton.vue";
</script>

<template>
  <div class="login-main-container">
    <div class="login-container answer">
      <div class="answer-message">
        <slot></slot>
      </div>
      <GotoButton @click="$router.go(-1)">
        Вернуться
      </GotoButton>
    </div>
  </div>
</template>

<style scoped>

</style>