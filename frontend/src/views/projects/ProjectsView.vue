<script setup>

import ProjectCart from "@/components/ProjectCard.vue";
import AddProjectCard from "@/components/AddProjectCard.vue";
</script>

<template>
  <div class="projects-title">
    <h1>Проекты</h1>
  </div>
  <div class="load__container" v-if="loading">Загружаем проекты...</div>
  <div class="projects-content">
    <ProjectCart v-for="project in projects" :project="project"/>
    <AddProjectCard/>
  </div>
</template>

<script>
import {projectAdminService} from "@/services/ProjectAdminService.js";

export default {
  data() {
    return {
      projects: [],
      errors: {},
      loading: false,
    };
  },
  methods: {
    async handleLoadProjects() {
      this.loading = true
      try {
        const response = await projectAdminService.getAllProjects()
        if (response.success) {
          this.projects = response.data;
        }
      } catch (error) {
        console.error('Error loading projects:', error)
      } finally {
        this.loading = false
      }
    },
  },
  mounted() {
    this.handleLoadProjects();
  },
}
</script>

<style scoped>
.projects-title {
  text-align: left;
}

.projects-content {
  margin-top: 5em;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

</style>