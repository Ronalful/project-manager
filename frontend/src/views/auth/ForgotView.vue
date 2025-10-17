<script setup>
import '../../assets/styles/main.css'
import SubmitButton from "@/components/ui/SubmitButton.vue";
import NotificationWithBack from "@/components/NotificationWithBack.vue";
</script>

<template>
  <div class="login-main-container recovery">
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

          <SubmitButton>Восстановить</SubmitButton>

        </div>

      </form>
    </div>
  </div>

</template>

<script>
import {authService} from "@/services/AuthService.js";

export default {
  data() {
    return {
      formData: {
        email: '',
        secret: '',
      },
      errors: {},
    };
  },
  methods: {
    async handleRecovery() {
      this.errors = {};

      if (!this.validateEmail() || !this.validateSecret()) {
        return false
      }

      const response = await authService.initiateResetPassword({
        email: this.formData.email,
        secretPhrase: this.formData.secret
      })

      if (response.incorrectSecretPhrase) {
        this.errors.incorrect = 'Неверное секретное слово';
      } else if (response.userDoesNotExist) {
        this.errors.incorrect = 'Пользователь с таким email не найден.';
      } else {
        this.errors.others = response?.data?.message || 'Ошибка восстановления.'
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
  }
}
</script>