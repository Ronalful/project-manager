<script setup>
import '../../assets/styles/main.css'
import SubmitButton from "@/components/ui/SubmitButton.vue";
import GotoLink from "@/components/ui/GotoLink.vue";
import Input from "@/components/ui/Input.vue";
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
      <form @submit.prevent="handleLogin" novalidate>
        <p v-if="errors.incorrect" class="error title">{{ errors.incorrect }}</p>
        <p v-if="errors.others" class="error title">{{ errors.others }}</p>
        <div class="login-form-group-container">
          <Input
              ref="emailField"
              v-model="formData.email"
              type="email"
              placeholder="Email"
              required
          ></Input>

          <Input
              ref="passwordField"
              v-model="formData.password"
              type="password"
              placeholder="Пароль"
              required
          ></Input>

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

      if (!this.validateForm()) {
        return;
      }

      const response = await authService.login({
        email: this.formData.email,
        password: this.formData.password
      })
      if (!response.requiresActivation) {
        if (response.incorrectLoginPassword) {
          this.errors.incorrect = 'Неверный логин или пароль';
        } else if(response.passwordExpired){
          this.errors.incorrect = 'Этот пароль недействителен. Пожалуйста, установите новый через операцию сброса пароля.';
        } else {
          this.errors.others = response?.data?.message || 'Ошибка входа'
        }
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

<style scoped>

</style>
