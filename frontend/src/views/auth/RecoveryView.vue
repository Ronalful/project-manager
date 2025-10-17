<script setup>

import SubmitButton from "@/components/ui/SubmitButton.vue";
</script>

<template>
  <div class="login-main-container">
    <form @submit.prevent="handleChangePassword">
      <div class="login-container">
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

        <SubmitButton>Сменить пароль</SubmitButton>

      </div>
    </form>
  </div>


</template>

<script>
export default {
  data() {
    return {
      recoveryFormData: {
        password: '',
        confirmPassword: '',
      },
      errors: {}
    }
  },
  methods: {
    handleChangePassword() {
      this.errors = {};

      if (this.validatePassword() && this.checkPasswordCompliance()) {

        // запрос на бэк

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