<template>
  <BaseModal :backto="backto" :open="isOpen">
    <template #header>
      <h2>Добавить проект</h2>
    </template>

    <template #main>
      <form @submit.prevent="submitForm" class="modal-body" novalidate>
        <Input
            ref="nameField"
            v-model="form.name"
            type="text"
            placeholder="Название проекта"
            required
        />

        <MultiSelect
            v-model="form.developers"
            :options="users"
            placeholder="Назначить разработчиков..."
        />

        <Textarea
            ref="descriptionField"
            v-model="form.description"
            placeholder="Описание проекта"
            required
        />

          <SubmitButton>
            {{ loading ? 'Создание...' : 'Создать' }}
          </SubmitButton>
      </form>
    </template>
  </BaseModal>
</template>

<script>
import router from "@/router/index.js";
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {userAdminService} from "@/services/UserAdminService.js";
import Input from "@/components/ui/Input.vue";
import Textarea from "@/components/ui/Textarea.vue"
import SubmitButton from "@/components/ui/SubmitButton.vue";
import BaseModal from "@/components/BaseModal.vue";
import MultiSelect from "@/components/ui/MultiSelect.vue";

export default {
  data() {
    return {
      backto: "/projects",
      form: {
        name: '',
        description: '',
        developers: [],
      },
      users:[],
      loading: false,
      loadingUsers: false,
      isOpen: true,
    }
  },

  methods: {
    async submitForm() {
      this.loading = true
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
            const assign = await projectAdminService.assignDeveloper({
              projectId: response.data.id,
              userId: developer.value
            })
          }
        }
        this.isOpen = false
        this.$emit('success', response.data)

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
          this.prepareUsers(response.data);
        }
      } catch (error) {
        console.error('Error loading users:', error)
      } finally {
        this.loadingUsers = false
      }
    },

    prepareUsers(data){
      for (const userData of data){
        this.users.push({
          value: userData.id,
          label: userData.firstname + ' ' + userData.lastname + ' (' + userData.email + ')'
        })
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
  components: {BaseModal, MultiSelect, SubmitButton, Input, Textarea},

  mounted() {
    this.handleLoadDevelopers()
  }
}
</script>

<style scoped>

</style>