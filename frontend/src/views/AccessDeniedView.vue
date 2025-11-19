<script setup>

</script>

<template>
  <h1>403</h1>
  <p>Тебе сюда нельзя</p>
  <router-link
      class="project-link"
      :to="this.returnTo"
  >
    иди
  </router-link>
</template>

<script>
export default {
  name: 'AccessDenied',
  computed: {
    returnTo() {
      return this.$route.query.returnTo ? this.$route.query.returnTo : '/'
    }
  },
}

</script>

<style scoped>

</style>