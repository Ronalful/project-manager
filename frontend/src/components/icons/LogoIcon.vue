<script setup>

</script>

<template>
  <div v-if="!$slots.isDark" id="lightTheme">
    <div class="logo__icon">
      <a href="/">
        <img class="logo__img" src="@/assets/img/logo_hor-light.png" alt="logo"/>
      </a>
    </div>
  </div>
  <div v-if="$slots.isDark" id="darkTheme">
    <div class="logo__icon">
      <a href="/">
        <img class="logo__img" src="@/assets/img/logo_hor-dark.png" alt="logo"/>
      </a>
    </div>
  </div>
</template>


<style scoped>
.logo__icon{
  margin: 0.5em 1em 0.5em 1em;
}

.logo__img{
  width: 150px;
}
</style>