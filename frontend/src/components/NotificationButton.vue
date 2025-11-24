<script setup>

import NotificationIcon from "@/components/icons/NotificationIcon.vue";
</script>

<template>
  <div class="notification">
    <button
        class="notification__button"
        type="button"
    >
    <NotificationIcon/>
    </button>
  </div>
</template>

<style scoped>
.notification{
  margin: 0 1em 0 1em;
}

.notification__button{
  background: rgb(0, 0, 0, 0);
  border: 0;
  cursor: pointer;
}
</style>