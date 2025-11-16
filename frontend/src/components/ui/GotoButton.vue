<script setup>

</script>

<template>
  <button
      class="goto__button"
      type="button"
      v-bind="$attrs"
  >
    <slot>
      Подтвердить
    </slot>
  </button>
</template>

<style scoped>
.goto__button {
  width: 100%;
  padding: 10px;
  background-color: var(--main-color);
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  margin-top: 10px;
}

.goto__button:hover {
  background-color: var(--additional-color);
}
</style>