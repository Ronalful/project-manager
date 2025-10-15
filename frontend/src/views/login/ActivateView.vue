<script setup>
import '../../assets/styles/main.css'
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
          для восстановления аккаунта</h3>
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
              type="password"
              placeholder="Секретное слово"
          />
          <p v-if="errors.confirmPassword" class="error">{{ errors.confirmPassword }}</p>
        </div>
        <button
            class="submit-button"
            type="submit"
        >
          Подтвердить
        </button>
      </div>
    </form>
  </div>
</template>

<script>
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
    confirmActivationHandle(){

    }
  }

}
</script>
