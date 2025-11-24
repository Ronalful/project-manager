<template>
  <div class="main-container">
    <div class="container">
      <div class="titles">
        <h1 class="first-title">
          <slot name="first-title"></slot>
        </h1>
        <h2 class="second-title">
          <slot name="second-title"></slot>
        </h2>
        <h3 class="third-title">
          <slot name="third-title"></slot>
        </h3>
      </div>

      <div class="container-content">
        <slot name="content"></slot>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  min-width: 50vh;
}

.container {
  width: 100%;
  padding: 20px;
  background-color: var(--background2);
  border-radius: 15px;
  box-shadow: 0px 0px 13px var(--shadow);
}

@media (min-width: 800px) {
  .container {
    width: 448px;
  }
}

.titles {
  text-align: center;
}

.second-title{
  margin-bottom: 10px;
  font-weight: inherit;
  color: var(--font-additional);
  font-size: x-large;
}

</style>