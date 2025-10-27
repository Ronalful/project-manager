<script setup>
import LogoutIcon from "@/components/icons/LogoutIcon.vue";
import LogoutButton from "@/components/LogoutButton.vue";
</script>

<template>
    <div class="wrapper">
      <h1> HELLO </h1>
      <h2> Let's go </h2>
      <h3>Но только наступила осень</h3>
      <h4>Назови мне свое имя</h4>
    </div>
    <img alt="Vue logo" class="logo" src="../assets/logo.svg" width="125" height="125" />

  <main>
    <TheWelcome />
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}
</style>
