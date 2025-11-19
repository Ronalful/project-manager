<script setup>
import AddTaskButton from "@/components/AddTaskButton.vue";

</script>

<template>
  <div class="tasks__main-container">
    <AddTaskButton/>

    <div class="filter__container">
    </div>
    <div class="tasks__container">
      <div class="tasks__table">
        <table>
          <thead>
          <tr>
            <th>Название</th>
            <th>Статус</th>
            <th>Проект</th>
            <th>Оценка</th>
            <th>Назначены</th>
            <th>Приоритет</th>
            <th>Дедлайн</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td></td>
            <td></td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

</template>

<script>
import {useUserStore} from "@/stores/UserStore.js";
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {taskAdminService} from "@/services/TaskAdminService.js";

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

  },
  mounted() {

  },
}
</script>

<style scoped>
.tasks__main-container {
  border-radius: 16px;
  padding: 3em;
  background-color: var(--background3);
}
</style>