<script setup>
import LightThemeIcon from "@/components/icons/LightThemeIcon.vue";
import DarkThemeIcon from "@/components/icons/DarkThemeIcon.vue";
</script>

<template>
  <div class="theme-switch">
    <div v-if="isDark"><LightThemeIcon><template #isDark></template></LightThemeIcon></div>
    <div v-if="!isDark"><LightThemeIcon><template></template></LightThemeIcon></div>
    <label class="theme-switch__label">
      <input
          class="theme-switch__checkbox"
          id="theme-switch"
          type="checkbox"
          @change="themeSwitch"
          v-model="isDark"
          hidden
      />
      <span class="theme-switch__slider"></span>
    </label>
    <div v-if="isDark"><DarkThemeIcon><template #isDark></template></DarkThemeIcon></div>
    <div v-if="!isDark"><DarkThemeIcon><template></template></DarkThemeIcon></div>
  </div>


</template>

<script>
export default {
  data() {
    return {
      theme: localStorage.getItem('theme') || 'light',
      isDark: localStorage.getItem('theme') === 'dark',
    }
  },
  methods: {
    themeSwitch() {
      this.theme = this.theme === 'dark' ? 'light' : 'dark'
      localStorage.setItem('theme', this.theme)
    }
  },
}
</script>

<style scoped>
.theme-switch__icon{
  width: 30px;
  height: 30px;
}

.theme-switch{
  display: flex;
  flex-direction: row;
  align-items: center;
}

.theme-switch__label {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.theme-switch__label input {
  opacity: 0;
  width: 0;
  height: 0;
}

.theme-switch__slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--gray2);
  transition: .4s;
  border-radius: 34px;
}

.theme-switch__slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: var(--background);
  transition: .4s;
  border-radius: 50%;
}

input:checked + .theme-switch__slider {
  background-color: var(--main-color);
}

input:checked + .theme-switch__slider:before {
  transform: translateX(26px);
}

.theme-switch__slider.round {
  border-radius: 34px;
}

.theme-switch__slider.round:before {
  border-radius: 50%;
}
</style>