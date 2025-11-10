<script setup>

import ProjectCart from "@/components/ProjectCard.vue";
</script>

<template>
  <div class="projects-title">
    <h1>Проекты</h1>
  </div>
  <div class="projects-content">
    <ProjectCart v-for="proj in projs" :project="proj"/>
  </div>
</template>

<script>
import {projectAdminService} from "@/services/ProjectAdminService.js";

export default {
  data() {
    return {
      projects: [],
      errors: {},
      projs: [
        {
          id: '1',
          name: "Проект 1",
          descriptor: "Первый проект",
          performers: [
            {id: 1, firstname: "Софья", lastname: "Позднякова"},
            {id: 2, firstname: "Алина", lastname: "Комарова"}
          ]
        },
        {
          id: '2',
          name: "Проект 2",
          descriptor: "Второй проект",
          performers: [
            {id: 3, firstname: "Анастасия", lastname: "Кузнецова"},
            {id: 2, firstname: "Алина", lastname: "Комарова"},
            {id: 4, firstname: "Денис", lastname: "Рыжов"}
          ]
        },
        {
          id: '3',
          name: "Проект 3",
          descriptor: "Третий проект",
          performers: [
            {id: 5, firstname: "Александра", lastname: "Алексеева"},
            {id: 6, firstname: "Снежанна", lastname: "Орлова"},
            {id: 4, firstname: "Денис", lastname: "Рыжов"},
            {id: 7, firstname: "Артём", lastname: "Воробьев"}
          ]
        }
      ]
    };
  },
  methods: {
    async handleLoadProject() {
      const response = await projectAdminService.getAllProjects()
      if(response.success){
        this.projects = response.data;
        console.log(this.projects)
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