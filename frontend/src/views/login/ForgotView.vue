<script setup>
import '../../assets/styles/main.css'
</script>

<template>
  <div v-if="!isAnswer" class="login-main-container recovery">
    <div class="login-container">
      <h2 class="login-second-title">Восстановление пароля</h2>
      <form @submit.prevent="handleRecovery">
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
            Отправить запрос
          </button>

        </div>

      </form>
    </div>
  </div>

  <div v-if="isAnswer" class="login-main-container answer">
    <div class="login-container answer">
      <div class="answer-message">
        <p> Запрос на восстановление пароля отправлен администратору. Дождитесь ответа на почту. </p>
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
      errors: {},
      isAnswer: false
    };
  },
  methods: {
    handleRecovery() {
      this.errors = {};

      if (this.validateEmail() && this.validateSecret()) {
        if (this.formData.email === 'admin@admin.ru' && this.formData.secret === 'cat') {

          // запрос на бэк

          this.isAnswer = true;

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
    }
  }
}
</script>