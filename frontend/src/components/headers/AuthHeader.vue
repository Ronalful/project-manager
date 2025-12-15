<script setup>
import ThemeSwitch from "@/components/ThemeSwitch.vue";
</script>

<template>
  <header>
    <div class="header__container">
      <section class="header__section">
        <ThemeSwitch/>
      </section>
    </div>
  </header>
</template>

<style scoped>

.header__section{
  display: flex;
  justify-content: flex-end;
  flex-direction: row;
  align-items: center;
}
</style>