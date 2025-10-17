<script setup>
import '../../assets/styles/main.css'
import SubmitButton from "@/components/ui/SubmitButton.vue";
import GotoLink from "@/components/ui/GotoLink.vue";
</script>

<template>
  <div class="login-main-container">
    <div class="login-container">
      <div class="login-titles">
        <h1 class="login-first-title">
          <img class="logo-image login" src="../../assets/img/logo.png">
        </h1>
        <h2 class="login-second-title">Давайте начнём!</h2>
      </div>
      <form @submit.prevent="handleLogin">
        <p v-if="errors.incorrect" class="error title">{{ errors.incorrect }}</p>
        <p v-if="errors.others" class="error title">{{ errors.others }}</p>
        <div class="login-form-group-container">
          <div class="login-form-group">
            <input
                class="field"
                id="email"
                v-model="formData.email"
                type="text"
                placeholder="Email"/>
            <p v-if="errors.email" class="error">{{ errors.email }}</p>
          </div>

          <div class="login-form-group">
            <input
                class="field"
                id="password"
                v-model="formData.password"
                type="password"
                placeholder="Пароль"
            />
            <p v-if="errors.password" class="error">{{ errors.password }}</p>
          </div>

        <SubmitButton>Войти</SubmitButton>

        <GotoLink href="login/forgot">Не помню пароль</GotoLink>

        </div>

      </form>
    </div>
  </div>
</template>

<script>
import {authService} from '@/services/AuthService.js'

export default {
  data() {
    return {
      formData: {
        email: '',
        password: '',
      },
      errors: {},
    };
  },
  methods: {
    async handleLogin() {
      this.errors = {}

      if (!this.validateEmail() || !this.validatePassword()) {
        return;
      }

      const response = await authService.login({
        email: this.formData.email,
        password: this.formData.password
      })
      if (!response.requiresActivation) {
        if (response.incorrectLoginPassword) {
          this.errors.incorrect = 'Неверный логин или пароль';
        } else {
          this.errors.others = response?.data?.message || 'Ошибка входа'
        }
      }
    },

    validateEmail() {
      const emailField = document.getElementById('email');

      if (!this.formData.email) {
        this.errors.email = 'Введите email';
        emailField.classList.add('field-error');
        return false;
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.formData.email)) {
        emailField.classList.add('field-error');
        this.errors.email = 'Введите корректный email';
        return false;
      } else {
        emailField.classList.remove('field-error');
        return true;
      }
    },

    validatePassword() {
      const passwordField = document.getElementById('password');

      if (!this.formData.password) {
        this.errors.password = 'Введите пароль';
        passwordField.classList.add('field-error');
        return false;
      } else {
        passwordField.classList.remove('field-error');
        return true;
      }
    },

  }
}
</script>

<style scoped>

</style>
