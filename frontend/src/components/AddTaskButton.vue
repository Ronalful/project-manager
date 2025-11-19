<template>
    <router-link
        class="task-add"
        :to="{ name: 'TaskCreate' }"
    >
      Добавить задачу
    </router-link>


  <router-view v-slot="{ Component }">
    <component :is="Component"/>
  </router-view>
</template>


<style scoped>
.task-add {
  border: 1px solid var(--border);
  border-radius: 0.75rem;
  max-width: 10rem;
  max-height: 10rem;
  margin: 0 0 1em 1em;
  min-width: 10em;
  min-height: 5em;
  color: var(--font-main);
  display: flex;
  align-items: center;
  justify-content: center;
}

.task-add:hover {
  background-color: var(--background3);
  text-decoration: none;
  color: var(--gray);
}


</style>