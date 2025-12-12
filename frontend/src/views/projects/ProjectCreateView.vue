<template>
  <BaseModal
      :backto="backto"
      :open="isOpen"
      v-show="isOpen"
      ref="baseModal"
  >
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
  components: {BaseModal, MultiSelect, SubmitButton, Input, Textarea},
  inject: ['close'],
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

        if (response.success) {
          const assign = await projectAdminService.assignDevelopers({
            projectId: response.data.id,
            userIds: this.form.developers.map(developer => developer.value)
          })
        }
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading = false
        this.$refs.baseModal.close()
      }
    },

    async handleLoadDevelopers() {
      this.loadingUsers = true
      try {
        const response = await userAdminService.getAllDevelopers()
        if (response.success) {
          this.prepareUsers(response.data);
        }
      } catch (error) {
        console.error('Error loading users:', error)
      } finally {
        this.loadingUsers = false
      }
    },

    prepareUsers(data) {
      for (const userData of data) {
        this.users.push({
          value: userData.id,
          label: userData.firstname + ' ' + userData.lastname + ' (' + userData.email + ')'
        })
      }
    },

    validateForm() {
      const fields = [this.$refs.nameField, this.$refs.descriptionField]
      let isValid = true

      Object.values(fields).forEach(field => {
        if (!field.isValid()) {
          isValid = false
        }
      })

      return isValid
    },
  },
  mounted() {
    this.handleLoadDevelopers()
  }
}
</script>

<style scoped>

</style>