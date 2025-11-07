<template>
  <div
      class="project-card"
      @click="$emit('click')"
  >
    <div class="card-content">
      <div class="card-header">
        <h4 class="project-title">
          {{ title }}
        </h4>
      </div>

      <div class="card-footer">
        <div class="avatars-titles">
          <span>Исполнители</span>
        </div>
        <div class="avatars-container">
          <span
              v-for="(avatar, index) in avatars"
              :key="index"
              class="avatar"
          >
            <span class="avatar-name">
              {{ avatar }}
            </span>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Props
const props = defineProps({
  title: {
    type: String,
    default: 'Название проектааааа'
  },
  avatars: {
    type: Array,
    default: () => ['ЕВ', 'МП', 'ОН']
  },
})

// Emits
defineEmits(['click'])
</script>

<style scoped>
.project-card {
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  max-width: 10rem;
  max-height: 10rem;
  padding: 0 1em 1em 1em;
  text-align: left;
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
  border: 1px solid #e5e7eb;
  border-radius: 50%;
  background-color: #e997ff80;
  padding: 0.5rem;
  text-align: center;
  margin-left: 0.5em;
}
</style>