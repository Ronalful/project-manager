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
import BaseModal from "@/components/BaseModal.vue";
import MultiSelect from "@/components/ui/MultiSelect.vue";

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
      usersData: [
        {
          id: '1',
          firstname: 'Ivan',
          lastname: 'Ivanov',
          email: 'ivanov@gmail.com'
        },
        {
          id: '2',
          firstname: 'Elen',
          lastname: 'Sergeeva',
          email: 'sergeeva@gmail.com'
        },
        {
          id: '3',
          firstname: 'Kate',
          lastname: 'Livanova',
          email: 'livanova@gmail.com'
        },
      ],
      users:[],
      loading: false,
      loadingUsers: false,
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
              userId: developer
            })
          }
        }
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
          this.prepareUsers(response.data);
        }
        else{
          this.prepareUsers(this.usersData);
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
  components: {BaseModal, Multiselect, SubmitButton, Input, Textarea},
}
</script>

<style scoped>

</style>