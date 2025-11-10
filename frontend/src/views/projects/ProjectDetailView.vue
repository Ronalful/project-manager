<script setup>

</script>

<template>
  <h1> Проект {{this.projectId}} </h1>
</template>

<script>
export default {
  data(){
    return {
      projectId: '',
    }
  },
  mounted() {
    // запрос в бэк
    this.projectId = this.$route.params.id;
  }
}
</script>

<style scoped>

</style>