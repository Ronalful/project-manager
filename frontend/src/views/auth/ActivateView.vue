<script setup>
import '../../assets/styles/main.css'
import SubmitButton from "@/components/ui/SubmitButton.vue";
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
        <div class="login-form-group">
          <input
              class="field"
              id="password"
              v-model="activationData.password"
              type="password"
              placeholder="Новый пароль"
          />
          <p v-if="errors.password" class="error">{{ errors.password }}</p>
        </div>
        <div class="login-form-group">
          <input
              class="field"
              id="secret"
              v-model="activationData.secret"
              type="text"
              placeholder="Секретное слово"
          />
          <p v-if="errors.secret" class="error">{{ errors.secret }}</p>
        </div>

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
      activationData: {
        password: '',
        secret: '',
      },
      errors: {}
    }
  },
  methods:{
    async confirmActivationHandle(){
      this.errors = {}

      if (!this.validateSecret() || !this.validatePassword()) {
        return;
      }

      try {
        const response = await authService.confirmActivation({
          secretPhrase: this.activationData.secret,
          password: this.activationData.password
        })
      } catch (error){
        this.errors.others = error.response?.data?.message || 'Ошибка активации аккаунта. Обратитесь к администратору.'
      }
    },

    validateSecret() {
      const secretField = document.getElementById('secret');

      if (!this.activationData.secret) {
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

      if (!this.activationData.password) {
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
