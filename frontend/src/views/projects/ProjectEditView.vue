<script setup>

</script>

<template>
  <h1> Изменяем проект {{this.projectId}} </h1>
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