<script setup>

</script>

<template>
  <button
      class="submit__button"
      type="submit"
  >
    <slot>
      Подтвердить
    </slot>
  </button>
</template>

<style scoped>
.submit__button {
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

.submit__button:hover {
  background-color: var(--additional-color);
}
</style>