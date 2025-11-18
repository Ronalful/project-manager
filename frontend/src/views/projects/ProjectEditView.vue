<template>
  <BaseModal :backto="backto" :open="true">
    <template #header>
      <h2>Проект #{{ projectId }}</h2>
    </template>

    <template #main>
      <form @submit.prevent="submitForm" class="modal-body" novalidate>

        <div class="modal-body">
          <div class="name-section">
            <h3 class="section-title">Название проекта</h3>
            <Input
                ref="nameField"
                v-model="project.name"
                type="text"
                placeholder="Название проекта"
                required
            ></Input>

          </div>
          <div class="description-section">
            <h3 class="section-title">Описание проекта</h3>
            <Textarea
                ref="descriptionField"
                v-model="project.description"
                placeholder="Описание проекта"
                required
            >
            </Textarea>
          </div>

          <div class="developers-section">
            <h3 class="section-title">Команда разработки</h3>
            <MultiSelect
                v-model="project.developers"
                :options="users"
                :preSelectedItems="actualDevelopers"
                placeholder="Назначить разработчиков..."
            />

          </div>
        </div>
        <SubmitButton
        >
          Редактировать
        </SubmitButton>
      </form>
    </template>
  </BaseModal>
</template>

<script>
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {userAdminService} from "@/services/UserAdminService.js";
import Input from "@/components/ui/Input.vue";
import Textarea from "@/components/ui/Textarea.vue"
import SubmitButton from "@/components/ui/SubmitButton.vue";
import {projectUserService} from "@/services/ProjectUserService.js";
import BaseModal from "@/components/BaseModal.vue";
import MultiSelect from "@/components/ui/MultiSelect.vue";

export default {
  components: {
    BaseModal, MultiSelect, SubmitButton, Input, Textarea
  },
  data() {
    return {
      projectId: '',
      backto: '/projects',
      project: {
        name: '',
        description: '',
        developers: [],
      },
      users: [],
      actualDevelopers: [],
    }
  },
  mounted() {
    this.projectId = this.$route.params.id;

    this.handleLoadProject()
    this.handleLoadDevelopers()
  },
  methods: {
    async handleLoadProject() {
      this.loading = true
      try {
        const response = await projectAdminService.getProjectInfo(this.projectId)
        if (response.success) {
          this.setProjectInfo(response.data)
        }
      } catch (error) {
        console.error('Error loading projects:', error)
      } finally {
        this.loading = false
      }
    },
    async handleLoadDevelopers() {
      this.loadingUsers = true
      try {
        const response = await userAdminService.getAllUsers()
        if (response.success) {
          this.prepareUsers(response.data, this.users);
        }
      } catch (error) {
        console.error('Error loading users:', error)
      } finally {
        this.loadingUsers = false
      }
    },
    setProjectInfo(data) {
      this.project.name = data.name
      this.project.description = data.description
      for (const developer of data.developers) {
        this.project.developers.push({
          id: developer.id,
          firstname: developer.firstname,
          lastname: developer.lastname,
          email: developer.email
        })
      }
      this.prepareUsers(this.project.developers, this.actualDevelopers)
    },
    prepareUsers(data, target) {
      for (const userData of data) {
        target.push({
          value: userData.id,
          label: userData.firstname + ' ' + userData.lastname + ' (' + userData.email + ')'
        })
      }
    },

    async submitForm() {
      this.loading = true
      try {
        if (!this.validateForm()) {
          return;
        }

        const response = await projectAdminService.editProject({
          id: this.projectId,
          name: this.project.name,
          description: this.project.description
        })

        // if(response.success){
        //   for(const developer of this.form.developers){
        //     const assign = await projectAdminService.assignDeveloper({
        //       projectId: response.data.id,
        //       userId: developer
        //     })
        //   }
        // }
        this.$emit('success', response.data)

        this.close()
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading = false
      }
    },
    isDeveloperSelected(userId) {
      return this.project.developers.some(dev => dev.id === userId);
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
  },
}
</script>

<style scoped>

</style>