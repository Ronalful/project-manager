<script setup>

import ProjectCart from "@/components/ProjectCard.vue";
</script>

<template>
  <div class="projects-title">
    <h1>Проекты</h1>
  </div>
  <div class="projects-content">
    <ProjectCart v-for="project in projects" :project="project"/>
  </div>
</template>

<script>
import {projectAdminService} from "@/services/ProjectAdminService.js";

export default {
  data() {
    return {
      projects: [],
      errors: {},
    };
  },
  methods: {
    async handleLoadProject() {
      const response = await projectAdminService.getAllProjects()
      if(response.success){
        this.projects = response.data;
      }
    },
  },
  mounted() {
    this.handleLoadProject();
  },
}
</script>

<style scoped>
.projects-title {
  text-align: left;
}

.projects-content{
  margin-top: 5em;
  display: flex;
}
</style>