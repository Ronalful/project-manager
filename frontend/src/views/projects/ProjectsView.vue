<script setup>

import ProjectCart from "@/components/ProjectCard.vue";
</script>

<template>
  <div class="projects-title">
    <h1>Проекты</h1>
  </div>
  <div class="projects-content">
    <ProjectCart/>
  </div>
</template>

<script>

</script>

<style scoped>
.projects-title {
  text-align: left;
}

.projects-content{
  margin-top: 5em;
}
</style>