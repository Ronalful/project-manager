<template>
  <Container>
    <template #second-title>Осталось совсем чуть-чуть!</template>
    <template #third-title>
      Придумайте
      <a href="#password">новый пароль</a>
      и
      <a href="#secret">секретное слово</a>
      для активации аккаунта
    </template>
    <template #content>
      <form @submit.prevent="confirmActivationHandle" novalidate>
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
      </form>
    </template>
  </Container>
</template>

<script>
import {authService} from "@/services/AuthService.js";
import SubmitButton from "@/components/ui/SubmitButton.vue";
import Input from "@/components/ui/Input.vue";
import Container from "@/components/Container.vue";

export default {
  components: {Container, Input, SubmitButton},
  data() {
    return {
      activationFormData: {
        password: '',
        secret: '',
      },
      errors: {}
    }
  },
  methods: {
    async confirmActivationHandle() {
      this.errors = {}

      if (!this.validateForm()) {
        return false
      }

      try {
        const response = await authService.confirmActivation({
          secretPhrase: this.activationFormData.secret,
          password: this.activationFormData.password
        })
      } catch (error) {
        this.errors.others = error.response?.data?.message || 'Ошибка активации аккаунта. Обратитесь к администратору.'
      }
    },

    validateForm() {
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
