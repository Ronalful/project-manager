<script setup>

</script>

<template>
  <div
      class="project-card"
  >
    <div class="card-content">
      <div class="card-header">
        <h4 class="project-title">
          <router-link
              class="project-link"
              :to="{
                name: 'ProjectDetail',
                params: { id: this.project?.id }}"
          >
            {{ project.name }}
          </router-link>
        </h4>
      </div>

      <div class="card-footer">
        <div class="avatars-titles">
          <span>Исполнители</span>
        </div>
        <div class="avatars-container">
          <span
              v-for="performer in visiblePerformers"
              class="avatar"
          >
            <span class="avatar-name">
               {{ formatInitials(performer) }}
            </span>
          </span>
          <span
              v-if="hiddenPerformersCount > 0"
              class="avatars-counter"
          >
            <span class="avatars-name">
              +{{ hiddenPerformersCount }}
            </span>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props:[
      'project'
  ],
  data(){
    return{
    }
  },
  methods: {
    formatInitials(performer) {
      const firstInitial = performer.firstname.charAt(0);
      const lastInitial = performer.lastname.charAt(0);
      return `${firstInitial}${lastInitial}`;
    }
  },
  computed: {
    visiblePerformers() {
      return this.project.performers?.slice(0, 3) || []
    },

    hiddenPerformersCount() {
      const total = this.project.performers?.length || 0
      return total > 3 ? total - 3 : 0
    }
  },
}
</script>

<style scoped>
.project-link{
  color: var(--font-main);
}

.project-link:hover{
  text-decoration: none;
}

.project-card {
  border: 1px solid var(--border);
  border-radius: 0.75rem;
  max-width: 10rem;
  max-height: 10rem;
  padding: 0 1em 1em 1em;
  text-align: left;
  font-size: 20px;
  margin: 0 0 1em 1em;
  min-width: 10em;
  background-color: var(--background3);
}

.avatars-titles{
  margin-bottom: 1em;
  font-size: 0.8rem;
  color: var(--font-additional);
}

.avatars-container{
  margin-left: -0.5em;
}

.avatar{
  border: 1px solid var(--border);
  border-radius: 50%;
  background-color: #e997ff80;
  padding: 0.5rem;
  text-align: center;
  margin-left: 0.3em;
}

.avatars-counter{
  margin-left: 0.3em;
}

</style>