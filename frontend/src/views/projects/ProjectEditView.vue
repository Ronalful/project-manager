<template>
  <BaseModal
      :backto="backto"
      :open="true"
      ref="baseModal"
  >
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
          {{ loading.editing ? 'Сохранение...' : 'Редактировать' }}
        </SubmitButton>
      </form>
    </template>

    <template #footer>
      <DeleteButton
          @click="deleteProject"
      >
        {{ loading.deleting ? 'Удаление...' : 'Удалить проект' }}
      </DeleteButton>
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
import DeleteButton from "@/components/ui/DeleteButton.vue";

export default {
  components: {BaseModal, MultiSelect, Input, Textarea, SubmitButton, DeleteButton},
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
      loading: {
        loading: false,
        users: false,
        editing: false,
        deleting: false,
      }
    }
  },
  methods: {
    async handleLoadProject() {
      this.loading.loading = true
      try {
        const response = await projectAdminService.getProjectInfo(this.projectId)
        if (response.success) {
          this.setProjectInfo(response.data)
        }
      } catch (error) {
        console.error('Error loading projects:', error)
      } finally {
        this.loading.loading = false
      }
    },
    async handleLoadDevelopers() {
      this.loading.users = true
      try {
        const response = await userAdminService.getAllDevelopers()
        if (response.success) {
          this.prepareUsers(response.data, this.users);
        }
      } catch (error) {
        console.error('Error loading users:', error)
      } finally {
        this.loading.users = false
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
      this.loading.editing = true
      try {
        if (!this.validateForm()) {
          return;
        }

        const response = await projectAdminService.editProject({
          id: this.projectId,
          name: this.project.name,
          description: this.project.description
        })

        if (response.success) {
          await this.assignAndUnassignDevelopers()
        }
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading.editing = false
        this.$refs.baseModal.close()
      }
    },
    async deleteProject() {
      try {
        this.loading.deleting = true
        const response = await projectAdminService.deleteProject(this.projectId)
      } catch (error) {
        console.error('Error creating project:', error)
      } finally {
        this.loading.deleting = false
        this.$refs.baseModal.close()
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

    async assignAndUnassignDevelopers() {
      const allDevs = new Set(this.users.map(dev => dev.value))
      const currentDevs = new Set(this.actualDevelopers.map(dev => dev.value))
      const selectedDevs = new Set(this.project.developers.map(dev => dev.value))

      const addedDevs = [...selectedDevs].filter(devId => !currentDevs.has(devId))
      const removedDevs = [...currentDevs].filter(devId => !selectedDevs.has(devId))

      console.log('Added:', addedDevs)
      console.log('Removed:', removedDevs)
      console.log('Current:', [...currentDevs])
      console.log('Selected:', [...selectedDevs])
      if (addedDevs) {
        const assign = await projectAdminService.assignDevelopers({
          projectId: this.projectId,
          userId: addedDevs
        })
      }

      if (removedDevs) {
        const unassign = await projectAdminService.unassignDevelopers({
          projectId: this.projectId,
          userId: removedDevs
        })
      }
    },
  },

  mounted() {
    this.projectId = this.$route.params.id;

    this.handleLoadProject()
    this.handleLoadDevelopers()
  },
}
</script>

<style scoped>

</style>