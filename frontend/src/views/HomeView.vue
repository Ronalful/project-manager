<script setup>
import LogoutIcon from "@/components/icons/LogoutIcon.vue";
import LogoutButton from "@/components/LogoutButton.vue";
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="../assets/logo.svg" width="125" height="125" />
    <div class="wrapper">

    </div>
  </header>

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
