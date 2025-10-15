<script setup>
import '../../assets/styles/main.css'
</script>

<template>
  <div v-if="!isChangePassword && !isSuccess" class="login-main-container recovery">
    <div class="login-container">
      <h2 class="login-second-title">Восстановление пароля</h2>
      <form @submit.prevent="handleRecovery">
        <div class="access-token" hidden></div>
        <p v-if="errors.incorrect" class="error title">{{ errors.incorrect }}</p>
        <div class="login-form-group-container">
          <div class="login-form-group">
            <input
                class="field"
                id="email"
                v-model="formData.email"
                type="text"
                placeholder="Email"/>
          </div>
          <p v-if="errors.email" class="error">{{ errors.email }}</p>

          <div class="login-form-group">
            <input
                class="field"
                id="secret"
                v-model="formData.secret"
                type="text"
                placeholder="Секретное слово"
            />
          </div>
          <p v-if="errors.secret" class="error">{{ errors.secret }}</p>

          <button
              class="submit-button"
              type="submit"
          >
            Восстановить
          </button>

        </div>

      </form>
    </div>
  </div>

  <div v-if="isChangePassword" class="login-main-container answer">
    <form @submit.prevent="handleChangePassword">
      <div class="login-container answer">
        <div class="login-form-group">
          <input
              class="field"
              id="password"
              v-model="recoveryFormData.password"
              type="password"
              placeholder="Пароль"
          />
          <p v-if="errors.password" class="error">{{ errors.password }}</p>
        </div>
        <div class="login-form-group">
          <input
              class="field"
              id="confirmPassword"
              v-model="recoveryFormData.confirmPassword"
              type="password"
              placeholder="Подтвердите пароль"
          />
          <p v-if="errors.confirmPassword" class="error">{{ errors.confirmPassword }}</p>
        </div>
          <button
              class="submit-button"
              type="submit"
          >
            Сменить пароль
          </button>
      </div>
    </form>
  </div>

  <div v-if="isSuccess" class="login-main-container answer">
    <div class="login-container answer">
      <div class="answer-message">
        <p> Пароль восстановлен! </p>
      </div>
      <div class="goback-button">
        <a
            class="submit-button"
            type="button"
            href="/login"

        >Вернуться</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        email: '',
        secret: '',
      },
      recoveryFormData: {
        password: '',
        confirmPassword: '',
      },
      errors: {},
      isSuccess: false,
      isChangePassword: false,
    };
  },
  methods: {
    handleRecovery() {
      this.errors = {};

      if (this.validateEmail() && this.validateSecret()) {
        if (this.formData.email === 'admin@admin.ru' && this.formData.secret === 'cat') {

          // запрос на бэк

          this.isChangePassword = true;
        }
      }
    },

    handleChangePassword() {
      this.errors = {};

      if (this.validatePassword() && this.checkPasswordCompliance()) {

        // запрос на бэк

        this.isChangePassword = false;
        this.isSuccess = true;
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

    validateSecret() {
      const secretField = document.getElementById('secret');

      if (!this.formData.secret) {
        this.errors.secret = 'Введите секретное слово';
        secretField.classList.add('field-error');
        return false;
      } else {
        secretField.classList.remove('field-error');
        return true;
      }
    },

    validatePassword() {
      const passwordField = document.getElementById('password');

      if (!this.recoveryFormData.password) {
        this.errors.password = 'Введите пароль';
        passwordField.classList.add('field-error');
        return false;
      } else {
        passwordField.classList.remove('field-error');
        return true;
      }
    },

    checkPasswordCompliance() {
      const confirmPasswordField = document.getElementById('confirmPassword');

      if (this.recoveryFormData.password !== this.recoveryFormData.confirmPassword) {
        console.log('не');
        this.errors.confirmPassword = 'Пароли не совпадают';
        confirmPasswordField.classList.add('field-error');
        return false;
      } else {
        confirmPasswordField.classList.remove('field-error');
        return true;
      }
    }
  }
}
</script>