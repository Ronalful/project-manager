<script setup>
import '../../assets/styles/main.css'
import SubmitButton from "@/components/ui/SubmitButton.vue";
import Input from "@/components/ui/Input.vue";
</script>

<template>
  <div class="login-main-container recovery">
    <div class="login-container">
      <h2 class="login-second-title">Восстановление пароля</h2>
      <form @submit.prevent="handleRecovery" novalidate>
        <p v-if="errors.incorrect" class="error title">{{ errors.incorrect }}</p>
        <div class="login-form-group-container">
          <Input
              ref="emailField"
              v-model="formData.email"
              type="email"
              placeholder="Email"
              required
          ></Input>

          <Input
              ref="secretField"
              v-model="formData.secret"
              type="text"
              placeholder="Секретное слово"
              required
          ></Input>

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

      if (!this.validateForm()) {
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