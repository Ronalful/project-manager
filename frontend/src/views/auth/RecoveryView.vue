<script setup>

import SubmitButton from "@/components/ui/SubmitButton.vue";
import Input from "@/components/ui/Input.vue";
</script>

<template>
  <div class="login-main-container">
    <form @submit.prevent="handleChangePassword" novalidate>
      <div class="login-container">
        <h2 class="login-second-title">Задайте новый пароль</h2>
        <p v-if="errors.incorrect" class="error title">{{ errors.incorrect }}</p>

        <Input
            ref="passwordField"
            v-model="recoveryFormData.password"
            type="password"
            placeholder="Пароль"
            required
        ></Input>

        <Input
            id="confirmPassword"
            ref="confirmPasswordField"
            v-model="recoveryFormData.confirmPassword"
            type="password"
            placeholder="Подтвердите пароль"
            required
        ></Input>

        <SubmitButton>Сменить пароль</SubmitButton>

      </div>
    </form>
  </div>


</template>

<script>
import {authService} from "@/services/AuthService.js";

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
    async handleChangePassword() {
      this.errors = {};

      if (this.validateForm() && this.checkPasswordCompliance()) {
        const response = await authService.confirmResetPassword({
          password: this.recoveryFormData.password,
        })
        if(response.error){
          this.errors.incorrect = 'Непредвиденная ошибка';
        }
      }
    },

    checkPasswordCompliance() {
      if (this.recoveryFormData.password !== this.recoveryFormData.confirmPassword) {
        this.errors.incorrect = 'Пароли не совпадают';
        return false;
      } else {
        this.errors.incorrect = '';
        return true;
      }
    },

    validateForm(){
      const fields = this.$refs
      let isValid = true

      Object.values(fields).forEach(field => {
        if (!field.isValid()) {
          isValid = false
        }
      })

      return isValid
    }
  }
}
</script>