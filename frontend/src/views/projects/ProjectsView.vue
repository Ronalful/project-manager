<script setup>

import ProjectCart from "@/components/ProjectCard.vue";
import AddProjectCard from "@/components/AddProjectCard.vue";
</script>

<template>
  <div class="projects__main-container">
    <div class="projects-title">
      <h1>Проекты</h1>
    </div>
    <div class="load__container" v-if="loading">Загружаем проекты...</div>
    <div class="projects-content">
      <ProjectCart v-for="project in projects" :project="project"/>
      <AddProjectCard
          v-if="withCreate"
      />
    </div>
  </div>
</template>

<script>
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {projectUserService} from "@/services/ProjectUserService.js";
import {useUserStore} from "@/stores/UserStore.js";

export default {
  data() {
    return {
      projects: [],
      errors: {},
      loading: false,
      withCreate: false,
    };
  },
  methods: {
    async handleLoadProjects() {
      this.loading = true
      const userStore = useUserStore()
      try {
        if (userStore.isAdmin) {
          this.withCreate = true

          const response = await projectAdminService.getAllProjects()
          if (response.success) {
            this.projects = response.data;
          }
        } else {
          const response = await projectUserService.getMyProjects()
          if (response.success) {
            this.projects = response.data;
          }
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
.projects__main-container {
  border-radius: 16px;
  padding: 3em;
  background-color: var(--background3);
}

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