<script setup>

import AddIcon from "@/components/icons/AddIcon.vue";

</script>

<template>
  <router-link
      class="project-add"
      to="/projects/create"
  >
    <AddIcon/>
  </router-link>

  <router-view v-slot="{ Component }">
    <component
        :is="Component"
        @close="closeModal"
        @success="handleSuccess"
    />
  </router-view>
</template>


<script>
import router from "@/router/index.js";

export default {
  setup() {
    const closeModal = () => {
      // Возвращаемся назад или на текущую страницу
      if (window.history.length > 1) {
        window.history.back()
      } else {
        router.push('/projects')
      }
    }

    const handleSuccess = (project) => {
      console.log('Project created:', project)
      closeModal()
      // Обновляем список проектов
    }

    return {
      closeModal,
      handleSuccess
    }
  },
  data(){
    return{
    }
  },
  methods: {

  },
}
</script>

<style scoped>
.project-add {
  border: 1px dashed var(--border);
  border-radius: 0.75rem;
  max-width: 10rem;
  max-height: 10rem;
  margin: 0 0 1em 1em;
  min-width: 10em;
  min-height: 5em;
  color: var(--gray2);
  display: flex;
  align-items: center;
  justify-content: center;
}

.project-add:hover{
  background-color: var(--background3);
  text-decoration: none;
  color: var(--gray);
}



</style>