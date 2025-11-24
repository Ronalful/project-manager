<script setup>

</script>

<template>
  <button
      class="submit__button"
      type="button"
  >
    <slot>
      Удалить
    </slot>
  </button>
</template>

<style scoped>
.submit__button {
  width: fit-content;
  padding: 10px;
  border: 1px solid var(--error-text);
  background-color: var(--error-background);
  color: var(--font-main);
  font-weight: bold;
  border-radius: 10px;
  cursor: pointer;
  margin-top: 20px;
}

.submit__button:hover {
  background-color: var(--error-background);
  cursor: pointer;
  border: none;
}
</style>