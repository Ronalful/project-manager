<template>
  <BaseModal :title="this.title" :backto="this.backto" :open="true">
      <form @submit.prevent="submitForm" class="modal-body" novalidate>
          <Input
              ref="nameField"
              v-model="form.name"
              type="text"
              placeholder="Название проекта"
              required
          ></Input>

        <select multiple v-model="form.developers">
          <option v-for="user in users" :key="user.id" :value="user.id">
            {{ user.firstname }}
          </option>
        </select>

          <Textarea
            ref="descriptionField"
            v-model="form.description"
            placeholder="Описание проекта"
            required
            >
          </Textarea>

        <div class="form-actions">
          <SubmitButton
          >
            {{ loading ? 'Создание...' : 'Создать' }}
          </SubmitButton>
        </div>
      </form>
  </BaseModal>
</template>

<script>
import router from "@/router/index.js";
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {userAdminService} from "@/services/UserAdminService.js";
import Input from "@/components/ui/Input.vue";
import Textarea from "@/components/ui/Textarea.vue"
import SubmitButton from "@/components/ui/SubmitButton.vue";
import Select from "@/components/ui/Select.vue";
import BaseModal from "@/components/BaseModal.vue";

export default {
  data() {
    return {
      title: "Добавить проект",
      backto: "/projects",
      form: {
        name: '',
        description: '',
        developers: [],
      },
      users: [],
      loading: false,
      loadingUsers: false,
    }
  },

  methods: {
    async submitForm() {
      this.loading = true
      console.log(this.form.developers)
      try {
        if (!this.validateForm()) {
          return;
        }

        const response = await projectAdminService.createProject({
          name: this.form.name,
          description: this.form.description
        })

        if(response.success){
          for(const developer of this.form.developers){
            console.log(developer)
            const assign = await projectAdminService.assignDeveloper({
              projectId: response.data.id,
              userId: developer
            })
          }
        }
alert('wait')
        this.$emit('success', response.data)

        this.close()
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading = false
      }
    },

    async handleLoadDevelopers(){
      this.loadingUsers = true
      try {
        const response = await userAdminService.getAllUsers()
        if (response.success) {
          this.users = response.data;
        }
      } catch (error) {
        console.error('Error loading users:', error)
      } finally {
        this.loadingUsers = false
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
  },
  components: {BaseModal, Select, SubmitButton, Input, Textarea},
}
</script>

<style scoped>

</style>