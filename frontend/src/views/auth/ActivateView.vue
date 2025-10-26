<script setup>
import '../../assets/styles/main.css'
import SubmitButton from "@/components/ui/SubmitButton.vue";
import Input from "@/components/ui/Input.vue";
</script>

<template>
  <div class="login-main-container">
    <form @submit.prevent="confirmActivationHandle">
      <div class="login-container">
        <h2 class="login-second-title">Осталось совсем чуть-чуть!</h2>
        <h3 class="login-third-title">Придумайте
          <a href="#password">новый пароль</a>
          и
          <a href="#secret">секретное слово</a>
          для активации аккаунта</h3>
        <p v-if="errors.others" class="error title">{{ errors.others }}</p>

        <Input
            id="password"
            ref="passwordField"
            v-model="activationFormData.password"
            type="password"
            placeholder="Пароль"
            required
        ></Input>

        <Input
            id="secret"
            ref="secretField"
            v-model="activationFormData.secret"
            type="text"
            placeholder="Секретное слово"
            required
        ></Input>

        <SubmitButton></SubmitButton>

      </div>
    </form>
  </div>
</template>

<script>
import {authService} from "@/services/AuthService.js";

export default {
  data() {
    return {
      activationFormData: {
        password: '',
        secret: '',
      },
      errors: {}
    }
  },
  methods:{
    async confirmActivationHandle(){
      this.errors = {}

      if (!this.validateForm()) {
        return false
      }

      try {
        const response = await authService.confirmActivation({
          secretPhrase: this.activationFormData.secret,
          password: this.activationFormData.password
        })
      } catch (error){
        this.errors.others = error.response?.data?.message || 'Ошибка активации аккаунта. Обратитесь к администратору.'
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
