<script setup>
import '../../assets/styles/main.css'
import ThemeSwitch from "@/components/ThemeSwitch.vue";
</script>

<template>
  <ThemeSwitch></ThemeSwitch>
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

          <button
              class="submit-button"
              type="submit"
          >
            Войти
          </button>

          <a
              class="forgot-button"
              href="login/forgot"
              type="button"
          >
            Не помню пароль
          </a>
        </div>

      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        email: '',
        password: '',
      },
      errors: {}
    };
  },
  methods: {
    handleLogin() {
      this.errors = {};

      if (this.validateEmail() && this.validatePassword()) {
        if (this.formData.email === 'admin@admin.ru' && this.formData.password === '1234') {
          alert('Успешный вход');
          // Можно добавить здесь переход на другую страницу или вызов API
        } else {
          console.log('error');
          this.errors.incorrect = 'Неверный логин или пароль';
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
  },
}
</script>

<style scoped>

</style>
