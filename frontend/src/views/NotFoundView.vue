<script setup>

</script>

<template>
  <h1>404</h1>
  <p>Нот фаунд</p>
  <router-link
      class="project-link"
      :to="this.returnTo"
  >
    свободен
  </router-link>
</template>

<script>
export default {
  name: 'NotFound',
  computed: {
    returnTo() {
      return this.$route.query.returnTo ? this.$route.query.returnTo : '/'
    }
  },
}

</script>

<style scoped>

</style>