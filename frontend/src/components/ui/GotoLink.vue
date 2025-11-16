<script setup>

</script>

<template>
  <div class="goto">
    <a
        class="goto__link"
        v-bind="$attrs"
        type="button"
    >
      <slot></slot>
    </a>
  </div>
</template>

<style scoped>
.goto__link {
  display: block;
  text-align: center;
  margin-top: 10px;
  color: var(--gray);
}

.goto__link:hover {
  color: var(--gray);
  text-decoration: none;
  text-shadow: var(--main-color) 1px 0 10px;
}
</style>