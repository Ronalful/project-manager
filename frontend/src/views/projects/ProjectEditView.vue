<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="project-title">Проект #{{ projectId }}</h2>
        <button @click="close" class="close-btn">&times;</button>
      </div>

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
                placeholder="Назначить разработчиков..."
            />

          </div>
        </div>
        <SubmitButton
        >
          Редактировать
        </SubmitButton>
      </form>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";
import {projectAdminService} from "@/services/ProjectAdminService.js";
import {userAdminService} from "@/services/UserAdminService.js";
import Input from "@/components/ui/Input.vue";
import Textarea from "@/components/ui/Textarea.vue"
import SubmitButton from "@/components/ui/SubmitButton.vue";
import Select from "@/components/ui/Select.vue";
import {projectUserService} from "@/services/ProjectUserService.js";
import {compile} from "vue";
import MultiSelect from "@/components/ui/MultiSelect.vue";

export default {
  data() {
    return {
      projectId: '',
      project: {
        name: 'Project',
        description: 'проект балванка',
        developers: [{
          id: '1',
          firstname: 'Ivan',
          lastname: 'Ivanov',
          email: 'ivanov@gmail.com'
        }
        ],
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
    }
  },
  mounted() {
    // запрос в бэк
    this.projectId = this.$route.params.id;

    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        this.close()
      }
    }
    document.addEventListener('keydown', handleEscape)

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
    },
    prepareUsers(data){
      for (const userData of data){
        this.users.push({
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

  components: {
    MultiSelect,
    Select, SubmitButton, Input, Textarea
  },

  emits: ['close', 'success'],

  setup(props, {emit}) {

    const close = () => {
      // Закрываем модалку через эмит или роутер
      emit('close')
      // Или через роутер
      if (window.history.length > 1) {
        router.back()
      } else {
        router.push('/projects')
      }
    }

    return {
      close
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: var(--background2);
  border-radius: 16px;
  box-shadow: var(--shadow);
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow: hidden;
  animation: modalAppear 0.3s ease-out;
}

@keyframes modalAppear {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.modal-header {
  padding: 24px;
  border-bottom: 1px solid var(--border);
  display: flex;
}

.project-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--font-main);
  margin: 0;
  flex: 1;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--font-main);
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  max-height: calc(90vh - 100px);
}

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--font-additional);
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid var(--border);
}

.description-section {
  margin-bottom: 32px;
}

@media (max-width: 640px) {
  .modal-content {
    margin: 10px;
    max-width: none;
  }

  .modal-body {
    padding: 20px;
  }
}
</style>