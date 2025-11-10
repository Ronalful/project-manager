<script setup>

</script>

<template>
  <div
      class="project-card"
  >
    <div class="card-content">
      <div class="card-header">
        <h4 class="project-title">
          {{ data.name }}
        </h4>
      </div>

      <div class="card-footer">
        <div class="avatars-titles">
          <span>Исполнители</span>
        </div>
        <div class="avatars-container">
          <span
              v-for="performer in data.performers"
              class="avatar"
          >
            <span class="avatar-name">
               {{ formatInitials(performer) }}
            </span>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data(){

  },
  methods: {
    formatInitials(performer) {
      const firstInitial = performer.firstname.charAt(0) + '.';
      const lastInitial = performer.lastname.charAt(0) + '.';
      return `${performer.lastname} ${firstInitial}${lastInitial}`;
    }
  }
}
</script>

<style scoped>
.project-card {
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  max-width: 10rem;
  max-height: 10rem;
  padding: 0 1em 1em 1em;
  text-align: left;
  font-size: 20px;
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